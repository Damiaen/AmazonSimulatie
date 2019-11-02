import {OBJLoader2} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/OBJLoader2.js';
import {MTLLoader} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/MTLLoader.js';
import {MtlObjBridge} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/obj2/bridge/MtlObjBridge.js';
import {socketService} from './socketService.js';

class generationService {

    constructor() {
        this.scene = new THREE.Scene();
        this.socket = new socketService();
        this.worldObjects = {};
        this.loadedObjects = [];
        this.loadedCrates = [];
    }

    async setupWorld() {
        return new Promise(resolve => {
            let skyboxGeometry = new THREE.BoxGeometry(1000, 1000, 1000);
            let skyboxMaterials = [
                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //RIGHT
                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //LEFT
                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_up.png"), side: THREE.DoubleSide}), //TOP
                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_down.png"), side: THREE.DoubleSide}), //BOTTOM
                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //FRONT
                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //BACK
            ];
            let skyboxMaterial = new THREE.MeshFaceMaterial(skyboxMaterials);
            let skybox = new THREE.Mesh(skyboxGeometry, skyboxMaterial);
            skybox.position.y = 1;
            this.scene.add(skybox);

            const color = 0xFFFFFF;
            const intensity = 0.4;
            const light = new THREE.AmbientLight(color, intensity);
            this.scene.add(light);

            let directionalLight = new THREE.DirectionalLight('#fff2cf', 0.8);
            directionalLight.castShadow = true;
            directionalLight.position.set(0.5,0.5,1);
            this.scene.add(directionalLight);

            this.importWorldModel().then((completedLoading) => {
                console.log('Loading worldmodel status:', completedLoading , 'Starting up socket');
                resolve(completedLoading);
                this.connect();
            });

        });
    }

    /*
    * Used for loading and importing the base models of the simulation (Only island atm)
    */
    async importWorldModel() {
        return new Promise(resolve => {
            console.log('Loading in world model');
            const objLoader = new OBJLoader2();
            const mtlLoader = new MTLLoader();
            mtlLoader.load('../../assets/models/floating_island.mtl', (mtlParseResult) => {
                let materials = MtlObjBridge.addMaterialsFromMtlLoader(mtlParseResult);
                objLoader.addMaterials(materials);
                objLoader.load('../../assets/models/floating_island.obj', (root) => {
                    root.scale.set(400, 400, 400);
                    root.position.x = 0;
                    root.position.y = 40;
                    root.position.z = 0;
                    root.castShadow = true;
                    root.receiveShadow = true;
                    root.name = "island";
                    root.rotation.y = 0;
                    this.scene.add(root);
                    resolve(true);
                });
            });
        });
    }

    getScene() {
        return this.scene;
    }

    async importModelDynamic(name, size, rotation, command) {
        return new Promise(resolve => {
            console.log('Loading in dynamic model: ', name);
            const objLoader = new OBJLoader2();
            const mtlLoader = new MTLLoader();
            mtlLoader.load('../../assets/models/' + name + '.mtl', (mtlParseResult) => {
                let materials = MtlObjBridge.addMaterialsFromMtlLoader(mtlParseResult);
                objLoader.addMaterials(materials);
                objLoader.load('../../assets/models/' + name + '.obj', (root) => {
                    root.scale.set(size, size, size);
                    root.castShadow = true;
                    root.receiveShadow = true;
                    root.rotation.y = Math.PI * rotation;
                    root.name = command.parameters.uuid;
                    const group = new THREE.Group();
                    group.add(root);
                    this.scene.add(group);
                    this.worldObjects[command.parameters.uuid] = group;
                    this.loadedObjects[command.parameters.uuid] = true;
                    resolve(true);
                });
            });
        });
    }

    async addCrateToRobot(command, hasCrate) {
        return new Promise(resolve => {
            const robot = this.scene.getObjectByName(command.parameters.uuid);
            const crateExists = robot.getObjectByName(command.parameters.uuid+ "_crate");
            if (hasCrate && !crateExists) {
                const robot = this.scene.getObjectByName(command.parameters.uuid);
                const objLoader = new OBJLoader2();
                const mtlLoader = new MTLLoader();
                mtlLoader.load('../../assets/models/crate.mtl', (mtlParseResult) => {
                    let materials = MtlObjBridge.addMaterialsFromMtlLoader(mtlParseResult);
                    objLoader.addMaterials(materials);
                    objLoader.load('../../assets/models/crate.obj', (root) => {
                        root.scale.set(3, 3, 3);
                        root.position.y = 1;
                        root.castShadow = true;
                        root.receiveShadow = true;
                        root.name = command.parameters.uuid+ "_crate";
                        root.rotation.y = Math.PI * 60;
                        robot.add(root);
                        resolve(true);
                    });
                });
            } else if (!hasCrate && crateExists)  {
                robot.remove(crateExists);
                resolve(true);
            }
        });
    }

    /*
    * Clear entire world of all its models, except the world itself (island)
    */
    async clearWorld() {
        this.worldObjects = {};
        this.loadedObjects = [];
        let children_to_remove = [];

        this.scene.traverse(function(child){
            if(child.type === "Group"){
                children_to_remove.push(child);
            }
        });

        for (let child of children_to_remove) {
            if (child.name !== "island") {
                this.scene.remove(child);
            }
        }
    }

    async connect() {
        this.socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/connectToSimulation");

        this.socket.addEventListener('open', (e) => {
            console.log('Connected to server: ', e);
        });

        this.socket.onmessage = e => {
            let command = this.parseCommand(e.data);

            if (command.command === "object_update") {
                if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0 && Object.keys(this.loadedObjects).indexOf(command.parameters.uuid) < 0) {
                    this.loadedObjects[command.parameters.uuid] = false;

                    if (command.parameters.type === "robot") {
                        this.importModelDynamic("balloon",4,0.5, command, "test").then(function(completed) {
                            console.log("New model added of balloon added, gotten value: ", completed);
                        });
                    }

                    if (command.parameters.type === "ship") {
                        this.importModelDynamic("CUPIC_AIRSHIP", 0.20, 0.5, command).then(function(completed) {
                            console.log("New model added of airship added, gotten value: ", completed);
                        });
                    }

                    if (command.parameters.type === "crate") {
                        this.importModelDynamic("crate", 0.20, 0.5, command).then(function(completed) {
                            console.log("New model added of airship added, gotten value: ", completed);
                        });
                    }
                }
                if (this.loadedObjects[command.parameters.uuid]) {
                    if (command.parameters.type === "robot") {
                        if (command.parameters.status === "WORKING") {
                            this.addCrateToRobot(command, true);
                        } else {
                            this.addCrateToRobot(command, false);
                        }
                    }

                    let object = this.worldObjects[command.parameters.uuid];

                    object.position.x = command.parameters.x;
                    object.position.y = command.parameters.y;
                    object.position.z = command.parameters.z;
                }
            }
        };

        this.socket.onclose = e => {
            console.log('Error connecting. Clearing worldObjects and attempting to reconnect.', e.reason);
            this.clearWorld();
            setTimeout(() => {
                this.connect();
            }, 1000);
        };
    }

    parseCommand(input = "") {
        return JSON.parse(input);
    }
}

export {generationService}