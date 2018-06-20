

function createFloor(width, length){
  let floor = new MaterialSGNode(
    new RenderSGNode(makeRect(width, length))
  );

  floor.diffuse = [0,0.6,0,1];

  //rotate floor, then return it
  return new TransformationSGNode(glm.rotateX(-90), floor);
}


function createFarmHouse(width, length, height) {
  let farmhouse = new MaterialSGNode(
    new FarmhouseRenderSGNode(width, length, height)
  );

  farmhouse.diffuse = [0.6,0.6,0.6,1];

  return new TransformationSGNode(glm.translate(10,0,0), farmhouse);
}
