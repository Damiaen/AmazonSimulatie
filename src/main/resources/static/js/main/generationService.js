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

            this.importWorldModel().then().then((completedLoading) => {
                console.log('Loading worldmodel status:', completedLoading , 'Starting up socket');
                resolve(completedLoading);
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

    updateObject(command) {
        return new Promise(resolve => {
            this.generateModel(command).then().then((modelExists) => {
                if (modelExists) {
                    const object = this.worldObjects[command.parameters.uuid];
                    if (object == null)
                        return;

                    object.position.x = command.parameters.x;
                    object.position.y = command.parameters.y;
                    object.position.z = command.parameters.z;
                    resolve(true);
                }
            });
        });
    }

    /*
    * Generate model for the first time, only when backend send the create command
    */
    generateModel(command) {
        return new Promise(resolve => {
            if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0 && !this.loadedObjects.includes(command.parameters.uuid)) {
                console.log('Adding new model to scene: ', command);
                this.loadedObjects.push(command.parameters.uuid);
                if (command.parameters.type === 'robot') {
                    this.importModelDynamic("balloon",4,0.5, command).then(function(completed) {
                        console.log("New model added of balloon added, gotten value: ", completed);
                        resolve(completed);
                    });
                }
                if (command.parameters.type === 'ship') {
                    this.importModelDynamic("CUPIC_AIRSHIP", 0.20, 0.5, command).then(function(completed) {
                        console.log("New model added of airship added, gotten value: ", completed);
                        resolve(completed);
                    });
                }
                if (command.parameters.type === 'crate') {
                    this.importModelDynamic("crate", 0.20, 0.5, command).then(function(completed) {
                        console.log("New model added of crate added, gotten value: ", completed);
                        resolve(completed);
                    });
                }
            } else {
                console.log(this.worldObjects);
                resolve(true);
            }
        });
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
                    resolve(true);
                });
            });
        });
    }

    async addCrateToRobot(command, group) {
        return new Promise(resolve => {
            const objLoader = new OBJLoader2();
            const mtlLoader = new MTLLoader();
            mtlLoader.load('../../assets/models/crate.mtl', (mtlParseResult) => {
                let materials = MtlObjBridge.addMaterialsFromMtlLoader(mtlParseResult);
                objLoader.addMaterials(materials);
                objLoader.load('../../assets/models/crate.obj', (root) => {
                    root.scale.set(10, 10, 10);
                    root.position.y = 2;
                    root.castShadow = true;
                    root.receiveShadow = true;
                    root.rotation.y = Math.PI * 60;
                    group.add(root);
                    this.scene.add(group);
                    this.worldObjects[command.parameters.uuid] = group;
                    resolve(true);
                });
            });
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
        // Connection opened
        this.socket.addEventListener('open', (e) => {
            this.socket.binaryType = 'arraybuffer'; // important
            console.log('Connected to server with following event: ', e);
        });

        // Do something based on command
        this.socket.onmessage = e => {
            let command = this.parseCommand(e.data);

            //Wanneer het commando is "object_update", dan wordt deze code uitgevoerd. Bekijk ook de servercode om dit goed te begrijpen.
            if (command.command === "object_update") {
                //Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
                //dan wordt het 3D model eerst aangemaakt in de 3D wereld.
                if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0 && !this.loadedObjects.includes(command.parameters.uuid)) {
                    this.loadedObjects.push(command.parameters.uuid);
                    //Wanneer het object een robot is, wordt de code hieronder uitgevoerd.
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
                let object = this.worldObjects[command.parameters.uuid];

                object.position.x = command.parameters.x;
                object.position.y = command.parameters.y;
                object.position.z = command.parameters.z;

                object.rotation.x = command.parameters.rotationX;
                object.rotation.y = command.parameters.rotationY;
                object.rotation.z = command.parameters.rotationZ;
            }
        };

        // If socket disconnects try to reconnect
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