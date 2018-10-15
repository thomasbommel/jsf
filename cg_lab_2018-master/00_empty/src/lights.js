

class LightNode extends LightSGNode{
  constructor(position, uniform, lightModifiers, sphereModifiers, children) {
      super(position, children);

      this.uniform = uniform;
      this.ambient =  [0.5,0.5, 0.5, 1];
      this.diffuse =  [1, 1, 1, 1];
      this.specular = [1, 1, 1, 1];

      if(lightModifiers){
          if(lightModifiers.ambient) this.ambient = lightModifiers.ambient;
          if(lightModifiers.diffuse) this.diffuse = lightModifiers.diffuse;
          if(lightModifiers.specular)   this.specular = lightModifiers.specular;
          if(lightModifiers.position)  this.position = lightModifiers.position;
      }
      if(sphereModifiers){
        this.showSphere = sphereModifiers.showSphere || true;
        this.sphereRadius = sphereModifiers.sphereRadius || 2;

        if(this.showSphere){
          this.sphere = createLightSphere(this.sphereRadius,{ambient:[1,1,0,1],diffuse: [1,1,0,1]})
          this.append(this.sphere);
        }
      }

      this.moveTo = function(position){
         this.position = position;
      };
  }
  render(context) {
      super.render(context);
  }
}

function createLightSphere(radius, material) {
  let sphere = new MaterialSGNode(new RenderSGNode(makeSphere(radius,30,30)));
  applyMaterial(sphere, material);
  return sphere;
}

class SunNode extends LightNode{

  constructor(position,lightModifiers, sphereModifiers,children){
    super(position, lightModifiers, sphereModifiers, children);
    this.turnRadius = 600;
    this.centerPosition = [position[0],position[1],position[2]];
    this.dayTimeAngles = {noon: 90, evening: 130, morning: 45 };

    let wholeTurn = 360;
    let currentAmbient = this.ambient;
    let currentDiffuse = this.diffuse;

    this.moveToMorning = function() {
        this.moveToAngle(this.dayTimeAngles.morning);
        this.setMorningColor();
    }

    this.setMorningColor = function() {
      this.ambient = [0.3,0.1,0.1,1];
      this.diffuse = [0.3,0.2,0.5,1];
    }

    this.moveToNoon = function(timeDifference) {
      this.moveToAngle(this.dayTimeAngles.noon);
      this.setNoonColor();
    }

    this.setNoonColor = function() {
      this.ambient = [0.6,0.6,0.7,1];
      this.diffuse = [0.9,0.9,0.8,1];
    }

    this.moveToEvening = function(){
      this.moveToAngle(this.dayTimeAngles.evening);
      this.setEveningColor();
    }

    this.setEveningColor = function() {
      this.ambient = [0.3,0,0,1];
      this.diffuse = [0.5,0.1,0.1,1];
    }

    this.currentColors = null;
    this.timePassedColorChange = -1;
    this.animateColor = function(fromAmbient, toAmbient,fromDiffuse, toDiffuse, secondsNeeded, deltaTime){
      if(this.timePassedColorChange < 0){
        this.currentColors = {ambient:fromAmbient, diffuse:fromDiffuse}
        this.ambient = fromAmbient;
        this.diffuse = fromDiffuse;
        this.timePassedColorChange = deltaTime;
      }

      let ambientColorChange = this.calculateColorChangeAmount(fromAmbient, toAmbient, secondsNeeded, deltaTime);
      let diffuseColorChange = this.calculateColorChangeAmount(fromDiffuse, toDiffuse, secondsNeeded, deltaTime);

      let newAmbient = [0,0,0,0];
      let newDiffuse = [0,0,0,0];
      for(let i in ambientColorChange){
        newAmbient[i]=this.currentColors.ambient[i]+ambientColorChange[i];
      }
      for(let i in diffuseColorChange){
        newDiffuse[i]=this.currentColors.diffuse[i]+diffuseColorChange[i];
      }

      this.timePassedColorChange += deltaTime;
      if(this.timePassedColorChange > secondsNeeded){
        this.timePassedColorChange = -1;
      }

      this.currentColors.diffuse = newDiffuse;
      this.currentColors.ambient = newAmbient;

      this.ambient = this.currentColors.ambient;
      this.diffuse = this.currentColors.diffuse;
    }

    this.calculateColorChangeAmount = function(fromColor, toColor, secondsNeeded,deltaTime){
        let diff = getVec3VectorDistance(toColor, fromColor);

        for(let i in diff){
          diff[i] = toColor[i] - fromColor[i];
          diff[i] = diff[i] * deltaTime/secondsNeeded;
        }
        return diff;
    }

  this.calculateMoveDistance = function(from, to, secondsNeeded,deltaTime){
      let difference = to - from;
      return difference * deltaTime/secondsNeeded;
  }

  this.timePassed = -1;
  this.currentPos = -1;
  this.animate = function(from, to, secondsNeeded, deltaTime){
    if(this.timePassed < 0){
      this.currentPos = from;
      this.timePassed = deltaTime;
    }

    let moveDist = this.calculateMoveDistance(from, to, secondsNeeded, deltaTime);
    let newPos = this.currentPos + moveDist;

    this.timePassed += deltaTime;
    if(this.timePassed > secondsNeeded){
      this.timePassed = -1;
    }

    this.currentPos = newPos;
    return newPos;
  }

  this.moveToAngle = function(newPos){
    let angle = Math.radians(newPos/wholeTurn*360);
    let xChange =  Math.cos(angle);
    let yChange =  Math.sin(angle);

    this.position[0] = this.centerPosition[0] + xChange * this.turnRadius;
    this.position[1] = this.centerPosition[1] + yChange * this.turnRadius *0.4;
  }
}
}
