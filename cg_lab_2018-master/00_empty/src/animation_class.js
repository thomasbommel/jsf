class Animation {

  //add one or multiple animations, other children can only be appended (not in constructor!)
  constructor(cycles, animationStructs){
    this.anims = (typeof animationStructs !== 'undefined') ? [].concat(animationStructs) : [];
    this.anims.forEach( function(anim) {
      anim.defaultMatrix = anim.node.matrix;
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
    this.to = calculatePlacementMatrix(this.anims[index].targetTransform);
  }

  startAnimation(){
    animations.push(this);
    this.currentMatrix = this.anims[0].defaultMatrix;
    this.switchAnimation(0);
  }

  stopAnimation(){
    var i = animations.indexOf(this);
    if (i >= 0) {
      animations.splice(i, 1);
    }
    return i >= 0;
  }

  restoreDefaultTransformations() {
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
