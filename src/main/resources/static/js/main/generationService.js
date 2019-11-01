import {OBJLoader2} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/OBJLoader2.js';
import {MTLLoader} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/MTLLoader.js';
import {MtlObjBridge} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/obj2/bridge/MtlObjBridge.js';

class generationService {

    constructor() {
        this.scene = new THREE.Scene();
        this.worldObjects = {};
    }

    async setupWorld() {
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

        await this.importModel("floating_island",400,15,40,15,0);

        return true;

        // var geometry = new THREE.PlaneGeometry(1000, 1000, 1000);
        // var material = new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/water.jpg"), side: THREE.DoubleSide });
        // var plane = new THREE.Mesh(geometry, material);
        // plane.rotation.x = Math.PI / 2.0;
        // plane.position.x = 15;
        // plane.position.z = 15;
        // plane.position.y = -300;
        // this.scene.add(plane);
    }

    /*
    * Used for loading and importing the base models of the simulation (Only island atm)
    */
    importModel(name, size, xpos, ypos, zpos, rotation) {
        return new Promise(resolve => {
            console.log('Loading in model: ', name);
            const objLoader = new OBJLoader2();
            const mtlLoader = new MTLLoader();
            mtlLoader.load('../../assets/models/' + name + '.mtl', (mtlParseResult) => {
                let materials = MtlObjBridge.addMaterialsFromMtlLoader(mtlParseResult);
                objLoader.addMaterials(materials);
                objLoader.load('../../assets/models/' + name + '.obj', (root) => {
                    root.scale.set(size, size, size);
                    root.position.x = xpos;
                    root.position.y = ypos;
                    root.position.z = zpos;
                    root.castShadow = true;
                    root.receiveShadow = true;
                    root.name = "island";
                    root.rotation.y = Math.PI * rotation;
                    this.scene.add(root);
                    resolve(true);
                });
            });
        });
    }

    /*
    * Import for loading in models dynamically, used for spawning crates and such
    */
    async importDynamicModel(name, size, rotation, command) {
        return new Promise(resolve => {
            console.log('Loading in dynamic model: ', name);
            const objLoader = new OBJLoader2();
            const mtlLoader = new MTLLoader();
            mtlLoader.load('../../assets/models/' + name + '.mtl', (mtlParseResult) => {
                let materials = MtlObjBridge.addMaterialsFromMtlLoader(mtlParseResult);
                objLoader.addMaterials(materials);
                objLoader.load('../../assets/models/' + name + '.obj', (root) => {
                    root.scale.set(size, size, size);
                    root.position.x = command.parameters.x;
                    root.position.y = command.parameters.y;
                    root.position.z = command.parameters.z;
                    root.castShadow = true;
                    root.receiveShadow = true;
                    root.rotation.y = Math.PI * rotation;
                    resolve(root);
                });
            });
        });
    }

    getScene() {
        return this.scene;
    }

    /*
    * Update model based on params gotten from command
    */
    async updateObject(command) {
        const object = await this.worldObjects[command.parameters.uuid];
        console.log('Updating following object:', object);

        if (object == null)
            return;

        object.position.x = command.parameters.x;
        object.position.y = command.parameters.y;
        object.position.z = command.parameters.z;

        // object.rotation.x = command.parameters.rotationX;
        // object.rotation.y = command.parameters.rotationY;
        // object.rotation.z = command.parameters.rotationZ;
    }

    /*
    * Generate model for the first time, only when backend send the create command
    */
    async generateObject(command) {
        if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0) {
            console.log('Adding new model to scene: ', command.parameters.type);
            if (command.parameters.type === 'robot') {
                await this.createBalloon(command);
            }
            if (command.parameters.type === 'ship') {
                await this.createShip(command);
            }
            if (command.parameters.type === 'crate') {
                await this.createCrate(command);
            }
        }
    }

    /*
    * Generate balloon model
    */
    async createBalloon(command) {
        const robot = await this.importDynamicModel("balloon",4,0.5, command);
        console.log('Added robot model to 3D world: ', robot);

        const group = new THREE.Group();
        group.add(robot);
        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
    }

    /*
    * Generate airship model
    */
    async createShip(command) {
        const ship = await this.importDynamicModel("CUPIC_AIRSHIP", 0.20, 0.5, command);
        console.log('Added ship model to 3D world: ', ship);

        const group = new THREE.Group();
        group.add(ship);
        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
    }

    /*
    * Generate crate model
    */
    async createCrate(command) {
        const crate = await this.importDynamicModel("crate", 0.20, 0.5, command);
        console.log('Added crate model to 3D world: ', crate);

        const group = new THREE.Group();
        group.add(crate);
        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
    }

    /*
    * Clear entire world of all its models, except the world itself (island, skybox etc.)
    */
    async clearWorld() {
        this.worldObjects = {};

        this.scene.traverse(function(child){
            if(child.type === "Group" && child.name !== "island"){
                this.scene.remove(child);
            }
        });
    }
}

export {generationService}