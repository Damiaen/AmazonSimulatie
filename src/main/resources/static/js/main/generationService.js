import {OBJLoader2} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/OBJLoader2.js';
import {MTLLoader} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/MTLLoader.js';
import {MtlObjBridge} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/obj2/bridge/MtlObjBridge.js';

class generationService {

    constructor() {
        this.scene = new THREE.Scene();
        this.worldObjects = {};
    }

    async initWorld() {
        return new Promise((resolve, reject) => {
            let generatingCompleted;
            this.setupWorld().then(response => { generatingCompleted = response });
            if(generatingCompleted) {
                resolve(true);
            } else {
                reject(false);
            }
        });
    }

    async setupWorld() {
        var skyboxGeometry = new THREE.BoxGeometry(1000, 1000, 1000);
        var skyboxMaterials = [
            new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //RIGHT
            new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //LEFT
            new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_up.png"), side: THREE.DoubleSide}), //TOP
            new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_down.png"), side: THREE.DoubleSide}), //BOTTOM
            new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //FRONT
            new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_base.png"), side: THREE.DoubleSide}), //BACK
        ];
        var skyboxMaterial = new THREE.MeshFaceMaterial(skyboxMaterials);
        var skybox = new THREE.Mesh(skyboxGeometry, skyboxMaterial);
        skybox.position.y = 1;
        this.scene.add(skybox);

        const color = 0xFFFFFF;
        const intensity = 0.4;
        const light = new THREE.AmbientLight(color, intensity);
        this.scene.add(light);

        var directionalLight = new THREE.DirectionalLight('#fff2cf', 0.8);
        directionalLight.castShadow = true;
        directionalLight.position.set(0.5,0.5,1);
        this.scene.add(directionalLight);

        // Import alle models die nodig zijn
        await this.importModel("floating_island",400,0,40,0,0);
        return true;
    }

    setupAudio() {
        // create an AudioListener and add it to the camera
        // var listener = new THREE.AudioListener();
        // camera.add( listener );
        //
        // // create a global audio source
        // var sound = new THREE.Audio( listener );
        //
        // // load a sound and set it as the Audio object's buffer
        // var audioLoader = new THREE.AudioLoader();
        // audioLoader.load( '../../assets/sound/background_music.ogg', function( buffer ) {
        //     sound.setBuffer( buffer );
        //     sound.setLoop( true );
        //     sound.setVolume( 0.5 );
        //     sound.play();
        // });
    }

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

    async updateObject(command) {
        // Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
        // dan wordt het 3D model eerst aangemaakt in de 3D wereld.
        const modelExists = await this.generateModel(command);
        console.log(modelExists);

        if (modelExists) {
            const object = this.worldObjects[command.parameters.uuid];

            if (object == null)
                return;

            object.position.x = command.parameters.x;
            object.position.y = command.parameters.y;
            object.position.z = command.parameters.z;
        }

        /*
        * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
        */

    }

    async generateModel(command) {
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
            return true;
        }
        return true;
    }

    async createBalloon(command) {
        const robot = await this.importDynamicModel("balloon",4,0.5, command);
        console.log('Added robot model to 3D world: ', robot);

        const group = new THREE.Group();
        group.add(robot);
        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
    }

    async createShip(command) {
        const ship = await this.importDynamicModel("CUPIC_AIRSHIP", 0.20, 0.5, command);
        console.log('Added ship model to 3D world: ', ship);

        const group = new THREE.Group();
        group.add(ship);
        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
    }

    async createCrate(command) {
        const crate = await this.importDynamicModel("crate", 0.20, 0.5, command);
        console.log('Added crate model to 3D world: ', crate);

        const group = new THREE.Group();
        group.add(crate);
        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
    }

    async clearWorld() {
        this.worldObjects = {};
        let children_to_remove = [];

        this.scene.traverse(function(child){
            if(child.type == "Group"){
                children_to_remove.push(child);
            }
        });

        for (let child of children_to_remove) {
            if (child.name != "island") {
                this.scene.remove(child);
            }
        }
    }
}

export {generationService}