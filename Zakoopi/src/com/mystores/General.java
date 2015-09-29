package com.mystores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;
import com.store.model.Card;
import com.store.model.LookbookCards;
import com.store.model.RelatedlookbookArrays;
import com.store.model.StoreCards;
import com.store.model.StoreImageArrays;
import com.store.model.StoreofferingArrays;
import com.store.model.StoreofferingDetais;
import com.tabbar.cam.NestedListView;
import com.zakoopi.R;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class General extends Fragment {

	GoogleMap googleMap;
	FragmentManager myFragmentManager;
	SupportMapFragment mySupportMapFragment;
	NestedListView catalogue;
	View gen;
	MyAdapter adapter;
	ArrayList<String> menoffername = new ArrayList<String>();
	ArrayList<String> menofferrangefrom = new ArrayList<String>();
	ArrayList<String> menofferrangeto = new ArrayList<String>();

	ArrayList<String> womenoffername = new ArrayList<String>();
	ArrayList<String> womenofferrangefrom = new ArrayList<String>();
	ArrayList<String> womenofferrangeto = new ArrayList<String>();

	ArrayList<String> kidoffername = new ArrayList<String>();
	ArrayList<String> kidofferrangefrom = new ArrayList<String>();
	ArrayList<String> kidofferrangeto = new ArrayList<String>();

	ImageView kids, women, men;
	ImageView img1, img2, img3, img4;
	RelativeLayout rel_img_count, rel_store_image, rel_catalouge;
	TextView image_count, txt_catalogue, txt_photos, txt_map;
	double lat, lng;
	ArrayList<StoreImageArrays> store_images = new ArrayList<StoreImageArrays>();
	ArrayList<StoreCards> card_images = new ArrayList<StoreCards>();
	ArrayList<StoreImageArrays> storeImage = new ArrayList<StoreImageArrays>();
	ArrayList<StoreofferingArrays> store_offerings = new ArrayList<StoreofferingArrays>();
	public static ArrayList<String> image_url_list;
	//ImageLoader imageLoader;
	boolean menbool = false;
	boolean womenbool = false;
	boolean kidbool = false;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	ImageView google_maps_img;
	String correct_url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		gen = inflater.inflate(R.layout.general, null);

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Store General");
		t.send(new HitBuilders.AppViewBuilder().build());
		// googleMap = ((SupportMapFragment) getChildFragmentManager()
		// .findFragmentById(R.id.location_map)).getMap();
		catalogue = (NestedListView) gen.findViewById(R.id.benchmarksList);
		catalogue.setFocusable(false);
		kids = (ImageView) gen.findViewById(R.id.imageView1);
		women = (ImageView) gen.findViewById(R.id.imageView2);
		men = (ImageView) gen.findViewById(R.id.imageView3);
		img1 = (ImageView) gen.findViewById(R.id.img_store_1);
		img2 = (ImageView) gen.findViewById(R.id.img_store_2);
		img3 = (ImageView) gen.findViewById(R.id.img_store_3);
		img4 = (ImageView) gen.findViewById(R.id.img_store_4);
		rel_store_image = (RelativeLayout) gen
				.findViewById(R.id.rel_store_image);
		rel_catalouge = (RelativeLayout) gen.findViewById(R.id.rel_catalouge);
		rel_img_count = (RelativeLayout) gen.findViewById(R.id.rel_img_count);
		image_count = (TextView) gen.findViewById(R.id.txt_photo_count);
		txt_catalogue = (TextView) gen.findViewById(R.id.txt_catalogue);
		txt_photos = (TextView) gen.findViewById(R.id.txt_photos);
		txt_map = (TextView) gen.findViewById(R.id.txt_map);
		google_maps_img = (ImageView) gen.findViewById(R.id.google_maps_img);
		kids.setVisibility(View.GONE);
		men.setVisibility(View.GONE);
		women.setVisibility(View.GONE);
		try {
			lat = Double.parseDouble(MainFragment.detail.getLatitude());
			lng = Double.parseDouble(MainFragment.detail.getLongitude());
		} catch (Exception e) {
			// TODO: handle exception
		}

		image_url_list = new ArrayList<String>();

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

		/**
		 * Set Font on TextView
		 */

		txt_catalogue.setTypeface(typeface_semibold);
		txt_photos.setTypeface(typeface_semibold);
		txt_map.setTypeface(typeface_semibold);
		image_count.setTypeface(typeface_bold);

		/*imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher).build();*/

		/**
		 * Getting all types images of store
		 */
		store_images = MainFragment.detail.getStore_images();
		for (int i = 0; i < store_images.size(); i++) {

			image_url_list.add(store_images.get(i).getMedium_img());
		}

		card_images = MainFragment.detail.getStore_cards();
		for (int i = 0; i < card_images.size(); i++) {
			Card cardss = card_images.get(i).getCard();
			
				image_url_list.add(cardss.getMedium_img());

		}

	
	
		if (image_url_list.size() == 0) {
			rel_store_image.setVisibility(View.GONE);
		} else {
			Collections.reverse(image_url_list);
			
		for (int j = 0; j < image_url_list.size(); j++) {
			
				if (image_url_list.size() > 4) {
					img1.setVisibility(View.VISIBLE);
					img2.setVisibility(View.VISIBLE);
					img3.setVisibility(View.VISIBLE);
					img4.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.VISIBLE);

					rel_store_image.setVisibility(View.VISIBLE);
					
					image_count.setText("+" + (image_url_list.size() - 4));
					
					String url1 = image_url_list.get(0).replaceAll(" ", "%20");
					String url2 = image_url_list.get(1).replaceAll(" ", "%20");
					String url3 = image_url_list.get(2).replaceAll(" ", "%20");
					String url4 = image_url_list.get(3).replaceAll(" ", "%20");
					
					Picasso.with(getActivity()).load(url1)
							.into(img1);
					Picasso.with(getActivity()).load(url2)
							.into(img2);
					Picasso.with(getActivity()).load(url3)
							.into(img3);
					Picasso.with(getActivity()).load(url4)
							.into(img4);

				} else if (image_url_list.size() == 4) {
					img1.setVisibility(View.VISIBLE);
					img2.setVisibility(View.VISIBLE);
					img3.setVisibility(View.VISIBLE);
					img4.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.GONE);

					rel_store_image.setVisibility(View.VISIBLE);
					/*
					 * imageLoader.displayImage(image_url_list.get(3), img4);
					 * imageLoader.displayImage(image_url_list.get(2), img3);
					 * imageLoader.displayImage(image_url_list.get(1), img2);
					 * imageLoader.displayImage(image_url_list.get(0), img1);
					 */
					image_count.setText("+" + (image_url_list.size() - 4));
					
					String url1 = image_url_list.get(0).replaceAll(" ", "%20");
					String url2 = image_url_list.get(1).replaceAll(" ", "%20");
					String url3 = image_url_list.get(2).replaceAll(" ", "%20");
					String url4 = image_url_list.get(3).replaceAll(" ", "%20");
					
					Picasso.with(getActivity()).load(url1)
							.into(img1);
					Picasso.with(getActivity()).load(url2)
							.into(img2);
					Picasso.with(getActivity()).load(url3)
							.into(img3);
					Picasso.with(getActivity()).load(url4)
							.into(img4);

				}else if (image_url_list.size() == 3) {

					img2.setVisibility(View.VISIBLE);
					img3.setVisibility(View.VISIBLE);
					img4.setVisibility(View.INVISIBLE);
					img1.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.INVISIBLE);
					rel_store_image.setVisibility(View.VISIBLE);
				
					String url1 = image_url_list.get(0).replaceAll(" ", "%20");
					String url2 = image_url_list.get(1).replaceAll(" ", "%20");
					String url3 = image_url_list.get(2).replaceAll(" ", "%20");
					
					Picasso.with(getActivity()).load(url1)
							.into(img1);
					Picasso.with(getActivity()).load(url2)
							.into(img2);
					Picasso.with(getActivity()).load(url3)
							.into(img3);

					img1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent in = new Intent(getActivity(),
									ImageGallery.class);
							startActivity(in);
						}
					});

					img2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent in = new Intent(getActivity(),
									ImageGallery.class);
							startActivity(in);
						}
					});

					img3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent in = new Intent(getActivity(),
									ImageGallery.class);
							startActivity(in);
						}
					});
				}

				else if (image_url_list.size() == 2) {
					img3.setVisibility(View.INVISIBLE);
					img4.setVisibility(View.INVISIBLE);
					img2.setVisibility(View.VISIBLE);
					img1.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.INVISIBLE);
					rel_store_image.setVisibility(View.VISIBLE);
					String url1 = image_url_list.get(0).replaceAll(" ", "%20");
					String url2 = image_url_list.get(1).replaceAll(" ", "%20");
					Picasso.with(getActivity()).load(url1)
							.into(img1);
					Picasso.with(getActivity()).load(url2)
							.into(img2);

					img1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent in = new Intent(getActivity(),
									ImageGallery.class);
							startActivity(in);
						}
					});
					img2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent in = new Intent(getActivity(),
									ImageGallery.class);
							startActivity(in);
						}
					});
				}

				else if (image_url_list.size() == 1) {

					img4.setVisibility(View.INVISIBLE);
					img1.setVisibility(View.VISIBLE);
					img2.setVisibility(View.INVISIBLE);
					img3.setVisibility(View.INVISIBLE);
					rel_img_count.setVisibility(View.INVISIBLE);
					rel_store_image.setVisibility(View.VISIBLE);
					
					String url1 = image_url_list.get(0).replaceAll(" ", "%20");
					
					Picasso.with(getActivity()).load(url1)
					.into(img1);

					img1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent in = new Intent(getActivity(),
									ImageGallery.class);
							startActivity(in);
						}
					});
				} 

			}

		}
		

		
		// googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
		// lat, lng), 14));

		// MarkerOptions marker = new MarkerOptions().position(
		// new LatLng(lat, lng)).title(
		// MainFragment.detail.getStore_address());
		// adding marker
		// googleMap.addMarker(marker);

		/**
		 * Getting all offerings of store
		 */

		store_offerings = MainFragment.detail.getStore_offerings();

		if (store_offerings.size() == 0) {
			rel_catalouge.setVisibility(View.GONE);
		} else {

			for (int k = 0; k < store_offerings.size(); k++) {
				rel_catalouge.setVisibility(View.VISIBLE);
				StoreofferingDetais offering = store_offerings.get(k)
						.getOffering();

				if (offering.getCategory().equals("Men")) {

					men.setVisibility(View.VISIBLE);
					menoffername.add(offering.getName());
					menofferrangefrom.add(store_offerings.get(k)
							.getPrice_range_from());
					menofferrangeto.add(store_offerings.get(k)
							.getPrice_range_to());
					menbool = true;

				} else if (offering.getCategory().equals("Women")) {

					women.setVisibility(View.VISIBLE);
					womenoffername.add(offering.getName());
					womenofferrangefrom.add(store_offerings.get(k)
							.getPrice_range_from());
					womenofferrangeto.add(store_offerings.get(k)
							.getPrice_range_to());
					womenbool = true;

				} else if (offering.getCategory().equals("Kids")) {

					kids.setVisibility(View.VISIBLE);
					kidoffername.add(offering.getName());
					kidofferrangefrom.add(store_offerings.get(k)
							.getPrice_range_from());
					kidofferrangeto.add(store_offerings.get(k)
							.getPrice_range_to());
					kidbool = true;
				}
			}
		}

		kids.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				catalogue.setAdapter(null);
				adapter = new MyAdapter(getActivity(), kidoffername,
						kidofferrangefrom, kidofferrangeto);

				catalogue.setAdapter(adapter);
				adapter.notifyDataSetChanged();

				kids.setImageResource(R.drawable.general_kids_active);
				men.setImageResource(R.drawable.general_men_inactive);
				women.setImageResource(R.drawable.general_women_inactive);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Kid tab in Store General")
						.setAction(
								"Clicked Kid tab by "
										+ MainFragment.pro_user_name)
						.setLabel("Store General").build());
			}
		});

		men.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				catalogue.setAdapter(null);
				adapter = new MyAdapter(getActivity(), menoffername,
						menofferrangefrom, menofferrangeto);

				catalogue.setAdapter(adapter);
				adapter.notifyDataSetChanged();

				kids.setImageResource(R.drawable.general_kids_inactive);
				men.setImageResource(R.drawable.general_men_active);
				women.setImageResource(R.drawable.general_women_inactive);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Men tab in Store General")
						.setAction(
								"Clicked Men tab by "
										+ MainFragment.pro_user_name)
						.setLabel("Store General").build());

			}
		});

		women.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				catalogue.setAdapter(null);
				adapter = new MyAdapter(getActivity(), womenoffername,
						womenofferrangefrom, womenofferrangeto);

				catalogue.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				kids.setImageResource(R.drawable.general_kids_inactive);
				men.setImageResource(R.drawable.general_men_inactive);
				women.setImageResource(R.drawable.general_women_active);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Women tab in Store General")
						.setAction(
								"Clicked Women tab by "
										+ MainFragment.pro_user_name)
						.setLabel("Store General").build());

			}
		});

		if (womenbool == true) {
			adapter = new MyAdapter(getActivity(), womenoffername,
					womenofferrangefrom, womenofferrangeto);
			catalogue.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else if (womenbool == false && menbool == true) {
			adapter = new MyAdapter(getActivity(), menoffername,
					menofferrangefrom, menofferrangeto);
			catalogue.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			men.setImageResource(R.drawable.general_men_active);

		} else if (womenbool == false && menbool == false && kidbool == true) {

			adapter = new MyAdapter(getActivity(), kidoffername,
					kidofferrangefrom, kidofferrangeto);
			catalogue.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			kids.setImageResource(R.drawable.general_kids_active);
		} else if (womenbool == false && menbool == true && kidbool == true) {
			adapter = new MyAdapter(getActivity(), menoffername,
					menofferrangefrom, menofferrangeto);
			catalogue.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			men.setImageResource(R.drawable.general_men_active);

		}
		rel_img_count.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), ImageGallery.class);
				startActivity(in);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Image Count in Store General")
						.setAction(
								"Clicked Store Images by "
										+ MainFragment.pro_user_name)
						.setLabel("Store General").build());
			}
		});

		try {
			// Loading map
			initilizeMap();
			/*
			 * googleMap.setMyLocationEnabled(true);
			 * googleMap.getUiSettings().setMapToolbarEnabled(true);
			 * googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		return gen;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(
				getActivity());
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(
				getActivity());
	}

	public class MyAdapter extends BaseAdapter {

		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> list3 = new ArrayList<String>();
		Context ctx;
		LayoutInflater inf;

		public MyAdapter(Context ctx, ArrayList<String> list1,
				ArrayList<String> list2, ArrayList<String> list3) {

			this.ctx = ctx;

			this.list1 = list1;
			this.list2 = list2;
			this.list3 = list3;
			inf = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list1.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			View view = arg1;
			if (view == null) {
				view = inf.inflate(R.layout.gernal_item, null);
				holder = new ViewHolder();
				holder.tv1 = (TextView) view.findViewById(R.id.txt_price_start);
				holder.tv2 = (TextView) view.findViewById(R.id.txt_price_end);

				typeface_regular = Typeface.createFromAsset(view.getContext()
						.getAssets(), "fonts/SourceSansPro-Regular.ttf");
				holder.tv1.setTypeface(typeface_regular);
				holder.tv2.setTypeface(typeface_regular);
				view.setTag(holder);
			} else {

				holder = (ViewHolder) view.getTag();
			}

			holder.tv1.setText(list1.get(arg0));
			holder.tv2.setText("\u20B9 " + list2.get(arg0) + " - " + "\u20B9 "
					+ list3.get(arg0));

			return view;
		}

		class ViewHolder {
			TextView tv1;
			TextView tv2;
		}
	}

	private void initilizeMap() {

		try {
			if (googleMap == null) {
				myFragmentManager = getChildFragmentManager();
				mySupportMapFragment = (SupportMapFragment) myFragmentManager
						.findFragmentById(R.id.location_map);
				googleMap = mySupportMapFragment.getMap();

			}
			// getting lat long for showing in google map
			try {
				lat = Double.parseDouble(MainFragment.detail.getLatitude());
				lng = Double.parseDouble(MainFragment.detail.getLongitude());
			} catch (Exception e) {
				// TODO: handle exception
			}

			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(lat, lng), 14));

			MarkerOptions marker = new MarkerOptions().position(new LatLng(lat,
					lng));
			// adding marker
			googleMap.addMarker(marker);

			googleMap.getUiSettings().setMapToolbarEnabled(false);

			google_maps_img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("geo:<" + lat + ">,<" + lng + ">?q=<" + lat
									+ ">,<" + lng + ">"));
					startActivity(intent);
					/*
					 * String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
					 * lat, lng); Intent intent = new Intent(Intent.ACTION_VIEW,
					 * Uri .parse(uri)); getActivity().startActivity(intent);
					 */
				}
			});

			googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0) {
					// TODO Auto-generated method stub

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					// set title
					alertDialogBuilder.setTitle("Zakoopi");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Do you want to open store address into google map?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											String uri = String.format(
													Locale.ENGLISH,
													"geo:%f,%f", lat, lng);
											Intent intent = new Intent(
													Intent.ACTION_VIEW, Uri
															.parse(uri));
											getActivity().startActivity(intent);

											dialog.cancel();
										}
									})
							.setNegativeButton("Exit",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close
											// the dialog box and do nothing

										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();

					return false;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
