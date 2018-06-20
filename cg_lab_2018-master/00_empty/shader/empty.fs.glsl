/**
 * fragment shader
 */
//need to specify how "precise" float should be
precision mediump float;

/**
 * definition of a material structure containing common properties
 */
struct Material {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec4 emission;
	float shininess;
};

//illumination related variables
uniform Material u_material;

//entry point again
void main() {
  gl_FragColor = u_material.diffuse;
}
