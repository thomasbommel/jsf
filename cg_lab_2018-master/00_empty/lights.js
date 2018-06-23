

class LightNode extends LightSGNode{
  constructor(position, lightModifiers, sphereModifiers, children) {
      super(position, children);

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

    this.moveToMorning = function(){
        this.moveToAngle(this.dayTimeAngles.morning);
    }

    this.moveToNoon = function(timeDifference){
      this.moveToAngle(this.dayTimeAngles.noon);
    }

    this.moveToEvening = function(){
      this.moveToAngle(this.dayTimeAngles.evening);
    }

  this.changeColor = function(newAmbient, newDiffuse){
    let colorChangeSpeed = 0.001;

    for(let i in this.ambient){
      if(newAmbient[i]>this.ambient[i]){
       this.ambient[i]+=colorChangeSpeed;
      }
      else if(newAmbient[i]<this.ambient[i]){
       this.ambient[i]-=colorChangeSpeed;
      }
    }

    for(let i in this.diffuse){
      if(newDiffuse[i]>this.diffuse[i]){
       this.diffuse[i]+=colorChangeSpeed;
      }
      else if(newDiffuse[i]<this.diffuse[i]){
       this.diffuse[i]-=colorChangeSpeed;
      }
    }
  }

  this.calculateMoveDistance = function(from, to, secondsNeeded,deltaTime){
      let difference = to - from;
      return difference * deltaTime/secondsNeeded;
  }


  this.timePassed = -1;
  this.currentPos = -1;
  this.animate = function(from, to, secondsNeeded, deltaTime){
    console.log(this.timePassed);
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
    this.position[1] = this.centerPosition[1] + yChange * this.turnRadius *0.5;
  }
}
}
