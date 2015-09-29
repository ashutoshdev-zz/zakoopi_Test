package com.mycam;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.image.effects.ZakoopiCamCamera;
import com.image.effects.ZakoopiCamData;
import com.image.effects.ZakoopiCamRenderer;
import com.zakoopi.R;
import com.zakoopi.helper.Variables;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class CameraFragment extends Fragment {

	ImageView cam_rotate;
	ImageView cam_focus;
	ImageView cam_capture;

	private final ZakoopiCamCamera mCamera = new ZakoopiCamCamera();;
	private final ButtonObserver mObserverButton = new ButtonObserver();
	private final CameraObserver mObserverCamera = new CameraObserver();
	private OrientationObserver mObserverOrientation;
	private final RendererObserver mObserverRenderer = new RendererObserver();
	private SharedPreferences mPreferences;
	private ZakoopiCamRenderer mRenderer;
	private final ZakoopiCamData mSharedData = new ZakoopiCamData();
	boolean camera_check = false;
	Bitmap bitmap = null;
	Matrix matrix;
	public static int displayWidth;
	public static boolean focus_on = false;
	public static int displayHeight;
	TextView txt_upload_image;
	RelativeLayout back;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,typeface_regular;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// We prevent screen orientation changes.
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View cam = inflater.inflate(R.layout.zakoopicam, null);

		// Force full screen view.
		
		/**
		* Google Analystics
		*/

		Tracker t = ((UILApplication) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Article View");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
		
		mObserverOrientation = new OrientationObserver(getActivity());
		// Instantiate camera handler.
		mCamera.setCameraFront(false);
		mCamera.setSharedData(mSharedData);

		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		displayWidth = size.x;
		displayHeight = size.y;

		// Find renderer view and instantiate it.
		mRenderer = (ZakoopiCamRenderer) cam
				.findViewById(R.id.instacam_renderer);
		mRenderer.setSharedData(mSharedData);
		mRenderer.setObserver(mObserverRenderer);

		cam_rotate = (ImageView) cam.findViewById(R.id.button_rotate);
		cam_focus = (ImageView) cam.findViewById(R.id.imageView2);
		cam_capture = (ImageView) cam.findViewById(R.id.imageView3);
		txt_upload_image = (TextView) cam.findViewById(R.id.txt_upload_image);
		back = (RelativeLayout) cam.findViewById(R.id.back);
txt_upload_image.setTypeface(typeface_bold);
		cam_capture.setOnClickListener(mObserverButton);
		cam_rotate.setOnClickListener(mObserverButton);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		// cam_focus.setOnClickListener(mObserverButton);
		cam_focus.setImageResource(R.drawable.upload_flash_off);
		mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

		mSharedData.mFilter = mPreferences.getInt(
				getString(R.string.key_filter), 0);

		cam_focus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!mCamera.isCameraFront()) {

					focus_on = true;
					cam_focus.setImageResource(R.drawable.upload_flash_on);
					mCamera.onResume();

				} else {

					focus_on = false;
					cam_focus.setImageResource(R.drawable.upload_flash_off);

					mCamera.onResume();
				}
			}
		});

		return cam;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
	}


	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		mCamera.onPause();
		mRenderer.onPause();
		mObserverOrientation.disable();
	}

	@Override
	public void onResume() {
		super.onResume();
		mCamera.onResume();
		mRenderer.onResume();

		if (mObserverOrientation.canDetectOrientation()) {
			mObserverOrientation.enable();
		}

		setCameraFront(mCamera.isCameraFront());
	}

	private final void setCameraFront(final boolean front) {
		View button = cam_rotate;
       
		PropertyValuesHolder holderRotation = PropertyValuesHolder.ofFloat(
				"rotation", button.getRotation(), 360);
		ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(button,
				holderRotation).setDuration(250);
		anim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
		
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				cam_rotate.setRotation(0);
				mCamera.setCameraFront(front);
			  if(front==true){
				camera_check = true;
			  }
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		});
		anim.start();
	}

	private final class ButtonObserver implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.imageView3:

				mObserverOrientation.disable();
				mCamera.takePicture(mObserverCamera);
				focus_on = false;
				cam_focus.setImageResource(R.drawable.upload_flash_off);
				break;

			case R.id.button_rotate:
				setCameraFront(!mCamera.isCameraFront());
				camera_check = false;
				focus_on = false;
				cam_focus.setImageResource(R.drawable.upload_flash_off);
				break;

			}
		}

	}

	/**
	 * Class for implementing Camera related callbacks.
	 */
	private final class CameraObserver implements ZakoopiCamCamera.Observer {
		@Override
		public void onAutoFocus(boolean success) {
			// If auto focus failed show brief notification about it.
			if (!success) {
			}
		}

		@Override
		public void onPictureTaken(byte[] data) {
			// Once picture is taken just store its data.
           try{
			  BitmapFactory.Options options = new BitmapFactory.Options();
			  Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length, options);

			if (camera_check == true) {
				matrix = new Matrix();
				matrix.preScale(-1.0f, 1.0f);
				bitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
						mBitmap.getHeight(), matrix, true);

			} else {

				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
						options);

			}
           }catch(Exception e){
        	   e.printStackTrace();
           }
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			mSharedData.mImageData = byteArray;

			FileOutputStream outStream = null;
			File filePath = Environment.getExternalStorageDirectory();
			File dir = new File(filePath.getAbsolutePath() + "/Zakoopi/Camera");
			dir.mkdirs();

			File outFile = new File(dir, "Pic" + System.currentTimeMillis()
					+ ".jpg");

			try {
				outStream = new FileOutputStream(outFile);
				outStream.write(byteArray);
				outStream.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Variables.imgbitmap=bitmap;
			Intent in = new Intent(getActivity(), ImageEffects.class);
			startActivity(in);
			//getActivity().finish();

			// And time it was taken.
			Calendar calendar = Calendar.getInstance();
			mSharedData.mImageTime = calendar.getTimeInMillis();
		}

		@Override
		public void onShutter() {

		}

	}

	/**
	 * Class for observing device orientation.
	 */
	private class OrientationObserver extends OrientationEventListener {

		public OrientationObserver(Context context) {
			super(context, SensorManager.SENSOR_DELAY_NORMAL);
			disable();
		}

		@Override
		public void onOrientationChanged(int orientation) {
			orientation = (((orientation + 45) / 90) * 90) % 360;
			if (orientation != mSharedData.mOrientationDevice) {

				// Prevent 270 degree turns.
				int original = mSharedData.mOrientationDevice;
				if (Math.abs(orientation - original) > 180) {
					if (orientation > original) {
						original += 360;
					} else {
						original -= 360;
					}
				}

				// Trigger rotation animation.
				View shoot = cam_capture;
				PropertyValuesHolder holderRotation = PropertyValuesHolder
						.ofFloat("rotation", -original, -orientation);
				ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(
						shoot, holderRotation).setDuration(10);
				anim.start();

				// Store and calculate new orientation values.
				mSharedData.mOrientationDevice = orientation;
				mCamera.updateRotation();
			}
		}

	}

	/**
	 * Class for implementing InstaCamRenderer related callbacks.
	 */
	private class RendererObserver implements ZakoopiCamRenderer.Observer {
		@Override
		public void onSurfaceTextureCreated(SurfaceTexture surfaceTexture) {
			// Once we have SurfaceTexture try setting it to Camera.
			try {
				mCamera.stopPreview();
				mCamera.setPreviewTexture(surfaceTexture);

			} catch (final Exception ex) {

			/*	Toast.makeText(getActivity(), ex.getMessage(),
						Toast.LENGTH_LONG).show();*/

			}
		}
	}
}
