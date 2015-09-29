package com.mycam;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNormalFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
//import android.widget.Toast;

import com.cam.imagedatabase.DBHelper;
import com.image.effects.HorizontalListView;
import com.zakoopi.R;
import com.zakoopi.activity.ProfileDrafts;
import com.zakoopi.helper.Variables;

public class ImageEffects extends FragmentActivity implements OnPictureSavedListener {

	RelativeLayout rel_back;
	GPUImageView img;
	HorizontalListView listview;
	ArrayList<String> list_item = new ArrayList<String>();
	public static Bitmap result;
	private GPUImageFilter mFilter;
	private FilterAdjuster mFilterAdjuster;
	String[] constant = { "NORMAL", "CONTRAST", "GRAYSCALE", "SEPIA", "EMBOSS",
			"POSTERIZE", "GAMMA", "BRIGHTNESS", "INVERT", "HUE", "SATURATION",
			"HIGHLIGHT_SHADOW", "MONOCHROME", "OPACITY", "RGB",
			"WHITE_BALANCE", "VIGNETTE", "TONE_CURVE", "BLEND_CHROMA_KEY",
			"GAUSSIAN_BLUR", "HAZE", "COLOR_BALANCE" };

	ArrayList<String> filter = new ArrayList<String>();
	private SQLiteDatabase db;
	private SQLiteStatement stm;
	public static final String DBTABLE = "lookbook";
	public static byte[] imageInByte;
	public static String imgpath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_effects);

		DBHelper hp = new DBHelper(this);
		db = hp.getWritableDatabase();
		stm = db.compileStatement("insert into  "
				+ DBTABLE
				+ " (photo,tag,desc,imagepath,storeid,mypath,card_id,slug) values (?, ?, ?, ?, ?,?,?,?)");

		img = (GPUImageView) findViewById(R.id.gpuimagebig);
		listview = (HorizontalListView) findViewById(R.id.listview);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		/*
		 * Bitmap bit = BitmapFactory.decodeResource(this.getResources(),
		 * R.drawable.mybig);
		 */
		img.setImage(Variables.imgbitmap);
		
		for (int i = 0; i < constant.length; i++) {

			filter.add(constant[i]);
		}

		listview.setAdapter(new MyAdapter(ImageEffects.this, filter));
		
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		((RelativeLayout)findViewById(R.id.rel_next)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveImage();

			}
		});
	}

	public class MyAdapter extends BaseAdapter {

		ArrayList<String> list = new ArrayList<String>();
		Context ctx;
		LayoutInflater inf;
		Bitmap icon;
		private int THUMBSIZE = 100;

		public MyAdapter(Context ctx, ArrayList<String> list) {

			this.ctx = ctx;
			this.list = list;
			inf = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			convertView = inf.inflate(R.layout.gallery_image_layout, null);
			final GPUImageView himg = (GPUImageView) convertView
					.findViewById(R.id.gallery_image_preview_item);

			/*
			 * icon = BitmapFactory.decodeResource(ctx.getResources(),
			 * R.drawable.my);
			 */
			

			try {

				mFilter = createFilterForType(ctx, list.get(position));
				himg.setFilter(mFilter);
				mFilterAdjuster = new FilterAdjuster(mFilter);
				mFilterAdjuster.adjust(60);
				//himg.requestRender();
				/*icon = BitmapFactory.decodeResource(ctx.getResources(),
						R.drawable.zakoopiicon);*/
				//icon = Bitmap.createScaledBitmap(Variables.imgbitmap, 80, 80, true);
				
				icon = ThumbnailUtils.extractThumbnail(Variables.imgbitmap,
						THUMBSIZE, THUMBSIZE);
				//icon = Bitmap.createBitmap(src)
				himg.setImage(icon);

				himg.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						/*
						 * Bitmap bit = BitmapFactory.decodeResource(
						 * ctx.getResources(), R.drawable.mybig);
						 */

						mFilter = createFilterForType(ctx, list.get(position));
						img.setFilter(mFilter);
						mFilterAdjuster = new FilterAdjuster(mFilter);
						mFilterAdjuster.adjust(60);
						himg.requestRender();
						//himg.setImage(scaleBitmap(Variables.imgbitmap, 200, 200));
						himg.setImage(icon);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

	}

	@Override
	public void onPictureSaved(final Uri uri,final File file) {
		/*Toast.makeText(this, "Saved: " + uri.toString(), Toast.LENGTH_SHORT)
				.show();*/

		/*
		 * Intent in = new Intent(ImageEffects.this, ImageDetail.class);
		 * in.putExtra("uri", uri.toString()); startActivity(in);
		 */

		if (!ImageDetail.addmoreimg.equals("addmore")) {
			DBHelper hp = new DBHelper(ImageEffects.this);
			db = hp.getWritableDatabase();
			db.delete(DBTABLE, null, null);
		}
		
		try {
			ContentResolver cr = getBaseContext().getContentResolver();
			InputStream inputStream = cr.openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			imageInByte = baos.toByteArray();
			Variables.imgarr = imageInByte;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		imgpath = file.getPath();
		stm.bindBlob(1, imageInByte);
		stm.bindString(2, "tag");
		stm.bindString(3, "desc");
		stm.bindString(4, imgpath);
		stm.bindString(5, "store_id");
		stm.bindString(6, "mypath");
		stm.bindString(7, "7");
		stm.bindString(8, "");
		stm.executeInsert();
		Intent mainIntent = new Intent(ImageEffects.this, ImageDetail.class);
		mainIntent.putExtra("ImageEffects", "ImageEffects");
		ImageEffects.this.startActivity(mainIntent);



	}

	private void saveImage() {
		String fileName = System.currentTimeMillis() + ".jpg";
		img.saveToPictures("Zakoopi", fileName, this);

	}

	// Code for image filler

	private GPUImageFilter createFilterForType(final Context context,
			final String type) {
		switch (type) {
		case "NORMAL":
			return new GPUImageNormalFilter(1.0f);

		case "CONTRAST":
			return new GPUImageContrastFilter(2.0f);
		case "GAMMA":
			return new GPUImageGammaFilter(2.0f);
		case "INVERT":
			return new GPUImageColorInvertFilter();

		case "HUE":
			return new GPUImageHueFilter(90.0f);
		case "BRIGHTNESS":
			return new GPUImageBrightnessFilter(1.5f);
		case "GRAYSCALE":
			return new GPUImageGrayscaleFilter();
		case "SEPIA":
			return new GPUImageSepiaFilter();

		case "EMBOSS":
			return new GPUImageEmbossFilter();
		case "POSTERIZE":
			return new GPUImagePosterizeFilter();

		case "SATURATION":
			return new GPUImageSaturationFilter(1.0f);

		case "HIGHLIGHT_SHADOW":
			return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
		case "MONOCHROME":
			return new GPUImageMonochromeFilter(1.0f, new float[] { 0.6f,
					0.45f, 0.3f, 1.0f });
		case "OPACITY":
			return new GPUImageOpacityFilter(1.0f);
		case "RGB":
			return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
		case "WHITE_BALANCE":
			return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
		case "VIGNETTE":
			PointF centerPoint = new PointF();
			centerPoint.x = 0.5f;
			centerPoint.y = 0.5f;
			return new GPUImageVignetteFilter(centerPoint, new float[] { 0.0f,
					0.0f, 0.0f }, 0.3f, 0.75f);
		case "TONE_CURVE":
			GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
			toneCurveFilter.setFromCurveFileInputStream(context.getResources()
					.openRawResource(R.raw.tone_cuver_sample));
			return toneCurveFilter;

		case "BLEND_CHROMA_KEY":
			return createBlendFilter(context,
					GPUImageChromaKeyBlendFilter.class);

		case "GAUSSIAN_BLUR":
			return new GPUImageGaussianBlurFilter();

		case "HAZE":
			return new GPUImageHazeFilter();

		case "COLOR_BALANCE":
			return new GPUImageColorBalanceFilter();

		default:
			throw new IllegalStateException("No filter of that type!");
		}

	}

	private static GPUImageFilter createBlendFilter(Context context,
			Class<? extends GPUImageTwoInputFilter> filterClass) {
		try {
			GPUImageTwoInputFilter filter = filterClass.newInstance();
			filter.setBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher));
			return filter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class FilterAdjuster {
		private final Adjuster<? extends GPUImageFilter> adjuster;

		public FilterAdjuster(final GPUImageFilter filter) {
			if (filter instanceof GPUImageSharpenFilter) {
				adjuster = new SharpnessAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSepiaFilter) {
				adjuster = new SepiaAdjuster().filter(filter);
			} else if (filter instanceof GPUImageContrastFilter) {
				adjuster = new ContrastAdjuster().filter(filter);
			} else if (filter instanceof GPUImageGammaFilter) {
				adjuster = new GammaAdjuster().filter(filter);
			} else if (filter instanceof GPUImageBrightnessFilter) {
				adjuster = new BrightnessAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSobelEdgeDetection) {
				adjuster = new SobelAdjuster().filter(filter);
			} else if (filter instanceof GPUImageEmbossFilter) {
				adjuster = new EmbossAdjuster().filter(filter);
			} else if (filter instanceof GPUImage3x3TextureSamplingFilter) {
				adjuster = new GPU3x3TextureAdjuster().filter(filter);
			} else if (filter instanceof GPUImageHueFilter) {
				adjuster = new HueAdjuster().filter(filter);
			} else if (filter instanceof GPUImagePosterizeFilter) {
				adjuster = new PosterizeAdjuster().filter(filter);
			} else if (filter instanceof GPUImagePixelationFilter) {
				adjuster = new PixelationAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSaturationFilter) {
				adjuster = new SaturationAdjuster().filter(filter);
			} else if (filter instanceof GPUImageExposureFilter) {
				adjuster = new ExposureAdjuster().filter(filter);
			} else if (filter instanceof GPUImageHighlightShadowFilter) {
				adjuster = new HighlightShadowAdjuster().filter(filter);
			} else if (filter instanceof GPUImageMonochromeFilter) {
				adjuster = new MonochromeAdjuster().filter(filter);
			} else if (filter instanceof GPUImageOpacityFilter) {
				adjuster = new OpacityAdjuster().filter(filter);
			} else if (filter instanceof GPUImageRGBFilter) {
				adjuster = new RGBAdjuster().filter(filter);
			} else if (filter instanceof GPUImageWhiteBalanceFilter) {
				adjuster = new WhiteBalanceAdjuster().filter(filter);
			} else if (filter instanceof GPUImageVignetteFilter) {
				adjuster = new VignetteAdjuster().filter(filter);
			} else if (filter instanceof GPUImageDissolveBlendFilter) {
				adjuster = new DissolveBlendAdjuster().filter(filter);
			} else if (filter instanceof GPUImageGaussianBlurFilter) {
				adjuster = new GaussianBlurAdjuster().filter(filter);
			} else if (filter instanceof GPUImageCrosshatchFilter) {
				adjuster = new CrosshatchBlurAdjuster().filter(filter);
			} else if (filter instanceof GPUImageBulgeDistortionFilter) {
				adjuster = new BulgeDistortionAdjuster().filter(filter);
			} else if (filter instanceof GPUImageGlassSphereFilter) {
				adjuster = new GlassSphereAdjuster().filter(filter);
			} else if (filter instanceof GPUImageHazeFilter) {
				adjuster = new HazeAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSphereRefractionFilter) {
				adjuster = new SphereRefractionAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSwirlFilter) {
				adjuster = new SwirlAdjuster().filter(filter);
			} else if (filter instanceof GPUImageColorBalanceFilter) {
				adjuster = new ColorBalanceAdjuster().filter(filter);
			} else if (filter instanceof GPUImageLevelsFilter) {
				adjuster = new LevelsMinMidAdjuster().filter(filter);
			} else if (filter instanceof GPUImageBilateralFilter) {
				adjuster = new BilateralAdjuster().filter(filter);
			} else {

				adjuster = null;
			}
		}

		public boolean canAdjust() {
			return adjuster != null;
		}

		public void adjust(final int percentage) {
			if (adjuster != null) {
				adjuster.adjust(percentage);
			}
		}

		private abstract class Adjuster<T extends GPUImageFilter> {
			private T filter;

			@SuppressWarnings("unchecked")
			public Adjuster<T> filter(final GPUImageFilter filter) {
				this.filter = (T) filter;
				return this;
			}

			public T getFilter() {
				return filter;
			}

			public abstract void adjust(int percentage);

			protected float range(final int percentage, final float start,
					final float end) {
				return (end - start) * percentage / 100.0f + start;
			}

			protected int range(final int percentage, final int start,
					final int end) {
				return (end - start) * percentage / 100 + start;
			}
		}

		private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setSharpness(range(percentage, -4.0f, 4.0f));
			}
		}

		private class PixelationAdjuster extends
				Adjuster<GPUImagePixelationFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setPixel(range(percentage, 1.0f, 100.0f));
			}
		}

		private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setHue(range(percentage, 0.0f, 360.0f));
			}
		}

		private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setContrast(range(percentage, 0.0f, 2.0f));
			}
		}

		private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setGamma(range(percentage, 0.0f, 3.0f));
			}
		}

		private class BrightnessAdjuster extends
				Adjuster<GPUImageBrightnessFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setBrightness(range(percentage, -1.0f, 1.0f));
			}
		}

		private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setIntensity(range(percentage, 0.0f, 2.0f));
			}
		}

		private class SobelAdjuster extends
				Adjuster<GPUImageSobelEdgeDetection> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
			}
		}

		private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setIntensity(range(percentage, 0.0f, 4.0f));
			}
		}

		private class PosterizeAdjuster extends
				Adjuster<GPUImagePosterizeFilter> {
			@Override
			public void adjust(final int percentage) {
				// In theorie to 256, but only first 50 are interesting
				getFilter().setColorLevels(range(percentage, 1, 50));
			}
		}

		private class GPU3x3TextureAdjuster extends
				Adjuster<GPUImage3x3TextureSamplingFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
			}
		}

		private class SaturationAdjuster extends
				Adjuster<GPUImageSaturationFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setSaturation(range(percentage, 0.0f, 2.0f));
			}
		}

		private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setExposure(range(percentage, -10.0f, 10.0f));
			}
		}

		private class HighlightShadowAdjuster extends
				Adjuster<GPUImageHighlightShadowFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setShadows(range(percentage, 0.0f, 1.0f));
				getFilter().setHighlights(range(percentage, 0.0f, 1.0f));
			}
		}

		private class MonochromeAdjuster extends
				Adjuster<GPUImageMonochromeFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setIntensity(range(percentage, 0.0f, 1.0f));
				// getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
			}
		}

		private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setOpacity(range(percentage, 0.0f, 1.0f));
			}
		}

		private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setRed(range(percentage, 0.0f, 1.0f));
				// getFilter().setGreen(range(percentage, 0.0f, 1.0f));
				// getFilter().setBlue(range(percentage, 0.0f, 1.0f));
			}
		}

		private class WhiteBalanceAdjuster extends
				Adjuster<GPUImageWhiteBalanceFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setTemperature(range(percentage, 2000.0f, 8000.0f));
				// getFilter().setTint(range(percentage, -100.0f, 100.0f));
			}
		}

		private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setVignetteStart(range(percentage, 0.0f, 1.0f));
			}
		}

		private class DissolveBlendAdjuster extends
				Adjuster<GPUImageDissolveBlendFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setMix(range(percentage, 0.0f, 1.0f));
			}
		}

		private class GaussianBlurAdjuster extends
				Adjuster<GPUImageGaussianBlurFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setBlurSize(range(percentage, 0.0f, 1.0f));
			}
		}

		private class CrosshatchBlurAdjuster extends
				Adjuster<GPUImageCrosshatchFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter()
						.setCrossHatchSpacing(range(percentage, 0.0f, 0.06f));
				getFilter().setLineWidth(range(percentage, 0.0f, 0.006f));
			}
		}

		private class BulgeDistortionAdjuster extends
				Adjuster<GPUImageBulgeDistortionFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setRadius(range(percentage, 0.0f, 1.0f));
				getFilter().setScale(range(percentage, -1.0f, 1.0f));
			}
		}

		private class GlassSphereAdjuster extends
				Adjuster<GPUImageGlassSphereFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setRadius(range(percentage, 0.0f, 1.0f));
			}
		}

		private class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setDistance(range(percentage, -0.3f, 0.3f));
				getFilter().setSlope(range(percentage, -0.3f, 0.3f));
			}
		}

		private class SphereRefractionAdjuster extends
				Adjuster<GPUImageSphereRefractionFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setRadius(range(percentage, 0.0f, 1.0f));
			}
		}

		private class SwirlAdjuster extends Adjuster<GPUImageSwirlFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setAngle(range(percentage, 0.0f, 2.0f));
			}
		}

		private class ColorBalanceAdjuster extends
				Adjuster<GPUImageColorBalanceFilter> {

			@Override
			public void adjust(int percentage) {
				getFilter().setMidtones(
						new float[] { range(percentage, 0.0f, 1.0f),
								range(percentage / 2, 0.0f, 1.0f),
								range(percentage / 3, 0.0f, 1.0f) });
			}
		}

		private class LevelsMinMidAdjuster extends
				Adjuster<GPUImageLevelsFilter> {
			@Override
			public void adjust(int percentage) {
				getFilter().setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f);
			}
		}

		private class BilateralAdjuster extends
				Adjuster<GPUImageBilateralFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setDistanceNormalizationFactor(
						range(percentage, 0.0f, 15.0f));
			}
		}

	}

	
	
}
