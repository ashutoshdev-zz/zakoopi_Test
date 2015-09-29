package com.mycam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.effect.Effect;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.image.effects.PixelBuffer;
import com.image.effects.ZakoopiImageRender;
import com.image.effects.ZakoopiImageUtils;
import com.zakoopi.R;

@SuppressLint("NewApi")
public class GalleryImageAdapter extends BaseAdapter {

	private List<String> effectArray = new ArrayList<String>();
	private Context context;
	private LayoutInflater inflater;
	private Bitmap srcBitmap;

	public Effect effect;
	private int selectedItem = -1;
	private List<Bitmap> previewList;
	private Map<String, Bitmap> previewImageMap = new HashMap<String, Bitmap>();
	private ZakoopiImageRender imageBlurRender;
	private PixelBuffer pixelBuffer;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;

	public GalleryImageAdapter(Context context, Bitmap imageBitmap) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.srcBitmap = imageBitmap;
		imageBlurRender = new ZakoopiImageRender(context, srcBitmap);
		
		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(context.getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface.createFromAsset(context.getAssets(),
				"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(context.getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface.createFromAsset(context.getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(context.getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
	}

	@Override
	public int getCount() {
		return ZakoopiImageUtils.getEffectName().size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedItem(int selectItemid) {
		//Log.i("jiangtao4", "in setSelectItem");
		if (this.selectedItem != selectItemid) {
			this.selectedItem = selectItemid;
			notifyDataSetChanged();
		}
	}

	public void setBlurPreviewImage(List<Bitmap> list) {
		previewList = list;
	}

	@Override
	public View getView(int position, View conteView, ViewGroup viewGroup) {
		ViewHolder viewHolder = null;
		if (conteView == null) {
			conteView = (LinearLayout) inflater.inflate(
					R.layout.gallery_image_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.roundedImageView = (ImageView) conteView
					.findViewById(R.id.gallery_image_preview_item);
			viewHolder.textView = (TextView) conteView
					.findViewById(R.id.gallery_text);
			viewHolder.textView.setTypeface(typeface_regular);
			viewHolder.textView.setVisibility(View.GONE);
			
			conteView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) conteView.getTag();
		}

		String effectName = ZakoopiImageUtils.getEffectName().get(position);
		Bitmap preBitmap = previewImageMap.get(effectName);
		if (preBitmap == null) {
			if (position == 0) {
				viewHolder.roundedImageView.setImageBitmap(srcBitmap);
			} else {
				viewHolder.roundedImageView.setBackground(context
						.getResources().getDrawable(R.drawable.nine_patch_icn));
				viewHolder.roundedImageView.setTag(effectName);

				// start asynctask blurimage

				new BlurImagesTask(position, effectName,
						viewHolder.roundedImageView).execute(srcBitmap);

			}
		} else {
			viewHolder.roundedImageView.setImageBitmap(preBitmap);
		}
		viewHolder.textView.setText(ZakoopiImageUtils.getEffectName().get(
				position));

		if (selectedItem == position) {
			// selected item will show different
			viewHolder.roundedImageView.setBackgroundColor(Color
					.parseColor("#f1c40f"));
			viewHolder.textView.setTextColor(Color.parseColor("#f1c40f"));
			ObjectAnimator bAnimatorX = ObjectAnimator.ofFloat(
					viewHolder.roundedImageView, "scaleX", 1.1f);
			ObjectAnimator bAnimatorY = ObjectAnimator.ofFloat(
					viewHolder.roundedImageView, "scaleY", 1.1f);
			bAnimatorX.setDuration(300);
			bAnimatorY.setDuration(300);
			AnimatorSet animationSet = new AnimatorSet();
			animationSet.play(bAnimatorX).with(bAnimatorY);
			animationSet.start();
		} else {
			viewHolder.textView.setTextColor(Color.parseColor("#ffffff"));
			viewHolder.roundedImageView.setBackgroundColor(Color
					.parseColor("#ffffff"));
		}
		return conteView;
	}

	public static class ViewHolder {
		public ImageView roundedImageView;
		public TextView textView;
	}

	public class BlurImagesTask extends AsyncTask<Bitmap, Void, Bitmap> {

		private int position;
		private String strEffectName;
		private ImageView roundedImageView;

		public BlurImagesTask(int pos, String name, ImageView imageView) {
			this.position = pos;
			this.strEffectName = name;
			this.roundedImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Bitmap... param) {
			Bitmap bitmap = param[0];

			try {
				pixelBuffer = new PixelBuffer(bitmap.getWidth(),
						bitmap.getHeight());
				pixelBuffer.setRenderer(imageBlurRender);
				imageBlurRender.mCurrentEffect = position;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return pixelBuffer.getBitmap(null, null);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (((String) roundedImageView.getTag()).equals(strEffectName)) {
				roundedImageView.setImageBitmap(result);
			}
			previewImageMap.put(strEffectName, result);
		}

	}

}
