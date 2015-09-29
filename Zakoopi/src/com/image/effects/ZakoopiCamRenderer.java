package com.image.effects;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.zakoopi.R;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.Toast;

/**
 * Renderer class which handles also SurfaceTexture related tasks.
 */
public class ZakoopiCamRenderer extends GLSurfaceView implements
		GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

	// View aspect ratio.
	private final float mAspectRatio[] = new float[2];
	// External OES texture holder, camera preview that is.
	private final ZakoopiCamFbo mFboExternal = new ZakoopiCamFbo();
	// Offscreen texture holder for storing camera preview.
	private final ZakoopiCamFbo mFboOffscreen = new ZakoopiCamFbo();
	// Full view quad vertices.
	private ByteBuffer mFullQuadVertices;
	// Renderer observer.
	private Observer mObserver;
	private static final double ASPECT_RATIO = 3.0 / 4.0;
	// Shader for copying preview texture into offscreen one.
	private final ZakoopiCamShader mShaderCopyOes = new ZakoopiCamShader();

	// Shared data instance.
	private ZakoopiCamData mSharedData;
	// One and only SurfaceTexture instance.
	private SurfaceTexture mSurfaceTexture;
	// Flag for indicating SurfaceTexture has been updated.
	private boolean mSurfaceTextureUpdate;
	// SurfaceTexture transform matrix.
	private final float[] mTransformM = new float[16];
	// View width and height.
	private int mWidth, mHeight;

	/**
	 * From GLSurfaceView.
	 */
	public ZakoopiCamRenderer(Context context) {
		super(context);
		init();
	}

	/**
	 * From GLSurfaceView.
	 */
	public ZakoopiCamRenderer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Initializes local variables for rendering.
	 */
	private void init() {
		// Create full scene quad buffer.
		final byte FULL_QUAD_COORDS[] = { -1, 1, -1, -1, 1, 1, 1, -1 };
		mFullQuadVertices = ByteBuffer.allocateDirect(4 * 2);
		mFullQuadVertices.put(FULL_QUAD_COORDS).position(0);

		setPreserveEGLContextOnPause(true);
		setEGLContextClientVersion(2);
		setRenderer(this);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	/**
	 * Loads String from raw resources with given id.
	 */
	private String loadRawString(int rawId) throws Exception {
		InputStream is = getContext().getResources().openRawResource(rawId);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return baos.toString();
	}

	@Override
	public synchronized void onDrawFrame(GL10 unused) {

		// Clear view.
		GLES20.glClearColor(.5f, .5f, .5f, 1f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// If we have new preview texture.
		if (mSurfaceTextureUpdate) {
			// Update surface texture.
			mSurfaceTexture.updateTexImage();
			// Update texture transform matrix.
			mSurfaceTexture.getTransformMatrix(mTransformM);
			mSurfaceTextureUpdate = false;

			// Bind offscreen texture into use.
			mFboOffscreen.bind();
			mFboOffscreen.bindTexture(0);

			// Take copy shader into use.
			mShaderCopyOes.useProgram();

			// Uniform variables.
			int uOrientationM = mShaderCopyOes.getHandle("uOrientationM");
			int uTransformM = mShaderCopyOes.getHandle("uTransformM");

			// We're about to transform external texture here already.
			GLES20.glUniformMatrix4fv(uOrientationM, 1, false,
					mSharedData.mOrientationM, 0);
			GLES20.glUniformMatrix4fv(uTransformM, 1, false, mTransformM, 0);

			// We're using external OES texture as source.
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
					mFboExternal.getTexture(0));

			// Trigger actual rendering.
			renderQuad(mShaderCopyOes.getHandle("aPosition"));
		}

		// Bind screen buffer into use.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, mWidth, mHeight);


		// Use offscreen texture as source.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFboOffscreen.getTexture(0));

		// Trigger actual rendering.
		renderQuad(mShaderCopyOes.getHandle("aPosition"));
	}

	@Override
	public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
		// Simply mark a flag for indicating new frame is available.
		mSurfaceTextureUpdate = true;
		requestRender();
	}

	@Override
	public synchronized void onSurfaceChanged(GL10 unused, int width, int height) {

		// Store width and height.
		mWidth = MeasureSpec.getSize(width);
		mHeight = MeasureSpec.getSize(height);
		if (width > height * ASPECT_RATIO) {
	        width = (int) (height * ASPECT_RATIO + .5);
	   } else {
	       height = (int) (width / ASPECT_RATIO + .5);
	   }
	    
		// Calculate view aspect ratio.
		mAspectRatio[0] = (float) Math.min(mWidth, mHeight) / mWidth;
		mAspectRatio[1] = (float) Math.min(mWidth, mHeight) / mHeight;

		// Initialize textures.
		if (mFboExternal.getWidth() != mWidth
				|| mFboExternal.getHeight() != mHeight) {
			mFboExternal.init(mWidth, mHeight, 1, true);
		}
		if (mFboOffscreen.getWidth() != mWidth
				|| mFboOffscreen.getHeight() != mHeight) {
			mFboOffscreen.init(mWidth, mHeight, 1, false);
		}

		// Allocate new SurfaceTexture.
		SurfaceTexture oldSurfaceTexture = mSurfaceTexture;
		mSurfaceTexture = new SurfaceTexture(mFboExternal.getTexture(0));
		mSurfaceTexture.setOnFrameAvailableListener(this);
		if (mObserver != null) {
			mObserver.onSurfaceTextureCreated(mSurfaceTexture);
		}
		if (oldSurfaceTexture != null) {
			oldSurfaceTexture.release();
		}

		requestRender();
	}

	@Override
	public synchronized void onSurfaceCreated(GL10 unused, EGLConfig config) {

		try {
			String vertexSource = loadRawString(R.raw.copy_oes_vs);
			String fragmentSource = loadRawString(R.raw.copy_oes_fs);
			mShaderCopyOes.setProgram(vertexSource, fragmentSource);
		} catch (Exception ex) {
			showError(ex.getMessage());
		}



		mFboExternal.reset();
		mFboOffscreen.reset();
	}

	/**
	 * Renders fill screen quad using given GLES id/name.
	 */
	private void renderQuad(int aPosition) {
		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mFullQuadVertices);
		GLES20.glEnableVertexAttribArray(aPosition);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	/**
	 * Setter for observer.
	 */
	public void setObserver(Observer observer) {
		mObserver = observer;
	}

	/**
	 * Setter for shared data.
	 */
	public void setSharedData(ZakoopiCamData sharedData) {
		mSharedData = sharedData;
		requestRender();
	}

	/**
	 * Shows Toast on screen with given message.
	 */
	private void showError(final String errorMsg) {
		Handler handler = new Handler(getContext().getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	/**
	 * Observer class for renderer.
	 */
	public interface Observer {
		public void onSurfaceTextureCreated(SurfaceTexture surfaceTexture);
	}

}
