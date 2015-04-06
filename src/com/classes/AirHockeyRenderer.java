package com.classes;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.android.util.LoggerConfig;
import com.android.util.ShaderHelper;
import com.android.util.TextResourceReader;

public class AirHockeyRenderer implements Renderer{
	
	//because for now, our positions will only have x, y coordinates
	private static final int POSITION_COMPONENT_COUNT = 2;
	/*
	 * This constant is the equivalent of sizeof(float) that we used in c++
	 */
	private static final int BYTES_PER_FLOAT = 4;
	private final FloatBuffer vertexData;
	private final Context context;
	private int program;
	
	private static final String U_COLOR = "u_Color";
	private int uColorLocation;
	
	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;
	
	/**
	 * Constructor
	 */
	public AirHockeyRenderer(Context context){
		//the positions of the vertices of a square
		
		this.context = context;
		float [] tableVertices = {
			//Triangle 1
			-0.5f, -0.5f,
			0.5f, 0.5f,
			-0.5f, 0.5f,
			
			//Triangle 2
			-0.5f, -0.5f,
			0.5f, -0.5f,
			0.5f, 0.5f,
			
			//Middle Line
			-0.5f, 0f,
			0.5f, 0f,
			
			//Mallets
			0f, -0.25f,
			0f, 0.25f,
			
			//Lets try to draw the puck
			0.0f, 0.0f,
			
			//Lets draw a border for the board
			//Border Line 1
			-0.5f, -0.5f,
			0.5f, -0.5f,
			//Border Line 2
			-0.5f, -0.5f,
			-0.5f, 0.5f,
			//Border Line 3
			0.5f, 0.5f,
			-0.5f, 0.5f,
			//Border Line 4
			0.5f, 0.5f,
			0.5f, -0.5f
			
		};
		/*
		 * - AllocateDirect tells the garbage collector to not collect that data, and you pass the size
		 * - Of the data, in this case, the array
		 * - Order is so the order in which we created the vertices is not changed
		 * - The data that was created must be treated as floats, not bites
		 */
		vertexData = ByteBuffer
				.allocateDirect(tableVertices.length * BYTES_PER_FLOAT) 
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		
		// Get the data from the Dalvik machine, to the memory of the phone :)
		vertexData.put(tableVertices);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		String vertexShaderSource = TextResourceReader.
				readTextFileFromResource(context, R.raw.simple_vertex_shader);

		String fragmentShaderSource = TextResourceReader.
				readTextFileFromResource(context, R.raw.simple_fragment_shader);
		
		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		
		if(LoggerConfig.ON){
			ShaderHelper.validateProgram(program);
		}
		
		glUseProgram(program);
		
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		
		vertexData.position(0);
		glVertexAttribPointer (aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
		
		glEnableVertexAttribArray(aPositionLocation);
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
	glViewport(0, 0, width, height);
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		//Drawing comands for the table edges
		glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		//Drawing commands for the line in the middle
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_LINES, 6, 2);
		
		//Drawing the mallets as points, first mallet blue
		glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
		glDrawArrays(GL_POINTS, 8, 1);
		
		//draw the second mallet as red
		glUniform4f (uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_POINTS, 9, 1);
		
		//draw the black puck
		glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 0.0f);
		glDrawArrays(GL_POINTS, 10, 1);
		
		//Draw the border lines
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_LINES, 11, 8);
		
		
		
		
	}

}
