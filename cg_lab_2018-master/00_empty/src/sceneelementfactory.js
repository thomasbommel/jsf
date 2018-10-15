

function createFloor(resources){
  return new TextureSGNode(resources.heightmap, 'u_enableHeightmap',
    createSimpleModel(resources.floor,
       {diffuse: [0.25,0.47,0.17,1], ambient: [0.25,0.47,0.17,1]},
       {translation:[0,0,250]}
  ));;
}


function createWater(resources){
  return new TextureSGNode(resources.alphamap, 'u_enableAlphamap',
    createSimpleModel(resources.water,
      {diffuse: [0,0,1,1], ambient: [0,0,0.6,1]},//diffuseVecFromRGB(23,185,193), ambient: ambientVecFromRGB(23,185,193)},
      {translation: [431,0,397], scale: [0.8,1,0.75]}
  ));;
}


function createAndAddLights(root, resources){
    sun = new SunNode([0,0,750],'u_sun',null,{showSphere:true,sphereRadius:20});
    root.append(sun);
    lamp = new LightNode([234.6,21,456.3],'u_lamp1',null,{showSphere:true,sphereRadius:0.5});
    lamp.sphere.emission = [0.6,0,0,1];
    root.append(lamp);
}



function createFarmHouse(width, length, height, material, xPos, zPos, yRotation) {
  let farmhouse = new MaterialSGNode(
    new FarmhouseSGNode(width, length, height)
  );
  applyMaterial(farmhouse, material || getDefaultMaterial());
  farmhouse = wrapWithTextureSGNode(farmhouse, material);

  let transformation = {
    translation: [xPos||0, height/2, zPos||0],
    yRotation: yRotation || 0,
  };
  return wrapWithTransformationSGNode(farmhouse, transformation);
}

/**
 * Creates basic nodes for a model and applies a specified transformation and material to it.
 * Returns the TransformationSGNode for later animations of the model.
 */
function createSimpleModel(model, material, transformation){
  let node = new MaterialSGNode(new RenderSGNode(model));
  applyMaterial(node, material || getDefaultMaterial());
  node = wrapWithTextureSGNode(node, material);
  return wrapWithTransformationSGNode(node, transformation);
}



function createCastle(resources, stoneMaterial, woodMaterial, transformation){
  let floorNode = createSimpleModel(resources.castle_floor, stoneMaterial, transformation);
  floorNode.append(createSimpleModel(resources.castle_walls, stoneMaterial));
  floorNode.append(createSimpleModel(resources.castle_bridge, woodMaterial));
  return floorNode;
}



function createPineTree(resources, stumpMaterial, leavesMaterial, transformation){
  let position = [0,0,0];
  if (transformation && transformation.translation){
    position = transformation.translation;
  }

  let stumpNode = new MaterialSGNode(
    new LODRenderSGNode(position, resources.treestump_lod0, resources.treestump_lod1, resources.treestump_lod2)
  );
  applyMaterial(stumpNode, stumpMaterial);
  stumpNode = wrapWithTextureSGNode(stumpNode, stumpMaterial);

  let leavesNode = new MaterialSGNode(
    new LODRenderSGNode(position, resources.treeleaves_lod0, resources.treeleaves_lod1, resources.treeleaves_lod2)
  );
  applyMaterial(leavesNode, leavesMaterial);
  leavesNode = wrapWithTextureSGNode(leavesNode, leavesMaterial);

  stumpNode.append(leavesNode);
  return wrapWithTransformationSGNode(stumpNode, transformation);
}




function createHuman(resources, bodyMaterial, armMaterial, legMaterial, transformation) {
  let root = wrapWithTransformationSGNode(null, transformation);

  let head = createSimpleModel(resources.human_head, getSkinMaterial());
  let body = createSimpleModel(resources.human_body, bodyMaterial);

  //right_arm is at body.x + 1.15, therefore left has to be shifted to -1.15
  let right_arm = createSimpleModel(resources.human_arm, armMaterial);
  let left_arm = createSimpleModel(resources.human_arm, armMaterial, {translation: [-2.3,0,0]});

  //right_leg is at body.x + 0.425 --> shift left to -0.425
  let right_leg = createSimpleModel(resources.human_leg, legMaterial);
  let left_leg = createSimpleModel(resources.human_leg, legMaterial, {translation: [-0.85,0,0]});

  root.append(head);
  root.append(body);
  root.append(right_arm);
  root.append(left_arm);
  root.append(right_leg);
  root.append(left_leg);

  return {  //human struct containing TransformationSGNodes to animate
    root: root,
    head: head,
    body: body,
    right_arm: right_arm,
    left_arm: left_arm,
    tool: null,
    right_leg: right_leg,
    left_leg: left_leg
  }
}



/**
 * Creates a tool and gives it to a human.
 * Does not return anything but appends the tool's SGNode to a body part instead.
 * A human can hold a tool at either: "left", "right" or "mouth"
 */
function createAndAddTool(toolModel, human, whereToHoldTool, material){
  if (human.tool){  //remove previous tool from human
    human.right_arm.remove(human.tool);
    human.left_arm.remove(human.tool);
    human.head.remove(human.tool);
  }

  let tool = createSimpleModel(toolModel, material);
  let placement;
  if (whereToHoldTool == "mouth"){
    placement = mat4.multiply(mat4.create(), glm.translate(0.8, 5.8, -0.5), glm.rotateZ(90));
    human.head.append(tool);
  }
  else {
    let xPos = 1.15;  //hand offset from human.root
    placement = mat4.multiply(mat4.create(), glm.translate(xPos, 2.5, 0.8), glm.rotateX(-90));
    if (whereToHoldTool == "right"){
      human.right_arm.append(tool);
    }
    else if (whereToHoldTool == "left"){
      human.left_arm.append(tool);
    }
    else {
      console.log("ERROR! Invalid whereToHoldTool-String at createTool()");
    }
  }

  tool.matrix = placement;
  human.tool = tool;
}
