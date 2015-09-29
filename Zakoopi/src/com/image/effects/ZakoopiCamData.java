package com.image.effects;

import android.app.ProgressDialog;

/**
 * Holder class for application wide data.
 */
public class ZakoopiCamData {
	// Preview view aspect ration.
	public final float mAspectRatioPreview[] = new float[2];
	// Filter values.
	public float mBrightness, mContrast, mSaturation, mCornerRadius;
	// Predefined filter.
	public int mFilter;
	// Taken picture data (jpeg).
	public byte[] mImageData;
	// Progress dialog while saving picture.
	public ProgressDialog mImageProgress;
	// Picture capture time.
	public long mImageTime;
	// Device orientation degree.
	public int mOrientationDevice;
	// Camera orientation matrix.
	public final float mOrientationM[] = new float[16];
}
