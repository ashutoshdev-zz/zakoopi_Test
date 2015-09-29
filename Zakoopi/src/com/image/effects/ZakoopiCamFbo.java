package com.image.effects;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

/*
 * Helper class for handling frame buffer objects.
 */
public final class ZakoopiCamFbo {

	// FBO handle.
	private int mFrameBufferHandle = -1;
	// Generated texture handles.
	private int[] mTextureHandles = {};
	// FBO textures and depth buffer size.
	private int mWidth, mHeight;

	
	public void bind() {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBufferHandle);
		GLES20.glViewport(0, 0, mWidth, mHeight);
	}

	public void bindTexture(int index) {
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
				GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
				mTextureHandles[index], 0);
	}

	
	public int getHeight() {
		return mHeight;
	}

	public int getTexture(int index) {
		return mTextureHandles[index];
	}

	
	public int getWidth() {
		return mWidth;
	}

	
	public void init(int width, int height, int textureCount,
			boolean textureExternalOES) {

		// Just in case.
		reset();

		// Store FBO size.
		mWidth = width;
		mHeight = height;

		// Genereta FBO.
		int handle[] = { 0 };
		GLES20.glGenFramebuffers(1, handle, 0);
		mFrameBufferHandle = handle[0];
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBufferHandle);

		// Generate textures.
		mTextureHandles = new int[textureCount];
		GLES20.glGenTextures(textureCount, mTextureHandles, 0);
		int target = textureExternalOES ? GLES11Ext.GL_TEXTURE_EXTERNAL_OES
				: GLES20.GL_TEXTURE_2D;
		for (int texture : mTextureHandles) {
			GLES20.glBindTexture(target, texture);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_T,
					GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MIN_FILTER,
					GLES20.GL_NEAREST);
			GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MAG_FILTER,
					GLES20.GL_LINEAR);
			if (target == GLES20.GL_TEXTURE_2D) {
				GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
						mWidth, mHeight, 0, GLES20.GL_RGBA,
						GLES20.GL_UNSIGNED_BYTE, null);
			}
		}
	}

	
	public void reset() {
		int[] handle = { mFrameBufferHandle };
		GLES20.glDeleteFramebuffers(1, handle, 0);
		GLES20.glDeleteTextures(mTextureHandles.length, mTextureHandles, 0);
		mFrameBufferHandle = -1;
		mTextureHandles = new int[0];
		mWidth = mHeight = 0;
	}

}
