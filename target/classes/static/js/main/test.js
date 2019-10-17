        import {OBJLoader2} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/OBJLoader2.js';
        import {MTLLoader} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/MTLLoader.js';
        import {MtlObjBridge} from 'https://threejsfundamentals.org/threejs/resources/threejs/r108/examples/jsm/loaders/obj2/bridge/MtlObjBridge.js';

        function parseCommand(input = "") {
            return JSON.parse(input);
        }

        var socket;

        window.onload = function () {

                function importModel(name, size = 1, xpos = 0, ypos = 0, zpos = 0, rotation = 0) {
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
                                    scene.add(root);
                                });
                            });
                        }

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


                // create an AudioListener and add it to the camera
                var listener = new THREE.AudioListener();
                camera.add( listener );

                // create a global audio source
                var sound = new THREE.Audio( listener );

                // load a sound and set it as the Audio object's buffer
                var audioLoader = new THREE.AudioLoader();
                audioLoader.load( '../../assets/sound/background_music.ogg', function( buffer ) {
                    sound.setBuffer( buffer );
                    sound.setLoop( true );
                    sound.setVolume( 0.5 );
                    sound.play();
                });


                var skyboxGeometry = new THREE.BoxGeometry(1000, 1000, 1000);
                var skyboxMaterials = [
                    new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_ft.JPG"), side: THREE.DoubleSide}), //RIGHT
                    new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_bk.JPG"), side: THREE.DoubleSide}), //LEFT
                    new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_up.JPG"), side: THREE.DoubleSide}), //TOP
                    new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_dn.JPG"), side: THREE.DoubleSide}), //BOTTOM
                    new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_rt.JPG"), side: THREE.DoubleSide}), //FRONT
                    new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/skybox/sea_lf.JPG"), side: THREE.DoubleSide}), //BACK
                ];
                var skyboxMaterial = new THREE.MeshFaceMaterial(skyboxMaterials);
                var skybox = new THREE.Mesh(skyboxGeometry, skyboxMaterial);
                skybox.position.y = 1;
                scene.add(skybox);

    const color = 0xFFFFFF;
    const intensity = 1;
    const light = new THREE.AmbientLight(color, intensity);
    scene.add(light);

    var directionalLight = new THREE.DirectionalLight(color, 0.5);
    directionalLight.castShadow = true;
    scene.add(directionalLight );


                var geometry = new THREE.PlaneGeometry(1000, 1000, 1000);
                var material = new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/water.jpg"), side: THREE.DoubleSide });
                var plane = new THREE.Mesh(geometry, material);
                plane.rotation.x = Math.PI / 2.0;
                plane.position.x = 15;
                plane.position.z = 15;
                plane.position.y = -3;
                scene.add(plane);

/*
                var geometry = new THREE.PlaneGeometry(20, 20, 21);
                var material = new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("textures/sand.jpg"), side: THREE.DoubleSide });
                var plane = new THREE.Mesh(geometry, material);
                plane.rotation.x = Math.PI / 2.0;
                plane.position.x = 15;
                plane.position.z = 15;
                plane.position.y = 0.15;
                scene.add(plane);
*/
                 importModel("beach",1,15,0.5,15,0);
                 importModel("ship",10,-6,-0.3,20,0);
                 importModel("chest",0.5,10,0.5,12,0.5);
                 importModel("house",0.4,20,0.15,15,0.5);
                 importModel("house",0.4,10,0.15,12,1.5);
                 importModel("house",0.4,11,0.15,8,1.25);
                 importModel("lighthouse",0.4,23,0.15,7,1.5);
               //importModel("rock",3,15,1,7,1.5);

                var geometry = new THREE.PlaneGeometry(5, 10, 5);
                var material = new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/wood.jpg"), side: THREE.DoubleSide });
                var plane = new THREE.Mesh(geometry, material);
                plane.rotation.x = Math.PI / 2.0;
                plane.position.x = 1.5;
                plane.position.z = 15;
                plane.position.y = -0.4;
                scene.add(plane);

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
             * Hier wordt de socketcommunicatie geregeld. Er wordt een nieuwe websocket aangemaakt voor het webadres dat we in
             * de server geconfigureerd hebben als connectiepunt (/connectToSimulation). Op de socket wordt een .onmessage
             * functie geregistreerd waarmee binnenkomende berichten worden afgehandeld.
             */
            socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/connectToSimulation");
            socket.onmessage = function (event) {
                //Hier wordt het commando dat vanuit de server wordt gegeven uit elkaar gehaald
                var command = parseCommand(event.data);

                //Wanneer het commando is "object_update", dan wordt deze code uitgevoerd. Bekijk ook de servercode om dit goed te begrijpen.
                if (command.command == "object_update") {
                    //Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
                    //dan wordt het 3D model eerst aangemaakt in de 3D wereld.
                    if (Object.keys(worldObjects).indexOf(command.parameters.uuid) < 0) {
                        //Wanneer het object een robot is, wordt de code hieronder uitgevoerd.
                        if (command.parameters.type == "robot") {
                            var geometry = new THREE.BoxGeometry(0.9, 0.3, 0.9);
                            var cubeMaterials = [
                                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/robot_side.png"), side: THREE.DoubleSide }), //LEFT
                                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/robot_side.png"), side: THREE.DoubleSide }), //RIGHT
                                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/robot_top.png"), side: THREE.DoubleSide }), //TOP
                                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/robot_bottom.png"), side: THREE.DoubleSide }), //BOTTOM
                                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/robot_front.png"), side: THREE.DoubleSide }), //FRONT
                                new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load("../../assets/textures/robot_front.png"), side: THREE.DoubleSide }), //BACK
                            ];
                            var material = new THREE.MeshFaceMaterial(cubeMaterials);
                            var robot = new THREE.Mesh(geometry, material);
                            robot.position.y = 0.3;
                            robot.position.z = 15;

                            var group = new THREE.Group();
                            group.add(robot);

                            scene.add(group);
                            worldObjects[command.parameters.uuid] = group;
                        }
                    }

                    /*
                     * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
                     */
                    var object = worldObjects[command.parameters.uuid];

                    object.position.x = command.parameters.x;
                    object.position.y = command.parameters.y;
                    object.position.z = command.parameters.z;

                    object.rotation.x = command.parameters.rotationX;
                    object.rotation.y = command.parameters.rotationY;
                    object.rotation.z = command.parameters.rotationZ;
                }
            };

            init();
            animate();
        }