'use strict';

import { socketConnect } from "./socketService.js";

function parseCommand(input = "") {
    return JSON.parse(input);
}

function main() {

        var socket;

        var camera, scene, renderer;
        var cameraControls;

        var worldObjects = {};

        function init() {
            camera = new THREE.PerspectiveCamera(70, window.innerWidth / window.innerHeight, 1, 1000);
            cameraControls = new THREE.OrbitControls(camera);
            camera.position.z = 15;
            camera.position.y = 5;
            camera.position.x = 15;
            cameraControls.update();
            scene = new THREE.Scene();

            renderer = new THREE.WebGLRenderer({ antialias: true });
            renderer.setPixelRatio(window.devicePixelRatio);
            renderer.setSize(window.innerWidth, window.innerHeight + 5);
            document.body.appendChild(renderer.domElement);

            window.addEventListener('resize', onWindowResize, false);

            var geometry = new THREE.PlaneGeometry(30, 30, 32);
            var material = new THREE.MeshBasicMaterial({ color: 0xffffff, side: THREE.DoubleSide });
            var plane = new THREE.Mesh(geometry, material);
            plane.rotation.x = Math.PI / 2.0;
            plane.position.x = 15;
            plane.position.z = 15;
            scene.add(plane);

            var light = new THREE.AmbientLight(0x404040);
            light.intensity = 4;
            scene.add(light);
        }

        function onWindowResize() {
            camera.aspect = window.innerWidth / window.innerHeight;
            camera.updateProjectionMatrix();
            renderer.setSize(window.innerWidth, window.innerHeight);
        }

        function animate() {
            requestAnimationFrame(animate);
            cameraControls.update();
            renderer.render(scene, camera);
        }

       /*
        * Setting up socket connection, this will run while the application is on.
        * See docs in SocketService for more information.
        */
        SocketService.connect();

        /*
         * Hier wordt de socketcommunicatie geregeld. Er wordt een nieuwe websocket aangemaakt voor het webadres dat we in
         * de server geconfigureerd hebben als connectiepunt (/connectToSimulation). Op de socket wordt een .onmessage
         * functie geregistreerd waarmee binnenkomende berichten worden afgehandeld.
         */
        // socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/connectToSimulation");
        // socket.onmessage = function (event) {
        //     //Hier wordt het commando dat vanuit de server wordt gegeven uit elkaar gehaald
        //     var command = parseCommand(event.data);
        //
        //     //Wanneer het commando is "object_update", dan wordt deze code uitgevoerd. Bekijk ook de servercode om dit goed te begrijpen.
        //     if (command.command == "object_update") {
        //         //Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
        //         //dan wordt het 3D model eerst aangemaakt in de 3D wereld.
        //         if (Object.keys(worldObjects).indexOf(command.parameters.uuid) < 0) {
        //             //Wanneer het object een robot is, wordt de code hieronder uitgevoerd.
        //             if (command.parameters.type == "robot") {
        //                 var geometry = new THREE.BoxGeometry(0.9, 0.3, 0.9);
        //                 var cubeMaterials = [
        //                     new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../assets/textures/robot_side.png"), side: THREE.DoubleSide }), //LEFT
        //                     new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../assets/textures/robot_side.png"), side: THREE.DoubleSide }), //RIGHT
        //                     new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../assets/textures/robot_top.png"), side: THREE.DoubleSide }), //TOP
        //                     new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../assets/textures/robot_bottom.png"), side: THREE.DoubleSide }), //BOTTOM
        //                     new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../assets/textures/robot_front.png"), side: THREE.DoubleSide }), //FRONT
        //                     new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../assets/textures/robot_front.png"), side: THREE.DoubleSide }), //BACK
        //                 ];
        //                 var material = new THREE.MeshFaceMaterial(cubeMaterials);
        //                 var robot = new THREE.Mesh(geometry, material);
        //                 robot.position.y = 0.15;
        //
        //                 var group = new THREE.Group();
        //                 group.add(robot);
        //
        //                 scene.add(group);
        //                 worldObjects[command.parameters.uuid] = group;
        //             }
        //         }

                /*
                 * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
                 */
                // var object = worldObjects[command.parameters.uuid];

                // object.position.x = command.parameters.x;
                // object.position.y = command.parameters.y;
                // object.position.z = command.parameters.z;
                //
                // object.rotation.x = command.parameters.rotationX;
                // object.rotation.y = command.parameters.rotationY;
                // object.rotation.z = command.parameters.rotationZ;
        //     }
        // };

        init();
        animate();


}

main();
