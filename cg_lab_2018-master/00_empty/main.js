//the OpenGL context
var gl = null;

//camera constancts
const defaultCameraRotX = 0, defaultCameraRotY = 0;
const defaultCameraPosX = 0, defaultCameraPosY = 15, defaultCameraPosZ = -30;
//actual camera struct
const camera = {
  rotation: {   //access with e.g. camera.rotation.x
    x: defaultCameraRotX,
    y: defaultCameraRotY
  },
  //access with e.g. camera.position[i]
  position: [defaultCameraPosX, defaultCameraPosY, defaultCameraPosZ],
  target: [0, 0, 0],
  isPerformingFlight: false     //true during an animated camera flight (TODO!)
};

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
  gl = createContext(400 /*width*/, 400 /*height*/);

  //create scenegraph
  root = createSceneGraph(gl, resources);

  //TODO: only enable interaction after initial cameraflight is done
  initInteraction(gl.canvas);
}

/**
 * initialize camera control
 * camera control is automatically disabled while camera.isPerformingFlight == true
 */
function initInteraction(canvas) {
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

    if (event.code === 'KeyR') {    //reset camera (rotation & position)
      camera.rotation.x = defaultCameraRotX;
      camera.rotation.y = defaultCameraRotY;
      camera.position = [defaultCameraPosX, defaultCameraPosY, defaultCameraPosZ];
    }
    else if (event.code == 'KeyX'){   //"x-tra" (additional) camera flight
      //TODO: trigger additional animated cameraflight

    }
    else if (event.code == 'ArrowUp' || event.code == 'KeyW'){
      //TODO: move camera forwards
      camera.position[1] += 1; //TODO: remove testcode
      displayText("new pos: " + camera.position);
    }
    else if (event.code == 'ArrowDown' || event.code == 'KeyS'){
      //TODO: move camera backwards
      camera.position[1] -= 1; //TODO: remove testcode
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

  { //create floor
    let floor = new RenderSGNode(makeRect(10, 10));

    //rotate floor, then append it to root
    root.append(new TransformationSGNode(glm.rotateX(-90), floor));
  }

  { //TODO: create mountains

  }

  return root;
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
