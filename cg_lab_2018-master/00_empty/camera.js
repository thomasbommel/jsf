
//camera struct
const camera = {
  //access with camera.rotation.x and camera.rotation.y
  rotation: getDefaultCameraRotation(),
  //access with camera.position[i] and camera.target[i] ;; i-values: 0=x, 1=y, 2=z
  position: getDefaultCameraPosition(),
  target: [0,0,0],   //calculated by utils.setCameraTarget() in init and when camera is moved
  isPerformingFlight: false,     //true during an animated camera flight
};
//camera default-values (as functions)
function getDefaultCameraRotation() { return { x: 0, y: 10 }; }
function getDefaultCameraPosition() { return [0,15,-25]; }


function setCameraToDefaultValues() {
  camera.rotation = getDefaultCameraRotation();
  camera.position = getDefaultCameraPosition();
  setCameraTarget();
  camera.isPerformingFlight = false;
}


/**
 * initialize camera control
 * camera control is automatically disabled while camera.isPerformingFlight == true
 */
function initCameraInteraction(canvas) {
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

      //keep rotation.x in interval ]-180,180]
      if (camera.rotation.x <= -180) camera.rotation.x += 360;
      else if (camera.rotation.x > 180) camera.rotation.x -= 360;

      //keep rotation.y in interval ]-180,180]
      if (camera.rotation.y <= -180) camera.rotation.y += 360;
      else if (camera.rotation.y > 180) camera.rotation.y -= 360;

      //TODO: OMAS?!?! folgendes machn ja oder nein? i glaub eig. nein aber wenn is ned mach is alles (noch) kaputt(er)/buggy(-ier)
      //setCameraTarget();
      updateStats();
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

    if (event.code === 'KeyR') {    //reset camera to defaults
      setCameraToDefaultValues();
    }
    else if (event.code == 'KeyX'){   //"x-tra" (additional) camera flight
      performAdditionalCameraFlight();
    }
    else if (event.code == 'KeyC'){   //redo main camera flight
      performMainCameraFlight();
    }
    else if (event.Code == 'KeyT'){   //toggle debugging panels
      //TODO
    }
    else if (event.code == 'ArrowUp' || event.code == 'KeyW'){
      let direction = getCameraDirection();
      for (let i in direction){
        camera.position[i] += direction[i];
      }
      setCameraTarget();
      displayText("up - new pos: " +  vectorToString(camera.position));

    }
    else if (event.code == 'ArrowDown' || event.code == 'KeyS'){
      let direction = getCameraDirection();
      for (let i in direction){
        camera.position[i] -= direction[i];
      }
      setCameraTarget();
      displayText("down - new pos: " +  vectorToString(camera.position));
    }
    updateStats();
  });
}




function setCameraTarget(){
  camera.target = [camera.position[0], 0, camera.position[2] + 20]; //TODO
  /*const distance = 20; //how many units the target should be away from the camera
  let cameraDirection = getCameraDirection();

  //calculate target
  let target = [0,0,0];
  for (let i in cameraDirection){
    target[i] = camera.position[i] + cameraDirection[i] * distance;
  }

  camera.target = target;
  displayText("New camera target: " + vectorToString(camera.target)); //TODO: remove this line*/
}




/**
 * Returns the camera direction as x,y,z coordinates calculated from its rotation.
 * The returned vector is a normalized unit vector.
 */
function getCameraDirection(){
  return [0,0,1]; //TODO
  /*let isCameraAustralian = false;   //bool to identify if camera is "standing on it's head"
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

  return normalizeVec3([xDirection, yDirection, zDirection]);*/
}
