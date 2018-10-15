//the OpenGL context
var gl = null;

//list of currently running animations
var animations = [];

//scene graph nodes
var root = null;

var farmer1, farmer2, farmer3, farmer4;
var dancer1, dancer2, dancer3, dancer4, dancer5, dancer6, dancer7;
var fisher1, fisher2;
var fish1, fish2, fish3, fish4, fish5;

var sun,lamp;

//load the shader resources using a utility function
loadResources({
  vs_phong: 'shader/phong.vs.glsl',
  fs_phong: 'shader/phong.fs.glsl',

  //textures
  heightmap: 'textures/heightmap.jpg',
  alphamap: 'textures/alphamap.jpg',
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
  water: 'models/water.obj',
  hoe: 'models/hoe.obj',
  dock: 'models/dock.obj',
  rose: 'models/rose.obj',
  rod: 'models/rod.obj',
  fish: 'models/fish.obj'
}).then(function (resources) {
  init(resources);
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
  gl.depthFunc(gl.LESS);
  gl.enable(gl.BLEND);
  gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);

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
  let black = {
    diffuse: [0.1,0.1,0.1,1], ambient: [0.1,0.1,0.1,1], specular: [0.05,0.05,0.05,0], shininess: 10
  };
  let white = {
    diffuse: [0.9,0.9,0.9,1], ambient: [0.7,0.7,0.7,1], specular: [0.05,0.05,0.05,0], shininess: 10
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
    {scale: vec3FromFloat(0.82), translation: [160,0,-48], yRotation: 180}
  );
  farmer2 = createHuman(resources, red, red, darkblue,
    {scale: vec3FromFloat(0.75), translation: [98,0,-11], yRotation: -45}
  );
  farmer3 = createHuman(resources, skinMat, skinMat, darkblue,
    {scale: vec3FromFloat(0.9), translation: [160,0,45], yRotation: 120}
  );
  farmer4 = createHuman(resources, darkblue, skinMat, darkblue,
    {scale: vec3FromFloat(0.45), translation: [174,0,-35], yRotation: 50}
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
    {scale: vec3FromFloat(2.5), translation: [-275,0.1,154], yRotation: -90}
  ));
  dancer1 = createHuman(resources, black, black, black,
    {scale: vec3FromFloat(0.87), translation: [-261,1,172], yRotation: 37}
  );
  dancer2 = createHuman(resources, black, black, black,
    {scale: vec3FromFloat(0.78), translation: [-248,1,147], yRotation: 70}
  );
  dancer3 = createHuman(resources, black, skinMat, black,
    {scale: vec3FromFloat(0.83), translation: [-265,1,133], yRotation: 10}
  );
  dancer4 = createHuman(resources, white, skinMat, white,
    {scale: vec3FromFloat(0.92), translation: [-288,1,168], yRotation: 200}
  );
  dancer5 = createHuman(resources, black, white, black,
    {scale: vec3FromFloat(0.74), translation: [-296,1,137], yRotation: 45}
  );
  dancer6 = createHuman(resources, white, white, white,
    {scale: vec3FromFloat(0.94), translation: [-161,0,207], yRotation: 45}
  );
  dancer7 = createHuman(resources, black, white, black,
    {scale: vec3FromFloat(1), translation: [-168,0,205], yRotation: 225}
  );
  root.append(dancer1.root);
  root.append(dancer2.root);
  root.append(dancer3.root);
  root.append(dancer4.root);
  root.append(dancer5.root);
  root.append(dancer6.root);
  root.append(dancer7.root);
  createAndAddTool(resources.rose, dancer1, "mouth", red);
  createAndAddTool(resources.rose, dancer5, "right", red);
  createAndAddTool(resources.rose, dancer6, "mouth", red);
  createAndAddTool(resources.rose, dancer7, "mouth", red);

  //build the fishing lake
  function sit(fisher){
    let rotationMatrix = mat4.multiply(mat4.create(), glm.rotateX(90), glm.translate(0,-2.5,-3));
    fisher.right_leg.matrix = mat4.multiply(mat4.create(), fisher.right_leg.matrix, rotationMatrix);
    fisher.left_leg.matrix = mat4.multiply(mat4.create(), fisher.left_leg.matrix, rotationMatrix);
    fisher.root.matrix = mat4.multiply(mat4.create(), fisher.root.matrix, glm.translate(0,-1,0));
  }

  root.append(createSimpleModel(resources.dock,
    getWoodMaterial(),
    {translation: [223,19,430], yRotation: 30}
  ));
  fisher1 = createHuman(resources, red, red, darkblue,
    {scale: vec3FromFloat(0.75), translation: [241,18,456], yRotation: 200}
  );
  sit(fisher1);
  fisher2 = createHuman(resources, darkblue, skinMat, darkblue,
    {scale: vec3FromFloat(0.45), translation: [239,18.5,461], yRotation: 220}
  );
  sit(fisher2);
  root.append(fisher1.root);
  root.append(fisher2.root);
  createAndAddTool(resources.rod, fisher1, "left", getWoodMaterial());
  createAndAddTool(resources.rod, fisher2, "right", getWoodMaterial());

  fish1 = createSimpleModel(resources.fish, darkblue, {translation: [270,16,450], yRotation: 200});
  fish2 = createSimpleModel(resources.fish, darkblue, {translation: [240,14,470], yRotation: 190});
  fish3 = createSimpleModel(resources.fish, darkblue, {translation: [331,14,433], yRotation: 200});
  fish4 = createSimpleModel(resources.fish, darkblue, {translation: [180,12,540], yRotation: 290});
  fish5 = createSimpleModel(resources.fish, darkblue, {translation: [310,13,490], yRotation: 277});
  root.append(fish1);
  root.append(fish2);
  root.append(fish3);
  root.append(fish4);
  root.append(fish5);

  createAndAddLights(root, resources);
  root.append(createWater(resources));
  return root;
}


