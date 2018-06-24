//the OpenGL context
var gl = null;

//list of currently running animations
var animations = [];

//scene graph nodes
var root = null;

var farmer1, farmer2, farmer3, farmer4;
var dancer1, dancer2, dancer3, dancer4, dancer5, dancer6, dancer7;
var sun,lamp;

//load the shader resources using a utility function
loadResources({
  vs_phong: 'shader/phong.vs.glsl',
  fs_phong: 'shader/phong.fs.glsl',
  vs_single: 'shader/single.vs.glsl',
  fs_single: 'shader/single.fs.glsl',

  //textures
  heightmap: 'textures/heightmap.jpg',
  tex_lava: 'textures/lava.jpg',
  tex_wood: 'textures/wood.jpg',
  tex_bricks: 'textures/bricks.jpg',

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



  //simple models -> use createSimpleModel()
  floor: 'models/floor.obj',
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
  //performMainCameraFlight();
  initCameraInteraction(gl.canvas);
  updatePannelFromCamera();
}

/**
 * creates the initial SceneGraph and returns it's root node
 */
function createSceneGraph(gl, resources) {
  //compile and link shader program and create root node of SceneGraph with it
  const root = new ShaderSGNode(createProgram(gl, resources.vs_phong, resources.fs_phong));

  //define simple color materials
  let pink = {
    diffuse: diffuseVecFromRGB(252,148,238), ambient: ambientVecFromRGB(252,148,239)
  };
  let red = {
    diffuse: diffuseVecFromRGB(170,5,30), ambient: ambientVecFromRGB(170,5,30)
  };
  let darkblue = {
    diffuse: diffuseVecFromRGB(10,0,70), ambient: ambientVecFromRGB(10,0,70)
  };
  let green = {
    diffuse: diffuseVecFromRGB(7,107,29), ambient: ambientVecFromRGB(7,107,29)
  };

  //BELOW: ACTUAL WORLD BUILDING
  //initiate worldbuilding
  root.append(createFloor(resources));

  let treeRootNode = new SGNode();
  buildTrees(resources, treeRootNode, green);
  root.append(treeRootNode);

  //build the farm
  root.append(createFarmHouse(64, 32, 24, {texture: resources.tex_wood}, 260, -40, 120));
  let skinMat = getSkinMaterial();

  farmer1 = createHuman(resources, pink, skinMat, pink,
    {scale: vec3FromFloat(0.82), translation: [160,0,-48], yRotation: 45}
  );
  farmer2 = createHuman(resources, red, red, darkblue,
    {scale: vec3FromFloat(0.75), translation: [98,0,-11], yRotation: -45}
  );
  farmer3 = createHuman(resources, skinMat, skinMat, darkblue,
    {scale: vec3FromFloat(0.9), translation: [160,0,45], yRotation: 120}
  );
  farmer4 = createHuman(resources, darkblue, skinMat, darkblue,
    {scale: vec3FromFloat(0.45), translation: [174,0,-35], yRotation: 33}
  );
  root.append(farmer1.root);
  root.append(farmer2.root);
  root.append(farmer3.root);
  root.append(farmer4.root);
  createAndAddTool(resources.hoe, farmer1, "right", getIronMaterial());
  createAndAddTool(resources.hoe, farmer2, "left", getIronMaterial());
  createAndAddTool(resources.hoe, farmer3, "right", getIronMaterial());
  createAndAddTool(resources.hoe, farmer4, "right", getIronMaterial());

  //build festival at the castle
  root.append(createCastle(resources,
    {texture: resources.tex_bricks},
    {texture: resources.tex_wood},
    {scale: [3,3,3], translation: [-275,0.1,154], yRotation: -90}
  ));


  //build the fishing lake
  root.append(createSimpleModel(resources.dock,
    getWoodMaterial(),
    {translation: [223,19,430], yRotation: 30}
  ));




  //ABOVE: ACTUAL WORLD BUILDING




  //createTool(resources.rose, farmer1, "mouth", {diffuse: [1,0,0,1], specular: [1,0,0,1], shininess: 10});

  //createTool(resources.rod, farmer1, "left", {diffuse: [0.26,0.15,0,1]});

  //TODO: remove testmodels
  let fish = createSimpleModel(resources.fish, {texture: resources.tex_lava}, {translation: [-5,2,0]});
  root.append(fish);

  //TODO: remove test animations
  let x = new Animation(2, [
    {node: fish, targetTransform: {translation: [-5,2,-5]}, duration: 1.5},
    {node: fish, targetTransform: {translation: [-5,2,5], scale: vec3FromFloat(3)}, duration: 1},
    {node: fish, targetTransform: {translation: [5,0,-5], yRotation: 180}, duration: 2},
    {node: fish, targetTransform: {translation: [-5,10,-5], scale: vec3FromFloat(1.5)}, duration: 6}]
  );
  x.startAnimation();

  createAndAddLights(root, resources);
  return root;
}


var lastRenderTime=0;
/**
 * renders a single frame
 */
function render(/*float*/ timeInMilliseconds){
  //keep window at maximum size possible
  checkForWindowResize(gl);
  let deltaTime = (timeInMilliseconds - lastRenderTime) /1000;
  lastRenderTime = timeInMilliseconds;

  //setup viewport
  gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight);
  gl.clearColor(0.9, 0.9, 0.9, 1.0);    //background color
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

  //create and setup context to use when rendering SceneGraph
  const context = createSGContext(gl, mat4.perspective(mat4.create(), glm.deg2rad(30), gl.drawingBufferWidth / gl.drawingBufferHeight, 0.01, 10000));
  //arguments: matOut, viewerPos, pointToLookAt, up_Vector
  let lookAtMatrix = mat4.lookAt(mat4.create(), camera.position, camera.target, [0,1,0]);
  context.viewMatrix = lookAtMatrix;

  //animate objects
  if (timeInMilliseconds > 1500) { //wait for site to load
    animations.forEach( function(anim) {
      anim.animate(deltaTime);
      //if (timeInMilliseconds < 3000) console.log(anim);
    });
  }

  //sun.moveToNoon();

  //sun.animate(0,360,10,deltaTime);
  //
  //sun.animateColor([0,0,0,1],[1,0,0,0],[0,0,0,1],[0,0,1,1],10,deltaTime);

  //combination example: farmer1.root.matrix = mat4.multiply(mat4.create(), glm.translate(0.001 * timeInMilliseconds, 0, 0), glm.rotateY(timeInMilliseconds*0.05));

  //start rendering SceneGraph
  root.render(context);
  //request another call as soon as possible (for animation)
  requestAnimationFrame(render);
}


function buildTrees(resources, root, leavesMat){
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [222,0,76], scale: vec3FromFloat(1.9)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [257,0,148], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [233,0,191], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [263,0,223], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [264,0,254], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [199,0,183], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [122,0,245], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [200,0,-139], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [316,0,49], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [190,0,-113], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [195,0,92], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-221,0,60], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-280,0,60], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-331,0,52], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-260,0,7], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-218,0,263], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-260,0,7], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-248,0,287], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-232,0,314], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-194,0,330], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [165,0,115], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-251,0,33], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-290,0,2], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-336,0,-42], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-165,0,386], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [115,22,497], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [97,18,493], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [105,23,545], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [79,23,586], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-3,2,413], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-78,0,404], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-107,0,390], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [207,0,214], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [228,0,276], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [280,4,305], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [329,15,303], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [323,0,191], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [321,0,151], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [349,0,140], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [385,1,76], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [315,0,213], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [374,7,205], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [372,12,240], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, getWoodMaterial(), leavesMat, {translation: [-136,12,-82], scale: vec3FromFloat(2)}));
}
