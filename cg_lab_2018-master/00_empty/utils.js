/**
 * This class contains several utility functions to be used in main.js
 */

/**
 * normalizes a vec3 (Float32 array with 3 elements) to a unit vector (length==1)
 */
function normalizeVec3(vec3){
  let length = Math.sqrt(Math.pow(vec3[0], 2) + Math.pow(vec3[1], 2) + Math.pow(vec3[2], 2));
  if (length != 0){
    for (let i in vec3){
      vec3[i] /= length;
    }
    return vec3;
  }
  else
    return [0,0,1]; //forward direction when x=y=0
}

/**
 * Returns the camera direction as x,y,z coordinates calculated from its rotation.
 * The returned vector is a normalized unit vector.
 */
function getCameraDirection(camera){
  //immediately return [0,0,0] if camera == null, otherwise assume a correct camera struct was passed
  if (typeof camera === 'undefined') return [0,0,0];

  let isCameraAustralian = false;   //bool to identify if camera is "standing on it's head"
  let isXAngleGreater90 = false;    //true if camera.rotation.x < -90 or > 90

  //calculate x (in interval [-1,1])
  let xDirection = camera.rotation.x / 90;
  if (xDirection < -1){
    let difference = xDirection + 1;
    xDirection = -1 - difference;
    isXAngleGreater90 = true;
  }
  else if (xDirection > 1){
    let difference = xDirection - 1;
    xDirection = 1 - difference;
    isXAngleGreater90 = true;
  }

  //calculate y (in interval [-1,1])
  let yDirection = camera.rotation.y / 90;
  if (yDirection < -1){
    let difference = yDirection + 1;
    yDirection = -1 - difference;
    isCameraAustralian = true;
  }
  else if (yDirection > 1){
    let difference = yDirection - 1;
    yDirection = 1 - difference;
    isCameraAustralian = true;
  }

  //calculate z (in interval [-1,1]; depends on x)
  let zDirection = xDirection < 0 ? 1 + xDirection : 1 - xDirection;
  if (isXAngleGreater90) zDirection *= -1;

  if (isCameraAustralian){  //reverse x&z directions if camera is standing on it's head
    xDirection *= -1;
    zDirection *= -1;
  }

  return normalizeVec3([xDirection, yDirection, zDirection]);
}

function setCameraTarget(camera){
  //immediately return if camera == null, otherwise assume a correct camera struct was passed
  if (typeof camera === 'undefined') return;

  const distance = 20; //how many units the target should be away from the camera
  let cameraDirection = getCameraDirection(camera);

  //calculate target
  let target = [0,0,0];
  for (let i in cameraDirection){
    target[i] = camera.position[i] + cameraDirection[i] * distance;
  }
  
  camera.target = target;
  displayText("New camera target: " + camera.target); //TODO: remove this line
}
