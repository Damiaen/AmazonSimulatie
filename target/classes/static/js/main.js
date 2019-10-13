// 'use strict';

import { socketConnect } from './socketService.js';

function main() {


        var camera, scene, renderer;
        var cameraControls;


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
        socketConnect(scene);



        init();
        animate();


}

main();
