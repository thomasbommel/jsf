
/* For copying:
{
  ambient: ,
  diffuse: ,
  specular: ,
  emission: ,
  shininess:
}
 */


function getDefaultMaterial() {
  return {
    ambient: [0.4, 0.4, 0.4, 1.0],  //color of the shadow(=unlit) side
    diffuse: [0.7, 0.7, 0.7, 1.0],  //color of the lit side
    specular: [0, 0, 0, 0], //reflection color (usually r=g=b except for metals where specular ~= diffuse); higher values = more reflection ; [0,0,0,1] = no reflection
    emission: [0, 0, 0, 0], // color to emit
    shininess: 0.0, // lower values = more reflection ; 100 = weak reflection
    texture: null //replaces ambient and diffuse if present
  };
}

function getSkinMaterial() {
  return {
    ambient: [0.435,0.345,0.23,1],
    diffuse: [0.87,0.69,0.46,1],
    specular: [0.05,0.05,0.05,0],
    shininess: 70,
  }
}


function getWoodMaterial() {
  return {
    ambient: [0.13, 0.075,0,1],
    diffuse: [0.26,0.15,0,1],
    specular: [0.01,0.01,0.01,0],
    shininess: 100,
  };
}

function getIronMaterial() {
  return {
    ambient: [0.5,0.5,0.5,1],
    diffuse: [0.65,0.65,0.65,1],
    specular: [0.3,0.3,0.3,0],
    shininess: 20,
  }
}
