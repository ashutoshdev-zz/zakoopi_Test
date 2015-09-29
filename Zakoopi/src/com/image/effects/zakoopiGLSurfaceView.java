package com.image.effects;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.mycam.CameraFragment;
import com.mycam.GalleryImageAdapter;
import com.mycam.ImageEffects;
import com.zakoopi.helper.Variables;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint("NewApi")
public class zakoopiGLSurfaceView extends GLSurfaceView implements
		GLSurfaceView.Renderer {

	private Bitmap bitmapSrc;
	private int imageWidth;
	private int imageHeight;
	private int[] textures = new int[2];
	private EffectContext effectContext;
	private Effect effect;
	private TextureRenderer texRenderer = new TextureRenderer();
	private boolean initialized = false;
	int currentEffect = 0;
	GalleryImageAdapter galleryImageAdapter;
	private boolean saveFrame;

	public zakoopiGLSurfaceView(Activity context) {
		super(context);

		initView();
	}

	public zakoopiGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public void setEffectAdapter(GalleryImageAdapter galleryImageAdapter) {
		this.galleryImageAdapter = galleryImageAdapter;
	}

	public void setImageSource(Bitmap bitmap) {
		this.bitmapSrc = bitmap;
		this.requestRender();
	}

	private void initView() {
		setEGLContextClientVersion(2);
		setRenderer(this);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		float imageProportion = (float) width / (float) height;

		float screenProportion = (float) CameraFragment.displayWidth
				/ (float) CameraFragment.displayHeight;
		// android.view.ViewGroup.LayoutParams lp =
		// zakoopiGLSurfaceView.getLayoutParams();

		if (imageProportion > screenProportion) {
			imageWidth = CameraFragment.displayWidth;
			imageHeight = (int) ((float) CameraFragment.displayWidth / imageProportion);
		} else {
			imageWidth = (int) (imageProportion * (float) CameraFragment.displayHeight);
			imageHeight = CameraFragment.displayHeight;
		}

		this.setMeasuredDimension(width, height);
	}

	private void loadTextures() {
		// Generate textures
		GLES20.glGenTextures(2, textures, 0);

		// Load input bitmap
		// imageWidth = CameraFragment.displayWidth;
		// imageHeight = CameraFragment.displayHeight;
		texRenderer.updateTextureSize(imageWidth, imageHeight);
		// Upload to texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapSrc, 0);

		// Set texture parameters
		GLToolbox.initTexParams();
	}

	private void applyEffect() {
		if (effect != null) {

			effect.apply(textures[0], imageWidth, imageHeight, textures[1]);

		}
	}

	private void renderResult() {
		if (currentEffect != 0) {
			// if no effect is chosen, just render the original bitmap
			texRenderer.renderTexture(textures[1]);
		} else {
			saveFrame = true;
			// render the result of applyEffect()
			texRenderer.renderTexture(textures[0]);

		}
	}

	public void setCurrentEffectId(int currentId) {
		this.currentEffect = currentId;
	}

	@Override
	public void onResume() {
		super.onResume();
		initialized = false;
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		Log.i("jiangtao4", "onDrawFrame is : " + initialized);
		if (!initialized) {
			// Only need to do this once
			effectContext = EffectContext.createWithCurrentGlContext();
			texRenderer.init();
			loadTextures();
			initialized = true;
		}
		if (currentEffect != 0) {
			// if an effect is chosen initialize it and apply it to the texture
			effect = ZakoopiImageUtils.createEffect(currentEffect,
					effectContext);
			applyEffect();
		}
		renderResult();

		if (saveFrame) {
			Variables.imgeffects = takeScreenshot(arg0);
		}

	}

	public Bitmap takeScreenshot(GL10 mGL) {

		final int mWidth = imageWidth;
		final int mHeight = imageHeight;
		IntBuffer ib = IntBuffer.allocate(mWidth * mHeight);
		IntBuffer ibt = IntBuffer.allocate(mWidth * mHeight);
		mGL.glReadPixels(0, 0, mWidth, mHeight, GL10.GL_RGBA,
				GL10.GL_UNSIGNED_BYTE, ib);

		// Convert upside down mirror-reversed image to right-side up normal
		// image.
		for (int i = 0; i < mHeight; i++) {
			for (int j = 0; j < mWidth; j++) {
				ibt.put((mHeight - i - 1) * mWidth + j, ib.get(i * mWidth + j));
			}
		}
		try {
			Bitmap mBitmap = Bitmap.createBitmap(mWidth, mHeight,
					Bitmap.Config.ARGB_8888);
			mBitmap.copyPixelsFromBuffer(ibt);
			return mBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmapSrc;

	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		if (texRenderer != null) {
			texRenderer.updateViewSize(width, height);
			texRenderer.updateTextureSize(width, height);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

	}

}
