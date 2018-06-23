//the OpenGL context
var gl = null;

//scene graph nodes
var root = null;

var farmHuman1;

var sun,lamp;

//load the shader resources using a utility function
loadResources({
  vs_phong: 'shader/phong.vs.glsl',
  fs_phong: 'shader/phong.fs.glsl',
  vs_single: 'shader/single.vs.glsl',
  fs_single: 'shader/single.fs.glsl',

  //textures
  tex_lava: 'textures/lava.jpg',

  //complex models -> use factory functions
  human_head: 'models/human/head.obj',
  human_body: 'models/human/body.obj',
  human_arm: 'models/human/arm.obj',
  human_leg: 'models/human/leg.obj',

  treestump_lod0 : 'models/trees/pine_stump_lod_0.obj',
  treestump_lod1 : 'models/trees/pine_stump_lod_1.obj',
  treestump_lod2 : 'models/trees/pine_stump_lod_2.obj',
  treeleaves_lod0 : 'models/trees/pine_leaves_lod_0.obj',
  treeleaves_lod1 : 'models/trees/pine_leaves_lod_1.obj',
  treeleaves_lod2 : 'models/trees/pine_leaves_lod_2.obj',

  castle_bridge: 'models/castle/bridge.obj',
  castle_floor: 'models/castle/floor.obj',
  castle_walls: 'models/castle/walls.obj',

  floor: 'models/floor.obj',

  //simple models -> use createSimpleModel()
  hoe: 'models/hoe.obj',
  dock: 'models/dock.obj',
  rose: 'models/rose.obj',
  rod: 'models/rod.obj',
  fish: 'models/fish.obj'
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
  updatePannelFromCamera();
}

/**
 * creates the initial SceneGraph and returns it's root node
 */
function createSceneGraph(gl, resources) {
  //compile and link shader program and create root node of SceneGraph with it
  const root = new ShaderSGNode(createProgram(gl, resources.vs_phong, resources.fs_phong));


  //root.append(createFarmHouse(16, 8, 6, 60, 10, -105));
  root.append(createFloor(resources));

  farmHuman1 = createHuman(resources, 0.8, {texture: resources.tex_lava}, null, {diffuse:[0,0,1,1]});

  //green: {diffuse: [0,0.6,0,1]}
  root.append(createFarmHouse(16,8,6, {texture: resources.tex_lava}, 60, 10, 70));

  farmHuman1 = createHuman(resources, 0.8,
    {texture: resources.tex_lava, specular: [1,1,1,1], shininess: 0.5},
    null,
    {diffuse:[0,0,1,1]}
  );
  root.append(farmHuman1.root);
  createTool(resources.hoe, farmHuman1, "right", {texture: resources.tex_lava});
  farmHuman1.tool = null;
  createTool(resources.rose, farmHuman1, "mouth", {diffuse: [1,0,0,1], specular: [1,0,0,1], shininess: 10});
  farmHuman1.tool = null;
  createTool(resources.rod, farmHuman1, "left", {diffuse: [0.26,0.15,0,1]});

  //TODO: remove testmodels
  root.append(createSimpleModel( resources.dock, {diffuse: [0.26,0.15,0,1]}, {translation: [0,2,20]} ));
  root.append(createPineTree(resources,
    {diffuse: [0,0,1,1]},
    {texture: resources.tex_lava},
    {translation: [5,0,0]}
  ));
  root.append(createSimpleModel(resources.fish, {texture: resources.tex_lava}, {translation: [-5,2,0]}));
  root.append(createCastle(resources,
    {diffuse: [1,0,1,1]},
    getWoodMaterial(),
    {translation: [-40,0,35], yRotation: -90}
  ));

  createAndAddLights(root, resources);

  {
    //TODO: create mountains
  }

  return root;
}

function createAndAddLights(root, resources){
    sun = createLight([0, 5, 40], 4, resources, false);
    root.append(sun);
    lamp = createLight([0, 1, 2], 0.4, resources, true, 'u_light2');
    root.append(lamp);
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
  const context = createSGContext(gl, mat4.perspective(mat4.create(), glm.deg2rad(30), gl.drawingBufferWidth / gl.drawingBufferHeight, 0.01, 1000));
  //arguments: matOut, viewerPos, pointToLookAt, up_Vector
  let lookAtMatrix = mat4.lookAt(mat4.create(), camera.position, camera.target, [0,1,0]);


  context.viewMatrix = lookAtMatrix;

  //TODO: animate objects by rotating/translating nodes using timeInMilliseconds
  sun.move(timeInMilliseconds,0.05);
  lamp.move(timeInMilliseconds,0.04);

  //lamp.moveTo(camera.target);

  //combination example: farmHuman1.root.matrix = mat4.multiply(mat4.create(), glm.translate(0.001 * timeInMilliseconds, 0, 0), glm.rotateY(timeInMilliseconds*0.05));

  //start rendering SceneGraph
  root.render(context);
  //request another call as soon as possible (for animation)
  requestAnimationFrame(render);
}