var lastRenderTime=0;
var sunTime = 0;
var animateSun = false;
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
  gl.clearColor(0.84,1,0.98,1);    //skybox color
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
    });
  }

  if(animateSun){
    sunTime = sun.animate(35,130,36,deltaTime);
    sun.moveToAngle(sunTime);
  }

  checkForAnimationProximity(timeInMilliseconds);
  displayCurrentEffect(timeInMilliseconds);
  //start rendering SceneGraph
  root.render(context);
  //request another call as soon as possible (for animation)
  requestAnimationFrame(render);
}


function displayCurrentEffect(timeInMilliseconds){
  let secondsAfterStart = (timeInMilliseconds - 1500) / 1000;

  if (secondsAfterStart < 5){
    displayText("Level of Details (Trees), Complex 3D Shape + Texture (Farmhouse)");
  }
  else if (secondsAfterStart < 9){
    displayText("Composed Models, Separate Animations (Humans)");
  }
  else if (secondsAfterStart < 12.5) {
    displayText("Terrain from Heightmap (Mountains)");
  }
  else if (secondsAfterStart < 21) {
    clearText();
  }
  else if (secondsAfterStart < 24) {
    displayText("Moving Lightsource (Sun)");
  }
  else if (secondsAfterStart < 30) {
    displayText("Transparency from Alphamap (Lake)");
  }
  else {
    clearText();
  }
}



function buildTrees(resources, root, leavesMat){
  let woodMaterial = getWoodMaterial();
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [222,0,76], scale: vec3FromFloat(1.9)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [257,0,148], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [233,0,191], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [263,0,223], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [264,0,254], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [199,0,183], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [122,0,245], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [200,0,-139], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [316,0,49], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [190,0,-113], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [195,0,92], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-221,0,60], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-280,0,60], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-331,0,52], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-260,0,7], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-218,0,263], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-260,0,7], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-248,0,287], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-232,0,314], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-194,0,330], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [165,0,115], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-251,0,33], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-290,0,2], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-336,0,-42], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-165,0,386], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [115,22,497], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [97,18,493], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [105,23,545], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [79,23,586], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-3,2,413], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-78,0,404], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-107,0,390], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [207,0,214], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [228,0,276], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [280,4,305], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [329,15,303], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [323,0,191], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [321,0,151], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [349,0,140], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [385,1,76], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [315,0,213], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [374,7,205], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [372,12,240], scale: vec3FromFloat(2)}));
  root.append(createPineTree(resources, woodMaterial, leavesMat, {translation: [-136,12,-82], scale: vec3FromFloat(2)}));
}
