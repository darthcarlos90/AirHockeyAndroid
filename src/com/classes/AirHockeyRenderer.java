package com.classes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import static android.opengl.GLES20.*;
import android.opengl.GLSurfaceView.Renderer;

public class AirHockeyRenderer implements Renderer{
	
	//because for now, our positions will only have x, y coordinates
	private static final int POSITION_COMPONENT_COUNT = 2;
	/*
	 * This constant is the equivalent of sizeof(float) that we used in c++
	 */
	private static final int BYTES_PER_FLOAT = 4;
	private final FloatBuffer vertexData;
	
	/**
	 * Constructor
	 */
	public AirHockeyRenderer(){
		//the positions of the vertices of a square
		float [] tableVertices = {
			//Triangle 1
			0f, 0f,
			9f, 14f,
			0f, 14f,
			
			//Triangle 2
			0f, 0f,
			9f, 14f,
			9f, 0f,
			
			//Middle Line
			0f, 7f,
			9f, 7f,
			
			//Mallets
			4.5f, 2f,
			4.5f, 12f
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
		glClearColor(0.0f, 1.0f, 1.0f, 0.0f);
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
	glViewport(0, 0, width, height);
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);
		
	}

}
