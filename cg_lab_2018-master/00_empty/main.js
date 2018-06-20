//the OpenGL context
var gl = null;

//scene graph nodes
var root = null;

//load the shader resources using a utility function
loadResources({
  vs: 'shader/empty.vs.glsl',
  fs: 'shader/empty.fs.glsl'
}).then(function (resources /*an object containing our keys with the loaded resources*/) {
  init(resources);

  //render one frame
  render(0);
});

/**
 * initializes OpenGL context, creates SceneGraph and loads buffers
 */
function init(resources) {
  //create a GL context
  gl = createContext();
  //enable depth test (render only the pixels closest to camera)
  gl.enable(gl.DEPTH_TEST);

  //create scenegraph
  root = createSceneGraph(gl, resources);

  //perform main camera flight, then initialise camera interaction
  performMainCameraFlight();
  initCameraInteraction(gl.canvas);
}



/**
 * creates the initial SceneGraph and returns it's root node
 */
function createSceneGraph(gl, resources) {
  //compile and link shader program and create root node of SceneGraph with it
  const root = new ShaderSGNode(createProgram(gl, resources.vs, resources.fs));

  { //TODO: create sun (moving light source)

  }

  root.append(createFloor(5, 10));
  root.append(createFarmHouse(5, 3, 2));

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
  let lookAtMatrix = mat4.lookAt(mat4.create(), camera.position, camera.target, [0,1,0]);
  //rotate scene according to camera rotation
  let mouseRotateMatrix = mat4.multiply(mat4.create(),
                          glm.rotateX(camera.rotation.y),
                          glm.rotateY(camera.rotation.x));
  context.viewMatrix = mat4.multiply(mat4.create(), lookAtMatrix, mouseRotateMatrix);

  //TODO: animate objects by rotating/translating nodes using timeInMilliseconds


  //start rendering SceneGraph
  root.render(context);
  //request another call as soon as possible (for animation)
  requestAnimationFrame(render);
}
