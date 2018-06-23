
/* For copying:
{
  ambient: ,
  diffuse: ,
  specular: ,
  emission: ,
  shininess: ,
  texture: null
}
 */


function getDefaultMaterial() {
  return {
    ambient: [0.2, 0.2, 0.2, 1.0],  //color of the shadow(=unlit) side
    diffuse: [0.6, 0.6, 0.6, 1.0],  //color of the lit side
    specular: [0, 0, 0, 1], //reflection color (usually r=g=b except for metals where specular ~= diffuse); higher values = more reflection ; [0,0,0,1] = no reflection
    emission: [0, 0, 0, 0], // color to emit
    shininess: 0.0, // lower values = more reflection ; 100 = no reflection
    texture: null //replaces ambient and diffuse if present
  };
}


function getWoodMaterial() {
  return {
    ambient: [0.13, 0.075,0,1],
    diffuse: [0.26,0.15,0,1],
    specular: [0.1,0.1,0.1,1],
    shininess: 50,
    texture: null
  };
}
