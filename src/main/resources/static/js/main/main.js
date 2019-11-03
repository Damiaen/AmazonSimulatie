import {generationService} from "./generationService.js";

let camera, renderer;
let cameraControls;

let _generationService;

async function init() {
    camera = new THREE.PerspectiveCamera(70, window.innerWidth / window.innerHeight, 1, 1500);
    cameraControls = new THREE.OrbitControls(camera);
    camera.position.z = 15;
    camera.position.y = 140;
    camera.position.x = 200;
    cameraControls.update();

    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight + 5);
    document.body.appendChild(renderer.domElement);

    window.addEventListener('resize', onWindowResize, false);
    _generationService = new generationService();
    await _generationService.setupWorld();
    console.log('Completed setting up world, opening socket connection');

    frameStep();
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
}

function frameStep() {
    requestAnimationFrame(frameStep);
    cameraControls.update();
    renderer.render(_generationService.getScene(), camera);
}

window.onload = init;