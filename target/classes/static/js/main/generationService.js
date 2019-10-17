var camera, scene, renderer;

class generationService {

    constructor() {
        this.scene = new THREE.Scene();
        this.worldObjects = {};
    }

    initWorld() {
        var geometry = new THREE.PlaneGeometry(30, 30, 32);
        var material = new THREE.MeshBasicMaterial({ color: 0xffffff, side: THREE.DoubleSide });
        var plane = new THREE.Mesh(geometry, material);
        plane.rotation.x = Math.PI / 2.0;
        plane.position.x = 15;
        plane.position.z = 15;
        this.scene.add(plane);

        var light = new THREE.AmbientLight(0x404040);
        light.intensity = 4;
        this.scene.add(light);
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