/**
 * This class contains several utility functions to be used in main.js
 */


function diffuseVecFromRGB(r,g,b){
  return [r/255,g/255,b/255,1];
}

function ambientVecFromRGB(r,g,b){
  return [0.6*r/255,0.6*g/255,0.6*b/255,1];
}

function getScaleVec(factor){
  return [factor,factor,factor];
}

/**
 * Careful! Assumes material to be not null/undefined.
 */
function applyMaterial(materialNode, material) {
  let defaultMaterial = getDefaultMaterial();

  materialNode.ambient    = material.ambient    || defaultMaterial.ambient;
  materialNode.diffuse    = material.diffuse    || defaultMaterial.diffuse;
  materialNode.specular   = material.specular   || defaultMaterial.specular;
  materialNode.emission   = material.emission   || defaultMaterial.emission;
  materialNode.shininess  = material.shininess  || defaultMaterial.shininess;
  materialNode.texture    = material.texture    || defaultMaterial.texture;
}


//wraps a node with a TransformationSGNode and immediately applies the given transformation to it
function wrapWithTransformationSGNode(node, transformation){
  let placement = mat4.create();
  if (transformation){
    let translation = transformation.translation || [0,0,0];
    let yRotation = transformation.yRotation || 0;
    let scale = transformation.scale || [1,1,1];

    mat4.multiply(/*out =*/placement,
      glm.translate(translation[0], translation[1], translation[2]),
      glm.rotateY(yRotation)
    );
    mat4.multiply(/*out =*/placement, placement,
      glm.scale(scale[0], scale[1], scale[2])
    );
  }
  if (node)
    return new TransformationSGNode(placement, node);
  else
    return new TransformationSGNode(placement);
}


/**
 * If material and material.texture are present then it wraps the specified node with a TextureSGNode and returns that.
 * Otherwise the specified node is simply returned.
 */
function wrapWithTextureSGNode(node, material){
  if (material && material.texture){
    node = new TextureSGNode(material.texture, false, node);
  }
  return node;
}


 /**
  * this method displays a Vector using the displayText method from the framework
  * @param  {[type]} vector [the vector itself]
  * @param  {[type]} name [>>optional<<, prefix for the vector]
  */
 function vectorToString(vector, name){
   var str = "{";
   for(let i in vector){
     str+= vector[i].toFixed(2)+", ";
   }
   str+="}";
   str = str.replace(", }","}");
   return str;
 }

/**
 * gets the euclidean length of a vector with an arbitrary amount of elements
 */
function getVectorLength(vector){
  let length = 0;
  for (let i in vector){
    length += Math.pow(vector[i], 2);
  }
  return Math.sqrt(length);
}

/**
 * normalizes a vec3 (Float32 array with 3 elements) to a unit vector (length==1)
 */
function normalizeVec3(vec3){
  let length = getVectorLength(vec3);
  if (length != 0){
    for (let i in vec3){
      vec3[i] /= length;
    }
    return vec3;
  }
  else
    return [0,0,1]; //forward direction when rotations x=y=0
}

/** returns the distance from A to B (= B - A) */
function getVec3VectorDistance(vecA, vecB){
  return [vecB[0] - vecA[0], vecB[1] - vecA[1], vecB[2] - vecA[2]];
}

/** returns the euclidean distance between two Points, rounded to 2 decimal places */
function getVec3EuclideanDistance(vecA, vecB){
  return getVectorLength(getVec3VectorDistance(vecA, vecB));
}
