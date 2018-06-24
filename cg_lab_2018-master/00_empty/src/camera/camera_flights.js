


function performMainCameraFlight() {
  setCameraToDefaultValues();
  animateSun = true;

  //30 second camera flight
  let sunAnim = new CameraAnimation([
    {position: [307.05, 19.63, 230.14], rotation: {x: -160, y:-4}, duration: 2},
    {position: [335.49, 93.99, 55.83], rotation: {x:-104.25,y:-25.75}, duration: 6}, // farm house

    {position: [219.46, 45.61, -100.98], rotation: {x: -40, y:2}, duration: 2},
    {position: [-200.36, 100.67, -103.21], rotation: {x: 2, y:-5}, duration: 2},
    {position: [-301.27, 121.91, -53.17], rotation: {x: 37, y:-22}, duration: 2},// castle
    {position: [-61.57, 200.80, 109.66], rotation: {x: -64, y:-70}, duration: 5},// c. far away
    {position: [-146.90, 16.42, 15.06], rotation: {x: -10, y:1}, duration: 2},// c. nearby

    {position: [-38.63, 97.16, 167.50], rotation: {	x:+5,y:2.5}, duration: 2},// middle of map
    {position: [361, 89, 292], rotation: {	x:-30,y: -21}, duration: 3}, // dock
    {position: [361, 89, 292], rotation: {	x:-30,y: -21}, duration: 4}  //wait
  ]);
  sunAnim.startAnimation();

  animateFarmers();
  animateDancers();
  animateLake();
}


function animateFarmers() {
  animateHoe(farmer1.right_arm, 1.5);
  animateHoe(farmer2.left_arm, 1.2);
  animateHoe(farmer3.right_arm, 1);
  let farmerChildMatrix = mat4.clone(farmer4.root.matrix);
  let farmerChildAnim = new Animation(3, [
    { node: farmer4.root, targetMatrix: calculatePlacementMatrix({translation: [140,0,-44], yRotation: -60, scale: vec3FromFloat(0.45)}), duration: 2 },
    { node: farmer4.root, targetMatrix: calculatePlacementMatrix({translation: [171,0,-62], yRotation: 180, scale: vec3FromFloat(0.45)}), duration: 2 },
    { node: farmer4.root, targetMatrix: calculatePlacementMatrix({translation: [174,0,-35], yRotation: 50, scale: vec3FromFloat(0.45)}), duration: 2 }
  ]);
  farmerChildAnim.startAnimation();
}


function animateDancers() {
  let dancer5Anim = new Animation(5, [
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(40)), duration: 0.6},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(60)), duration: 0.5},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(90)), duration: 0.5},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(120)), duration: 0.5},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(150)), duration: 0.5},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(150)), duration: 0.5},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(180)), duration: 0.5},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(230)), duration: 0.8},
    { node: dancer5.root, targetMatrix: mat4.multiply(mat4.create(), dancer5.root.matrix, glm.rotateY(300)), duration: 0.8},
  ]);
  dancer5Anim.startAnimation();

  dance(dancer6, 2.8, 1.5);
  dance(dancer7, 2.8, 1.5);
  dance(dancer1, 2.4, 2);
  dance(dancer2, 5, 2.6);
  dance(dancer3, 1.9, 1.4);
  dance(dancer4, 3.47, 4.20);
}



function animateLake() {
  fish(fisher1.left_arm, 5);
  fish(fisher2.right_arm, 2);

  swim(fish1, 12, 2.4);
  swim(fish2, 14, 1.6);
  swim(fish3, 10, 2.0);
  swim(fish4, 20, 2.1);
  swim(fish5, 13, 0.9);
}



function animateHoe(farmer_arm, swingDuration) {
  let mat = farmer_arm.matrix;
  let upMat = mat4.multiply(mat4.create(), glm.translate(0,1.5,-3.5), glm.rotateX(50));
  let downMat = mat4.multiply(mat4.create(), glm.translate(0,0,2), glm.rotateX(-20));

  let hoeAnim = new Animation(10, [
    {node: farmer_arm, targetMatrix: mat4.multiply(mat4.create(), mat, upMat), duration: swingDuration},
    {node: farmer_arm, targetMatrix: mat4.multiply(mat4.create(), mat, downMat), duration: swingDuration}
  ]);
  hoeAnim.startAnimation();
}



function dance(dancer, range, duration){
  let mat = dancer.root.matrix;
  let forwardsMat = mat4.multiply(mat4.create(), mat, glm.translate(-range/3,0,-1*range));
  let backwardsMat = mat4.multiply(mat4.create(), mat, glm.translate(range/3,0,range));

  let danceAnim = new Animation(10, [
    { node: dancer.root, targetMatrix: forwardsMat, duration: duration||2},
    { node: dancer.root, targetMatrix: backwardsMat, duration: duration||2}
  ]);
  danceAnim.startAnimation();
}


function fish(fisher_arm, swingDuration, bobDuration){
  let mat = fisher_arm.matrix;
  let upMat = mat4.multiply(mat4.create(), glm.translate(0,4.5,-5.5), glm.rotateX(80));
  let downMat = mat4.multiply(mat4.create(), glm.translate(0,0.3,-1.1), glm.rotateX(20));
  downMat = mat4.multiply(mat4.create(), mat, downMat);

  let fishingAnim = new Animation(1, [
    {node: fisher_arm, targetMatrix: mat4.multiply(mat4.create(), mat, upMat), duration: swingDuration/2},
    {node: fisher_arm, targetMatrix: downMat, duration: swingDuration/2},
    {node: fisher_arm, targetMatrix: mat, duration: 5},
    {node: fisher_arm, targetMatrix: downMat, duration: 5},
    {node: fisher_arm, targetMatrix: mat, duration: 5},
    {node: fisher_arm, targetMatrix: downMat, duration: 5},
    {node: fisher_arm, targetMatrix: mat, duration: 5},
    {node: fisher_arm, targetMatrix: downMat, duration: 5},
  ]);
  fishingAnim.startAnimation();
}


function swim(fish, range, duration){
  let mat = fish.matrix;
  let pos1 = mat4.multiply(mat4.create(), mat, calculatePlacementMatrix({translation: [0,0,-range], yRotation: 50}));
  let pos2 = mat4.multiply(mat4.create(), pos1, calculatePlacementMatrix({translation: [-range,-range/3,0], yRotation: 90}));
  let pos3 = mat4.multiply(mat4.create(), pos2, calculatePlacementMatrix({translation: [0,0,range], yRotation: 90}));

  let swimmingAnim = new Animation(15, [
    {node: fish, targetMatrix: pos1, duration: duration},
    {node: fish, targetMatrix: pos2, duration: duration},
    {node: fish, targetMatrix: pos3, duration: duration},
    {node: fish, targetMatrix: mat, duration: duration}
  ]);
  swimmingAnim.startAnimation();
}
