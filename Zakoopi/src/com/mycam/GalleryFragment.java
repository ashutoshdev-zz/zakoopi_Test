package com.mycam;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.helper.CustomScrollView;
import com.zakoopi.helper.CustomViewPager;
import com.zakoopi.helper.ExpandableHeightGridView;
import com.zakoopi.helper.ImageItem;
import com.zakoopi.helper.MyService;
import com.zakoopi.helper.SquareImageView;
import com.zakoopi.helper.Variables;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class GalleryFragment extends Fragment {

	ExpandableHeightGridView gridview;
	ImageCropView imgcrop;
	ImageView next;
	FrameLayout framely;
	ProgressBar progress;
	Animation animFadein;
	public ImageAdapter imageAdapter;
	protected Bitmap b;
	CustomScrollView scroll;
	boolean _areLecturesLoaded = false;
	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	SparseBooleanArray mSparseBooleanArray;
	Cursor imagecursor;
	TextView txt_gallery;
	RelativeLayout rel_back;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View gallry = inflater.inflate(R.layout.gallery, null);

		/**
		* Google Analystics
		*/

		Tracker t = ((UILApplication) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Lookbook Gallery");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		mSparseBooleanArray = new SparseBooleanArray();
		this.imageUrls = new ArrayList<String>();
		imageUrls.size();
		
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

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(color.darker_gray)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		
	

		gridview = (ExpandableHeightGridView) gallry
				.findViewById(R.id.PhoneImageGrid);

		gridview.setExpanded(true);
		imgcrop = (ImageCropView) gallry.findViewById(R.id.image);
		next = (ImageView) gallry.findViewById(R.id.imageView1);
		framely = (FrameLayout) gallry.findViewById(R.id.frm);
		progress = (ProgressBar) gallry.findViewById(R.id.progressBar1);
		scroll = (CustomScrollView) gallry.findViewById(R.id.scrollView1);
		txt_gallery = (TextView) gallry.findViewById(R.id.txt_gallery);
		rel_back = (RelativeLayout) gallry.findViewById(R.id.rel_back);
		txt_gallery.setTypeface(typeface_bold);
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});

		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		CameraFragment.displayWidth = size.x;
		CameraFragment.displayHeight = size.y;

		gridview.setFocusable(false);
		new DownloadFilesTask().execute();
		imgcrop.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				CustomViewPager.setSwipeable(false);
				scroll.setScrollingEnabled(false);
				return false;
			}
		});

		gridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				CustomViewPager.setSwipeable(true);
				scroll.setScrollingEnabled(true);
				return false;
			}
		});

		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				try {
					
				
				if (!imgcrop.isChangingScale()) {
					b = imgcrop.getCroppedImage();
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					Variables.imgbitmap = b;
					
					// Intent for service starting
					/*Intent i = new Intent();
					i.setClass(getActivity(), MyService.class);
					getActivity().startService(i);*/
					
					Intent in = new Intent(getActivity(), ImageEffects.class);
					startActivity(in);
					
					
					
					
				}
				
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		// load the animation
		animFadein = AnimationUtils
				.loadAnimation(getActivity(), R.anim.fade_in);

		return gallry;
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		try {

			// scroll.setScrollingEnabled(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			scroll.setVisibility(View.GONE);

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// getImageFromGallery();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			scroll.setVisibility(View.VISIBLE);
			scroll.setScrollingEnabled(true);

		try{
			imageAdapter = new ImageAdapter(getActivity(),
					LookBookTabsActivity.images);
			gridview.setAdapter(imageAdapter);

			ImageItem item = LookBookTabsActivity.images.get(0);
			// imgcrop.setImageFilePath(item.path);
			ImageLoader.getInstance().displayImage("file://" + item.path,
					imgcrop, options);

			imgcrop.setAspectRatio(1, 1.5);
		}catch(Exception e){
			e.printStackTrace();
		}
		}

	}


	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<ImageItem> images = new ArrayList<ImageItem>();
		private int selectedItem = 0;
		final int THUMBSIZE = 80;

		public ImageAdapter(Context ctx, ArrayList<ImageItem> images) {

			this.images = images;
			mInflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return images.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public void setSelectedItem(int selectItemid) {

			if (this.selectedItem != selectItemid) {
				this.selectedItem = selectItemid;
				notifyDataSetChanged();
			}
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;

			final ImageItem item = images.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.galleryitem, null);
				holder.imageview = (SquareImageView) convertView
						.findViewById(R.id.thumbImage);
				

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.imageview.setId(position);

			Picasso.with(getActivity()).load("file://"+item.path) .resize(100, 100)
			  .centerCrop().into(holder.imageview);

			if (selectedItem == position) {

				holder.imageview.setBackgroundColor(Color.parseColor("#f1c40f"));
				

			} else {

				holder.imageview.setBackgroundColor(Color.parseColor("#000000"));
				
			}

			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//imgcrop.startAnimation(animFadein);
					ImageLoader.getInstance().displayImage(
							"file://" + item.path, imgcrop, options);

					imgcrop.setAspectRatio(1, 1.5);
					scroll.setScrollingEnabled(true);
					scroll.scrollTo(0, (int) imgcrop.getY());
					
					setSelectedItem(position);

				}
			});

			return convertView;
		}

	}

	class ViewHolder {
		SquareImageView imageview;
		LinearLayout rel;
	}

	
}
