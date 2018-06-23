
//vertex shader inputs
attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;

uniform mat4 u_modelView;
uniform mat3 u_normalMatrix;
uniform mat4 u_projection;

//light position uniforms
uniform vec3 u_lightPos;
uniform vec3 u_light2Pos;

//vertex shader outputs
varying vec3 v_normalVec;
varying vec3 v_eyeVec;

varying vec3 v_lightVec;
varying vec3 v_light2Vec;

varying vec2 v_texCoord;
varying vec4 v_heightColorDifference;

uniform sampler2D u_tex;	//texture unit to use
uniform bool u_enableHeightmap;
const float maximumHeight = 150.0;

void main() {
	vec4 heightValue = vec4(0,0,0,0);

	if(u_enableHeightmap){
		float heightRatio = (1.0-texture2D(u_tex,a_texCoord).y);// 0 = ground, 1 = highest
		heightValue.y = heightRatio * maximumHeight;



		v_heightColorDifference = vec4(-heightRatio*0.25,0.25*heightRatio,-heightRatio*0.05,0);
 	}


	vec4 eyePosition = u_modelView * (vec4(a_position,1) + heightValue);

  v_normalVec = u_normalMatrix * a_normal;
  v_eyeVec = -eyePosition.xyz;

	//calculate light vectors
	v_lightVec = u_lightPos - eyePosition.xyz;
	v_light2Vec = u_light2Pos - eyePosition.xyz;

	v_texCoord = a_texCoord;
	gl_Position = u_projection * eyePosition;
}
