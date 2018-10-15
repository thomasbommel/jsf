class Animation {

  //add one or multiple animations, other children can only be appended (not in constructor!)
  constructor(cycles, animationStructs){
    this.anims = (typeof animationStructs !== 'undefined') ? [].concat(animationStructs) : [];
    this.anims.forEach( function(anim) {
      if (!anim.defaultMatrix) anim.defaultMatrix = anim.node.matrix;
    });
    this.cycles = cycles;   //the amount of cycles this animation should perform before stopping
  }

  switchAnimation(index){
    if (index === this.anims.length) {
      index = 0;
      this.cycles--;
      if (this.cycles === 0) {
        this.stopAnimation();
      }
    }
    this.timePassed = -1;
    this.animIndex = index;
    this.from = this.currentMatrix;
    this.to = this.anims[index].targetMatrix;
  }

  startAnimation(){
    this.currentlyRunning = true;
    animations.push(this);
    this.currentMatrix = this.anims[0].defaultMatrix;
    this.switchAnimation(0);
  }

  stopAnimation(){
    this.currentlyRunning = false;
    var i = animations.indexOf(this);
    if (i >= 0) {
      animations.splice(i, 1);
    }
    this.restoreDefaults();
    return i >= 0;
  }

  restoreDefaults() {
    this.anims.forEach( function(anim) {
      let difference = mat4.subtract(mat4.create(), anim.defaultMatrix, anim.node.matrix);
      anim.node.matrix = mat4.add(mat4.create(), anim.node.matrix, difference);
    });
  }

  calculateDeltaMatrix(deltaTime, duration) {
    let difference = mat4.subtract(mat4.create(), this.to, this.from);
    return mat4.multiplyScalar(mat4.create(), difference, deltaTime/duration);
  }

  transform(node, deltaMatrix){
    node.matrix = mat4.add(mat4.create(), node.matrix, deltaMatrix);
    return node.matrix;
  }

  animate(deltaTime) {
    if (this.timePassed < 0){
      this.timePassed = 0;
    }

    let curAnim = this.anims[this.animIndex];
    this.currentMatrix = this.transform(curAnim.node, this.calculateDeltaMatrix(deltaTime, curAnim.duration));

    this.timePassed += deltaTime;
    if (this.timePassed >= curAnim.duration) {
      this.switchAnimation(this.animIndex + 1);
    }
  }

}


class CameraAnimation {

  //only position and rotation matter
  constructor (cameraAnimStructs) {
    this.anims = (typeof cameraAnimStructs !== 'undefined') ? [].concat(cameraAnimStructs) : [];
  }

  switchAnimation(index){
    if (index === this.anims.length) {
      this.stopAnimation();
    }
    else if (index === 3) {
      sun.setNoonColor();
    }
    else if (index === 8){
      sun.setEveningColor();
    }

    this.timePassed = -1;
    this.animIndex = index;
    this.from = {
      position: vec3.clone(camera.position),
      rotation: {x: camera.rotation.x, y: camera.rotation.y}
    };
    this.to = this.anims[index] || this.anims[this.anims.length - 1];
  }

  startAnimation(){
    camera.isPerformingFlight = true;
    sun.setMorningColor();
    animations.push(this);
    this.switchAnimation(0);
  }

  stopAnimation(){
    var i = animations.indexOf(this);
    if (i >= 0) {
      animations.splice(i, 1);
    }

    //clean up camera
    camera.isPerformingFlight = false;
    setCameraToDefaultValues();
    //stop other animations
    animateSun = false;
    sun.moveToNoon();
    animations.forEach( function(anim) {
      anim.stopAnimation();
    });

    return i >= 0;
  }

  calculateDeltaStruct(deltaTime, duration) {
    let posDiff = vec3.subtract(vec3.create(), this.to.position, this.from.position);
    let rotationDiff = {
      x: this.to.rotation.x - this.from.rotation.x,
      y: this.to.rotation.y - this.from.rotation.y
    };
    let deltaSeconds = (deltaTime/duration);

    return {
      position: vec3.multiply(vec3.create(), posDiff, vec3FromFloat(deltaSeconds)),
      rotation: {x: rotationDiff.x * deltaSeconds, y: rotationDiff.y * deltaSeconds}
    };
  }

  transform(deltaStruct){
    moveCamera(deltaStruct.position, 1);
    setCameraTarget(deltaStruct.rotation, 1);
  }

  animate(deltaTime) {
    if (this.timePassed < 0){
      this.timePassed = 0;
    }

    let curAnim = this.anims[this.animIndex];
    this.transform(this.calculateDeltaStruct(deltaTime, curAnim.duration));

    this.timePassed += deltaTime;
    if (this.timePassed >= curAnim.duration) {
      this.switchAnimation(this.animIndex + 1);
    }
    updateStats();
  }

}
