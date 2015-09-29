package com.image.effects;

import java.io.IOException;
import java.util.List;

import com.mycam.CameraFragment;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.opengl.Matrix;
import android.util.Log;
import android.widget.Toast;

/**
 * Class for encapsulating Camera related functionality.
 */
public class ZakoopiCamCamera {

	// Current Camera instance.
	private Camera mCamera;
	// Current Camera Id.
	private int mCameraId;
	// Current Camera CameraInfo.
	private final Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
	// SharedData instance.
	private ZakoopiCamData mSharedData;
	// Surface texture instance.
	private SurfaceTexture mSurfaceTexture;
	private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
	private static final int PREVIEW_SIZE_MAX_WIDTH = 640;
	 int w,h;
	public int getOrientation() {
		if (mCameraInfo == null || mSharedData == null) {
			return 0;
		}
		if (mCameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
			return (mCameraInfo.orientation - mSharedData.mOrientationDevice + 360) % 360;
		} else {
			return (mCameraInfo.orientation + mSharedData.mOrientationDevice) % 360;
		}
	}

	public boolean isCameraFront() {
		return mCameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT;
	}

	/**
	 * Must be called from Activity.onPause(). Stops preview and releases Camera
	 * instance.
	 */
	public void onPause() {
		mSurfaceTexture = null;
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	
	/**
	 * Should be called from Activity.onResume(). Recreates Camera instance.
	 */
	public void onResume() {
		openCamera();
	}

	/**
	 * Handles camera opening.
	 */
	@SuppressWarnings("deprecation")
	private void openCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		if (mCameraId >= 0) {
			Camera.getCameraInfo(mCameraId, mCameraInfo);
			mCamera = Camera.open(mCameraId);
			System.out.println("id:==" + mCameraId);

			if (mCameraId > 0) {

				mCamera.setDisplayOrientation(180);
				
			}
			Camera.Parameters params = mCamera.getParameters();

			Size bestPreviewSize = determineBestPreviewSize(params);
			Size bestPictureSize = determineBestPictureSize(params);

			params.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
			
			params.setPictureSize(bestPictureSize.width,bestPictureSize.height);
		//	params.setPreviewSize(bestPictureSize.width, bestPictureSize.height);
			if (mCameraId > 0) {

				params.setRotation(270);

			} else {

				params.setRotation(90);

			}
			if(CameraFragment.focus_on==true){
				
			     params.setFlashMode(Parameters.FLASH_MODE_ON);
			}
			mCamera.setParameters(params);

			try {
				if (mSurfaceTexture != null) {
					mCamera.setPreviewTexture(mSurfaceTexture);
					mCamera.startPreview();
				}
			} catch (Exception ex) {
			}
		}

		updateRotation();
	}

	private Size determineBestPreviewSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPreviewSizes();

		return determineBestSize(sizes, PREVIEW_SIZE_MAX_WIDTH);
	}

	private Size determineBestPictureSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPictureSizes();

		return determineBestSize(sizes, PICTURE_SIZE_MAX_WIDTH);
	}

	protected Size determineBestSize(List<Size> sizes, int widthThreshold) {
		Size bestSize = null;

		for (Size currentSize : sizes) {
			
			boolean isDesiredRatio = (currentSize.width / 4) == (currentSize.height / 3);
			boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
			boolean isInBounds = currentSize.width <= PICTURE_SIZE_MAX_WIDTH;

			if (isDesiredRatio && isInBounds && isBetterSize) {
				bestSize = currentSize;
			}
		}

		if (bestSize == null) {
			//listener.onCameraError();

			return sizes.get(0);
		}

		return bestSize;
	}

	/**
	 * Selects either front-facing or back-facing camera.
	 */
	public void setCameraFront(boolean frontFacing) {
		int facing = frontFacing ? CameraInfo.CAMERA_FACING_FRONT
				: CameraInfo.CAMERA_FACING_BACK;

		mCameraId = -1;
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; ++i) {
			Camera.getCameraInfo(i, mCameraInfo);
			if (mCameraInfo.facing == facing) {
				mCameraId = i;

				break;
			}
		}

		openCamera();
	}

	/**
	 * Simply forwards call to Camera.setPreviewTexture.
	 */
	@SuppressWarnings("deprecation")
	public void setPreviewTexture(SurfaceTexture surfaceTexture)
			throws IOException {
		mSurfaceTexture = surfaceTexture;
		mCamera.setPreviewTexture(surfaceTexture);

	}

	/**
	 * Setter for storing shared data.
	 */
	public void setSharedData(ZakoopiCamData sharedData) {
		mSharedData = sharedData;
	}

	/**
	 * Simply forwards call to Camera.startPreview.
	 */
	public void startPreview() {
		mCamera.startPreview();
	}

	/**
	 * Simply forwards call to Camera.stopPreview.
	 */
	public void stopPreview() {
		mCamera.stopPreview();
	}

	/**
	 * Handles picture taking callbacks etc etc.
	 */
	public void takePicture(Observer observer) {
		try {
			mCamera.autoFocus(new CameraObserver(observer));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * Updated rotation matrix, aspect ratio etc.
	 */
	public void updateRotation() {
		if (mCamera == null || mSharedData == null) {
			return;
		}

		int orientation = mCameraInfo.orientation;
		Matrix.setRotateM(mSharedData.mOrientationM, 0, orientation, 0f, 0f, 1f);

		Camera.Size size = mCamera.getParameters().getPreviewSize();
		if (orientation % 90 == 0) {
			int w = size.width;
			size.width = size.height;
			size.height = w;
		}

		mSharedData.mAspectRatioPreview[0] = (float) Math.min(size.width,
				size.height) / size.width;
		mSharedData.mAspectRatioPreview[1] = (float) Math.min(size.width,
				size.height) / size.height;
	}

	/**
	 * Class for implementing Camera related callbacks.
	 */
	private final class CameraObserver implements Camera.ShutterCallback,
			Camera.AutoFocusCallback, Camera.PictureCallback {

		private Observer mObserver;

		private CameraObserver(Observer observer) {
			mObserver = observer;
		}

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			mObserver.onAutoFocus(success);
			mCamera.takePicture(this, null, this);
		}

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			mObserver.onPictureTaken(data);
		}

		@Override
		public void onShutter() {
			mObserver.onShutter();
		}

	}

	/**
	 * Interface for observing picture taking process.
	 */
	public interface Observer {

		/**
		 * Called once auto focus is done.
		 */
		public void onAutoFocus(boolean success);

		/**
		 * Called once picture has been taken.
		 */
		public void onPictureTaken(byte[] jpeg);

		/**
		 * Called to notify about shutter event.
		 */
		public void onShutter();
	}

	// for image full size

}
