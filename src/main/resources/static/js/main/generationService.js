import {OBJLoader2} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/OBJLoader2.js';
import {MTLLoader} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/MTLLoader.js';
import {MtlObjBridge} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/obj2/bridge/MtlObjBridge.js';

class generationService {

    constructor() {
        this.scene = new THREE.Scene();
        this.worldObjects = {};
    }

    initWorld() {
        // Setup van alle dingen required voor de wereld zelf
        this.setupWorld();

        // Import alle models die nodig zijn
        this.importModel("floating_island",200,15,40,15,0);
        this.importModel("CUPIC_AIRSHIP",0.05,-30,30,10,0);
        // importModel("chest",0.5,10,0.5,12,0.5);
        // importModel("house",0.4,20,0.15,15,0.5);
        // importModel("house",0.4,10,0.15,12,1.5);
        // importModel("house",0.4,11,0.15,8,1.25);
        this.importModel("lighthouse",0.7,-18,24,25,1.5);
    }

    setupWorld() {
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
        const intensity = 1;
        const light = new THREE.AmbientLight(color, intensity);
        this.scene.add(light);

        var directionalLight = new THREE.DirectionalLight(color, 0.5);
        directionalLight.castShadow = true;
        this.scene.add(directionalLight);

        var geometry = new THREE.PlaneGeometry(1000, 1000, 1000);
        var material = new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/water.jpg"), side: THREE.DoubleSide });
        var plane = new THREE.Mesh(geometry, material);
        plane.rotation.x = Math.PI / 2.0;
        plane.position.x = 15;
        plane.position.z = 15;
        plane.position.y = -3;
        this.scene.add(plane);
    }

    setupAudio() {
        // // create an AudioListener and add it to the camera
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

    importModel(name, size = 1, xpos = 0, ypos = 0, zpos = 0, rotation = 0) {
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
                root.rotation.y = Math.PI * rotation;
                this.scene.add(root);
            });
        });
    }

    getScene() {
        return this.scene;
    }

    updateObject(command) {
        // Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
        // dan wordt het 3D model eerst aangemaakt in de 3D wereld.
        if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0) {
            console.log('updating 3D object of: ', command.parameters.type);

            if (command.parameters.type === 'robot') {
                this.createRobot(command);
            }
            if (command.parameters.type === 'truck') {
                this.createRobot(command);
            }
        }
        /*
       * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
       */
        const object = this.worldObjects[command.parameters.uuid];

        if (object == null)
            return;

        object.position.x = command.parameters.x;
        object.position.y = command.parameters.y;
        object.position.z = command.parameters.z;

        object.rotation.x = command.parameters.rotationX;
        object.rotation.y = command.parameters.rotationY;
        object.rotation.z = command.parameters.rotationZ;
    }

    createRobot(command) {
        const geometry = new THREE.BoxGeometry(0.9, 0.3, 0.9);
        const cubeMaterials = [
            new THREE.MeshBasicMaterial({
                map: new THREE.TextureLoader().load('assets/textures/robot_side.png'),
                side: THREE.DoubleSide
            }), // LEFT
            new THREE.MeshBasicMaterial({
                map: new THREE.TextureLoader().load('assets/textures/robot_side.png'),
                side: THREE.DoubleSide
            }), // RIGHT
            new THREE.MeshBasicMaterial({
                map: new THREE.TextureLoader().load('assets/textures/robot_top.png'),
                side: THREE.DoubleSide
            }), // TOP
            new THREE.MeshBasicMaterial({
                map: new THREE.TextureLoader().load('assets/textures/robot_bottom.png'),
                side: THREE.DoubleSide
            }), // BOTTOM
            new THREE.MeshBasicMaterial({
                map: new THREE.TextureLoader().load('assets/textures/robot_front.png'),
                side: THREE.DoubleSide
            }), // FRONT
            new THREE.MeshBasicMaterial({
                map: new THREE.TextureLoader().load('assets/textures/robot_front.png'),
                side: THREE.DoubleSide
            }) // BACK
        ];
        const robot = new THREE.Mesh(geometry, cubeMaterials);
        robot.position.y = 0.15;

        const group = new THREE.Group();
        group.add(robot);
        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
    }

    clearWorld() {
        // Yet to be implemented.
    }
}

export {generationService}