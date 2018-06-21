


/**
 * [createLight description]
 * @param  position
 * @param  radius     the radius of the sphere
 * @param  resources
 * @param  showSphere whether or not the sphere should be visible
 * @param  uniform    optional
 * @return            the created Light (TransformationSGNode)
 */
function createLight(position, radius, resources, showSphere, uniform){
  function createLightSphere(radius) {
    return new ShaderSGNode(createProgram(gl, resources.vs_phong, resources.fs_phong), [
      new RenderSGNode(makeSphere(radius,30,30))
    ]);
  }

  let light = new LightSGNode();
  light.ambient = [0, 0, 0, 1];
  light.diffuse = [1, 1, 1, 1];
  light.specular = [1, 1, 1, 1];
  light.position = position;
  if(uniform){
    light.uniform = uniform;
  }
  if(showSphere === true){
    light.append(createLightSphere(radius));
  }

  let rotLight = new TransformationSGNode(mat4.create(), [
      light,
  ]);

  // moves the light according to the timeInMilliseconds, //TODO if not needed -> remove
  rotLight.move = function(timeInMilliseconds, factor){
     rotLight.matrix = glm.rotateY(timeInMilliseconds*factor);
  };
  // moves the light to a specific position
  rotLight.moveTo = function(position){
     light.position = position;
  };
  return rotLight;
}
