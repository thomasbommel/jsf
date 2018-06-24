

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
function getDefaultCameraRotation() { return { x: 0, y: 0}; }
function getDefaultCameraPosition() { return [0,20,-35]; }


function setCameraToDefaultValues() {
  camera.rotation = getDefaultCameraRotation();
  camera.position = getDefaultCameraPosition();
  camera.rotation = getDefaultCameraRotation();
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
     //factor to multiply with delta mouse movement

    if (mouse.leftButtonDown && !camera.isPerformingFlight) {
      setCameraTarget(delta, 0.25);

      updateStats();
      updatePannelFromCamera();
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
    else if (event.code == 'KeyF'){   //redo main camera flight
      performMainCameraFlight();
    }
    else if (event.code == 'KeyT'){   //toggle debugging panels
      toggleStats();
    }
    else if (event.code == 'ArrowUp' || event.code == 'KeyW'){
      let direction = normalizeVec3(getVec3VectorDistance(camera.position,camera.target));
      moveCamera(direction, 10);
    }
    else if (event.code == 'ArrowDown' || event.code == 'KeyS'){
      let direction = normalizeVec3(getVec3VectorDistance(camera.target,camera.position));
      moveCamera(direction, 10);
    }
    else if(event.code == 'Digit1'){
      sun.moveToMorning();
    }
    else if(event.code == 'Digit2'){
      sun.moveToNoon();
    }
    else if(event.code == 'Digit3'){
      sun.moveToEvening();
    }

    updateStats();
    updatePannelFromCamera();
  });
}

function moveCamera(direction, movespeed){
  for (let i in direction){
    camera.position[i] += direction[i] * movespeed;
    camera.target[i] += direction[i] * movespeed;
  }
}

function setCameraTarget(deltaRotation, mouseSensitivity){
  const distance = 30;  //distance the target is away from position
  camera.rotation.x += deltaRotation.x * mouseSensitivity;

  let x = Math.sin(camera.rotation.x*Math.PI/180);
  let z = Math.cos(camera.rotation.x*Math.PI/180);

  camera.target[0] = camera.position[0]+ x * distance;
  camera.target[2] = camera.position[2]+ z * distance;

  camera.rotation.y += deltaRotation.y * mouseSensitivity;
  let y = Math.sin(camera.rotation.y*Math.PI/180);
  camera.target[1] = camera.position[1]+ y * distance;
}
