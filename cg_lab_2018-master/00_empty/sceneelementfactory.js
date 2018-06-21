

function createFloor(width, length){
  let floor = new MaterialSGNode(
    new RenderSGNode(makeRect(width, length))
  );

  floor.diffuse = [0,0.6,0,1];

  //rotate floor, then return it
  return new TransformationSGNode(glm.rotateX(-90), floor);
}


function createFarmHouse(width, length, height, xPos, zPos, yRotation) {
  let farmhouse = new MaterialSGNode(
    new FarmhouseSGNode(width, length, height)
  );

  farmhouse.diffuse = [0.6,0.6,0.6,1];

  let placement = mat4.multiply(mat4.create(),
    glm.translate(xPos, height/2, zPos),
    glm.rotateY(yRotation)
  );
  return new TransformationSGNode(placement, farmhouse);
}

/** Creates a dock with the given position and y-rotation */
function createDock(resources, /*vec3*/ position, yRotation){
  let dock = new MaterialSGNode(
    new RenderSGNode(resources.dock)
  );
  dock.diffuse = [0.26,0.15,0,1];

  let placement = mat4.multiply(mat4.create(),
    glm.translate(position[0], position[1], position[2]),
    glm.rotateY(yRotation)
  );
  return new TransformationSGNode(placement, dock);
}




function createHuman(resources, scaleFactor) {
  //TODO: apply materials/textures
  let root = new TransformationSGNode(glm.scale(scaleFactor,scaleFactor,scaleFactor));

  let head = new TransformationSGNode(glm.translate(0,0,0), new RenderSGNode(resources.human_head));
  let body = new TransformationSGNode(glm.translate(0,0,0), new RenderSGNode(resources.human_body));

  //right_arm is at body.x + 1.15, therefore left has to be shifted to -1.15
  let right_arm = new TransformationSGNode(glm.translate(0,0,0), new RenderSGNode(resources.human_arm));
  let left_arm = new TransformationSGNode(glm.translate(-2.3,0,0), new RenderSGNode(resources.human_arm));

  //right_leg is at body.x + 0.425 --> shift left to -0.425
  let right_leg = new TransformationSGNode(glm.translate(0,0,0), new RenderSGNode(resources.human_leg));
  let left_leg = new TransformationSGNode(glm.translate(-0.85,0,0), new RenderSGNode(resources.human_leg));

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
 * Does not return anything but appends the tool's TransformationSGNode to human.tool instead.
 */
function createTool(toolModel, human, boolAddToRightHand){
  if (human.tool){  //remove previous tool from human.root
    human.root.remove(human.tool);
  }

  let xPos = 1.15;  //right hand offset from human.root
  if (!boolAddToRightHand){
    xPos *= -1; //add it to left hand
  }

  let placement = mat4.multiply(mat4.create(), glm.translate(xPos, 2.5, 0.8), glm.rotateX(-90));
  let tool = new TransformationSGNode(placement, new RenderSGNode(toolModel));
  human.tool = tool;
  human.root.append(tool);
}
