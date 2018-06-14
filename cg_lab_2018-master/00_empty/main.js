//the OpenGL context
var gl = null;

//camera struct
const camera = {
  //access with camera.rotation.x and camera.rotation.y
  rotation: getDefaultCameraRotation(),
  //access with camera.position[i] and camera.target[i] ;; i-values: 0=x, 1=y, 2=z
  position: getDefaultCameraPosition(),
  target: [0,0,0],   //calculated by utils.setCameraTarget(camera) in init and when camera is moved
  isPerformingFlight: false     //true during an animated camera flight (TODO!)
};
//camera default-values (as functions)
function getDefaultCameraRotation() { return {x: 0, y:-30}; }
function getDefaultCameraPosition() { return [0,15,-30]; }

//scene graph nodes
var root = null;

//load the shader resources using a utility function
loadResources({
  vs: 'shader/empty.vs.glsl',
  fs: 'shader/empty.fs.glsl'
}).then(function (resources /*an object containing our keys with the loaded resources*/) {
  init(resources);

  //render one frame
  render();
});

/**
 * initializes OpenGL context, creates SceneGraph and loads buffers
 */
function init(resources) {
  //create a GL context
  gl = createContext();

  //set initial target for camera
  setCameraTarget(camera);

  //create scenegraph
  root = createSceneGraph(gl, resources);

  initCameraInteraction(gl.canvas);
}

/**
 * initialize camera control
 * camera control is automatically disabled while camera.isPerformingFlight == true
 */
function initCameraInteraction(canvas) {
  const mouse = {
    pos: { x : 0, y : 0},
    leftButtonDown: false
  };

  function toPos(mouseEvent) {
    //convert to local coordinates
    const rect = canvas.getBoundingClientRect();
    return {
      x: mouseEvent.clientX - rect.left,
      y: mouseEvent.clientY - rect.top
    };
  }

  //on pressing LMB: update pos & set leftButtonDown = true
  canvas.addEventListener('mousedown', function(event) {
    mouse.pos = toPos(event);
    mouse.leftButtonDown = event.button === 0;
  });

  //when moving mouse: update pos and if LMB is down rotate camera
  canvas.addEventListener('mousemove', function(event) {
    const pos = toPos(event);
    const delta = { x : mouse.pos.x - pos.x, y: mouse.pos.y - pos.y };
    const mouseSensitivity = 0.25;   //factor to multiply with delta mouse movement

    if (mouse.leftButtonDown && !camera.isPerformingFlight) {
      //add the relative movement of the mouse to the rotation variables
  		camera.rotation.x += delta.x * mouseSensitivity;
  		camera.rotation.y += delta.y * mouseSensitivity;

      //keep rotation.x in interval ]-180,180]
      if (camera.rotation.x <= -180) camera.rotation.x += 360;
      else if (camera.rotation.x > 180) camera.rotation.x -= 360;

      //keep rotation.y in interval ]-180,180]
      if (camera.rotation.y <= -180) camera.rotation.y += 360;
      else if (camera.rotation.y > 180) camera.rotation.y -= 360;

      //TODO: OMAS?!?! folgendes machn ja oder nein? i glaub eig. nein aber wenn is ned mach is alles (noch) kaputt(er)/buggy(-ier)
      setCameraTarget(camera);
    }
    mouse.pos = pos;
  });

  //on releasing LMB: update pos & set leftButtonDown = false
  canvas.addEventListener('mouseup', function(event) {
    mouse.pos = toPos(event);
    mouse.leftButtonDown = false;
  });

  //handle key events
  document.addEventListener('keypress', function(event) {
    //https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent
    if (camera.isPerformingFlight) return;  //disable controls during flight
    displayText(event.code);    //TODO: debugging --> remove

    if (event.code === 'KeyR') {    //reset camera to defaults
      camera.rotation = getDefaultCameraRotation();
      camera.position = getDefaultCameraPosition();
      setCameraTarget(camera);
    }
    else if (event.code == 'KeyX'){   //"x-tra" (additional) camera flight
      //TODO: trigger additional animated cameraflight

    }
    else if (event.code == 'ArrowUp' || event.code == 'KeyW'){
      let direction = getCameraDirection(camera);
      for (let i in direction){
        camera.position[i] += direction[i];
      }
      setCameraTarget(camera);
      displayText("new pos: " + camera.position);
    }
    else if (event.code == 'ArrowDown' || event.code == 'KeyS'){
      let direction = getCameraDirection(camera);
      for (let i in direction){
        camera.position[i] -= direction[i];
      }
      setCameraTarget(camera);
      displayText("new pos: " + camera.position);
    }
  });
}

/**
 * creates the initial SceneGraph and returns it's root node
 */
function createSceneGraph(gl, resources) {
  //compile and link shader program and create root node of SceneGraph with it
  const root = new ShaderSGNode(createProgram(gl, resources.vs, resources.fs));

  { //TODO: create sun (moving light source)

  }

  root.append(createFloor());

  { //TODO: create mountains

  }

  return root;
}

function createFloor(){
  let floor = new RenderSGNode(makeRect(10, 10));

  //rotate floor, then return it
  return new TransformationSGNode(glm.rotateX(-90), floor);
}

/**
 * renders a single frame
 */
function render(/*float*/ timeInMilliseconds){
  //keep window at maximum size possible
  checkForWindowResize(gl);

  //setup viewport
  gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight);
  gl.clearColor(0.9, 0.9, 0.9, 1.0);    //background color
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

  //create and setup context to use when rendering SceneGraph
  const context = createSGContext(gl);
  //arguments: matOut, viewerPos, pointToLookAt, up_Vector
  context.viewMatrix = mat4.lookAt(mat4.create(), camera.position, camera.target, [0,1,0]);
  //rotate scene according to camera rotation
  context.sceneMatrix = mat4.multiply(mat4.create(),
                            glm.rotateY(camera.rotation.x),
                            glm.rotateX(camera.rotation.y));

  //TODO: animate objects by rotating/translating nodes using timeInMilliseconds


  //start rendering SceneGraph
  root.render(context);
  //request another call as soon as possible (for animation)
  requestAnimationFrame(render);
}
