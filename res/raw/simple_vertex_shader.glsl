uniform mat4 u_Matrix;

//Attribute types are unique for each vertex
attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color; // VArying makes a gradient kind of effect with the inputs it receives

void main()
{
	v_Color = a_Color;
	
	gl_Position = u_Matrix * a_Position;
	gl_PointSize = 10.0;
}
