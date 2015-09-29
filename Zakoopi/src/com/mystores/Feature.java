package com.mystores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.store.model.ArticleCoverImages;
import com.store.model.ArticleDetails;
import com.store.model.ArticleUser;
import com.store.model.ArticlestoresArray;
import com.store.model.LookbookCards;
import com.store.model.LookbookUser;
import com.store.model.RelatedlookbookArrays;
import com.store.model.StoreLookbookPojo;
import com.zakoopi.R;
import com.zakoopi.helper.ExpandableHeightListView;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.StoreFeatureAdapter;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class Feature extends Fragment {
	View feat;
	ExpandableHeightListView show_item;
	StoreFeatureAdapter adapter;
	// MaterialProgressBar progressBar;
	//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	ArrayList<RelatedlookbookArrays> lookbook = new ArrayList<RelatedlookbookArrays>();
	ArrayList<ArticlestoresArray> articlestoresArrays1 = new ArrayList<ArticlestoresArray>();
	ArrayList<ArticlestoresArray> articlestoresArrays1_1 = new ArrayList<ArticlestoresArray>();
	ArrayList<RelatedlookbookArrays> lookbook_1 = new ArrayList<RelatedlookbookArrays>();
	ArrayList<HashMap<String, String>> listmap = new ArrayList<HashMap<String, String>>();
	ArrayList<StoreLookbookPojo> pojolist = null;
	public static ArrayList<StoreLookbookPojo> pojolist1 = new ArrayList<StoreLookbookPojo>();
	public static ArrayList<Integer> colorlist1 = new ArrayList<Integer>();
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	int mode_position;
//	private DisplayImageOptions options;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	private SharedPreferences pref_location;
	SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password, pro_user_id;
	private String city_name;
	ArrayList<Integer> colorlist = new ArrayList<Integer>();
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;
	private List<ResolveInfo> listApp;
	int displayWidth;
	public int pos;
	public String url_img, user_name, other_user_id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		feat = inflater.inflate(R.layout.feature, null);

		pref_location = getActivity().getSharedPreferences("location", 1);
		city_name = pref_location.getString("city", "123");
		client = ClientHttp.getInstance(getActivity());

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Store Feature");
		t.send(new HitBuilders.AppViewBuilder().build());

		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);
		pro_user_id = pro_user_pref.getString("user_id", "NA");
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");

		show_item = (ExpandableHeightListView) feat
				.findViewById(R.id.PhoneImageGrid);
		// progressBar = (MaterialProgressBar)
		// feat.findViewById(R.id.progressBar);

		// progressBar.setColorSchemeResources(R.color.red, R.color.green,
		// R.color.blue, R.color.orange);

		show_item.setFocusable(false);

		show_item.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				MainFragment.scroll.setScrollingEnabled(true);
				return false;
			}
		});
		lookbook = MainFragment.detail.getRelated_lookbooks();
		articlestoresArrays1 = MainFragment.detail.getArticle_stores();
		if (lookbook.size() > 0) {
			new getImages().execute(lookbook);
		} else if (articlestoresArrays1.size() > 0) {
			new getImagesArticle().execute(articlestoresArrays1);
		} {
			// progressBar.setVisibility(View.GONE);
			// txt_no_lookbook.setVisibility(View.VISIBLE);
		}

		return feat;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if ((adapter != null) && (pojolist1.size() > 0)) {
			adapter.notifyDataSetChanged();
		}
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

	private class getImages extends
			AsyncTask<ArrayList<RelatedlookbookArrays>, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(
				ArrayList<RelatedlookbookArrays>... params) {
			// TODO Auto-generated method stub
			pojolist = new ArrayList<StoreLookbookPojo>();
			pojolist.clear();
			lookbook_1 = params[0];

			for (int i = 0; i < lookbook_1.size(); i++) {
				RelatedlookbookArrays pop = lookbook_1.get(i);
				LookbookUser lookbook_user = pop.getUser();
				ArrayList<LookbookCards> cards = pop.getCards();
				try {

					if (cards.size() >= 3) {

						LookbookCards ccll = cards.get(0);
						LookbookCards ccll1 = cards.get(1);
						LookbookCards ccll2 = cards.get(2);
						String main_image = ccll.getMedium_img();
						String tiny_1 = ccll1.getMedium_img();
						String tiny_2 = ccll2.getMedium_img();
						String width = ccll.getMedium_img_w();
						String height = ccll.getMedium_img_h();
						String lookbook_id = pop.getId();
						String lookbook_title = pop.getTitle();
						String lookbook_comment_count = pop
								.getLookbookcomment_count();
						String lookbook_likes_count = pop.getLikes_count();
						String lookbook_hits_text = pop.getHits_text();
						String view_count = pop.getView_count();
						String lookbook_slug = pop.getSlug();
						String lookbook_user_id = pop.getUser_id();
						String lookbook_is_liked = pop.getIs_liked();
						 user_name = lookbook_user.getFirst_name() + " "
								+ lookbook_user.getLast_name();
						// Log.e("LOGON", user_name);
						url_img = lookbook_user.getAndroid_api_img();
						String userid = lookbook_user.getId();

						StoreLookbookPojo sp = new StoreLookbookPojo(
								"Lookbooks", user_name, url_img, main_image,
								tiny_1, tiny_2, lookbook_likes_count,
								lookbook_hits_text, lookbook_title,
								String.valueOf(cards.size()), lookbook_id,
								"na", lookbook_is_liked, lookbook_slug, userid,
								lookbook_comment_count, "na", width, height,view_count);
						pojolist.add(sp);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
						pojolist1.add(sp);
						colorlist1.add(colors[rnd]);
					} else if (cards.size() == 2) {
						LookbookCards ccll = cards.get(0);
						LookbookCards ccll1 = cards.get(1);
						String main_image = ccll.getMedium_img();
						String tiny_1 = ccll1.getMedium_img();
						String width = ccll.getMedium_img_w();
						String height = ccll.getMedium_img_h();
						String lookbook_id = pop.getId();
						String lookbook_title = pop.getTitle();
						String lookbook_comment_count = pop
								.getLookbookcomment_count();
						String lookbook_likes_count = pop.getLikes_count();
						String lookbook_hits_text = pop.getHits_text();
						String view_count = pop.getView_count();
						String lookbook_slug = pop.getSlug();
						String lookbook_user_id = pop.getUser_id();
						String lookbook_is_liked = pop.getIs_liked();
						 user_name = lookbook_user.getFirst_name() + " "
								+ lookbook_user.getLast_name();
						 url_img = lookbook_user.getAndroid_api_img();
						String userid = lookbook_user.getId();

						StoreLookbookPojo sp = new StoreLookbookPojo(
								"Lookbooks", user_name, url_img, main_image,
								tiny_1, "na", lookbook_likes_count,
								lookbook_hits_text, lookbook_title,
								String.valueOf(cards.size()), lookbook_id,
								"na", lookbook_is_liked, lookbook_slug, userid,
								lookbook_comment_count, "na", width, height,view_count);
						pojolist.add(sp);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
						pojolist1.add(sp);
						colorlist1.add(colors[rnd]);
					} else {
						LookbookCards ccll = cards.get(0);
						String width = ccll.getMedium_img_w();
						String height = ccll.getMedium_img_h();
						String main_image = ccll.getMedium_img();
						String lookbook_id = pop.getId();
						String lookbook_title = pop.getTitle();
						String lookbook_comment_count = pop
								.getLookbookcomment_count();
						String lookbook_likes_count = pop.getLikes_count();
						String lookbook_hits_text = pop.getHits_text();
						String view_count = pop.getView_count();
						String lookbook_slug = pop.getSlug();
						String lookbook_user_id = pop.getUser_id();
						String lookbook_is_liked = pop.getIs_liked();
						 user_name = lookbook_user.getFirst_name() + " "
								+ lookbook_user.getLast_name();
						 url_img = lookbook_user.getAndroid_api_img();
						String userid = lookbook_user.getId();

						StoreLookbookPojo sp = new StoreLookbookPojo(
								"Lookbooks", user_name, url_img, main_image,
								"na", "na", lookbook_likes_count,
								lookbook_hits_text, lookbook_title,
								String.valueOf(cards.size()), lookbook_id,
								"na", lookbook_is_liked, lookbook_slug, userid,
								lookbook_comment_count, "na", width, height,view_count);
						pojolist.add(sp);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
						pojolist1.add(sp);
						colorlist1.add(colors[rnd]);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			ArrayList<ArticlestoresArray> articlestoresArrays = new ArrayList<ArticlestoresArray>();

			articlestoresArrays = MainFragment.detail.getArticle_stores();

			try {

				for (int k = 0; k < articlestoresArrays.size(); k++) {
					
					ArticlestoresArray asr = articlestoresArrays.get(k);
					ArticleDetails article_detail = asr.getArticle();
					ArticleUser article_user = article_detail.getUser();
					ArticleCoverImages article_cover = article_detail
							.getCover_img();

					String article_id = asr.getArticle_id();
					String store_id = asr.getStore_id();
					String store_name = asr.getName();
					String article_title = article_detail.getTitle();
					String article_slug = article_detail.getSlug();
					String article_description = article_detail
							.getDescription();
					String article_is_new = article_detail.getIs_new();
					String article_like_count = article_detail.getLikes_count();
					String article_is_liked = article_detail.getIs_liked();
					String article_hits_text = article_detail.getHits_text();
					String view_count = article_detail.getHits();
					String article_comment_count = article_detail
							.getArticle_comment_count();
					String article_user_id = article_user.getId();
					user_name = article_user.getFirst_name()
							+ " " + article_user.getLast_name();
					url_img = article_user.getAndroid_api_img();
					String article_cover_img = article_cover.getMedium_img();
					String width = article_cover.getMedium_img_w();
					String height = article_cover.getMedium_img_h();

					StoreLookbookPojo sp = new StoreLookbookPojo("Articles",
							user_name, url_img,
							article_cover_img, "na", "na", article_like_count,
							article_hits_text, article_title, "na", article_id,
							article_is_new, article_is_liked, article_slug,
							article_user_id, article_comment_count,
							article_description, width, height,view_count);
					pojolist.add(sp);
					int rnd = new Random().nextInt(colors.length);
					colorlist.add(colors[rnd]);
					pojolist1.add(sp);
					colorlist1.add(colors[rnd]);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		protected void onPostExecute(Void result) {

			try {

				// progressBar.setVisibility(View.GONE);
				adapter = new StoreFeatureAdapter(getActivity(), pojolist,
						colorlist,url_img,user_name);

				show_item.setAdapter(adapter);
				show_item.setExpanded(true);
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	
	/**
	 * 
	 * 
	 * For Article
	 * @author ZakoopiUser
	 *
	 */
	
	private class getImagesArticle extends
	AsyncTask<ArrayList<ArticlestoresArray>, Void, Void> {

@Override
protected void onPreExecute() {
	// TODO Auto-generated method stub
	super.onPreExecute();
	// progressBar.setVisibility(View.VISIBLE);
}

@Override
protected Void doInBackground(
		ArrayList<ArticlestoresArray>... params) {
	// TODO Auto-generated method stub
	pojolist = new ArrayList<StoreLookbookPojo>();
	pojolist.clear();
	articlestoresArrays1_1 = params[0];

	for (int i = 0; i < articlestoresArrays1_1.size(); i++) {
		try {
			
		
		ArticlestoresArray asr = articlestoresArrays1_1.get(i);
		ArticleDetails article_detail = asr.getArticle();
		ArticleUser article_user = article_detail.getUser();
		ArticleCoverImages article_cover = article_detail
				.getCover_img();

		String article_id = asr.getArticle_id();
		String store_id = asr.getStore_id();
		String store_name = asr.getName();
		String article_title = article_detail.getTitle();
		String article_slug = article_detail.getSlug();
		String article_description = article_detail
				.getDescription();
		String article_is_new = article_detail.getIs_new();
		String article_like_count = article_detail.getLikes_count();
		String article_is_liked = article_detail.getIs_liked();
		String article_hits_text = article_detail.getHits_text();
		String view_count = article_detail.getHits();
		String article_comment_count = article_detail
				.getArticle_comment_count();
		String article_user_id = article_user.getId();
		user_name = article_user.getFirst_name()
				+ " " + article_user.getLast_name();
		url_img = article_user.getAndroid_api_img();
		String article_cover_img = article_cover.getMedium_img();
		String width = article_cover.getMedium_img_w();
		String height = article_cover.getMedium_img_h();

		StoreLookbookPojo sp = new StoreLookbookPojo("Articles",
				user_name, url_img,
				article_cover_img, "na", "na", article_like_count,
				article_hits_text, article_title, "na", article_id,
				article_is_new, article_is_liked, article_slug,
				article_user_id, article_comment_count,
				article_description, width, height,view_count);
		pojolist.add(sp);
		int rnd = new Random().nextInt(colors.length);
		colorlist.add(colors[rnd]);
		pojolist1.add(sp);
		colorlist1.add(colors[rnd]);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
			}
	
	
	ArrayList<RelatedlookbookArrays> lookbooks_11 = new ArrayList<RelatedlookbookArrays>();

	lookbooks_11 = MainFragment.detail.getRelated_lookbooks();
	for (int i = 0; i < lookbooks_11.size(); i++) {
		RelatedlookbookArrays pop = lookbooks_11.get(i);
		LookbookUser lookbook_user = pop.getUser();
		ArrayList<LookbookCards> cards = pop.getCards();
		
		try {

			if (cards.size() >= 3) {

				LookbookCards ccll = cards.get(0);
				LookbookCards ccll1 = cards.get(1);
				LookbookCards ccll2 = cards.get(2);
				String main_image = ccll.getMedium_img();
				String tiny_1 = ccll1.getMedium_img();
				String tiny_2 = ccll2.getMedium_img();
				String width = ccll.getMedium_img_w();
				String height = ccll.getMedium_img_h();
				String lookbook_id = pop.getId();
				String lookbook_title = pop.getTitle();
				String lookbook_comment_count = pop
						.getLookbookcomment_count();
				String lookbook_likes_count = pop.getLikes_count();
				String lookbook_hits_text = pop.getHits_text();
				String view_count = pop.getView_count();
				String lookbook_slug = pop.getSlug();
				String lookbook_user_id = pop.getUser_id();
				String lookbook_is_liked = pop.getIs_liked();
				 user_name = lookbook_user.getFirst_name() + " "
						+ lookbook_user.getLast_name();
				 url_img = lookbook_user.getAndroid_api_img();
				String userid = lookbook_user.getId();

				StoreLookbookPojo sp = new StoreLookbookPojo(
						"Lookbooks", user_name, url_img, main_image,
						tiny_1, tiny_2, lookbook_likes_count,
						lookbook_hits_text, lookbook_title,
						String.valueOf(cards.size()), lookbook_id,
						"na", lookbook_is_liked, lookbook_slug, userid,
						lookbook_comment_count, "na", width, height,view_count);
				pojolist.add(sp);
				int rnd = new Random().nextInt(colors.length);
				colorlist.add(colors[rnd]);
				pojolist1.add(sp);
				colorlist1.add(colors[rnd]);
			} else if (cards.size() == 2) {
				LookbookCards ccll = cards.get(0);
				LookbookCards ccll1 = cards.get(1);
				String main_image = ccll.getMedium_img();
				String tiny_1 = ccll1.getMedium_img();
				String width = ccll.getMedium_img_w();
				String height = ccll.getMedium_img_h();
				String lookbook_id = pop.getId();
				String lookbook_title = pop.getTitle();
				String lookbook_comment_count = pop
						.getLookbookcomment_count();
				String lookbook_likes_count = pop.getLikes_count();
				String lookbook_hits_text = pop.getHits_text();
				String view_count = pop.getView_count();
				String lookbook_slug = pop.getSlug();
				String lookbook_user_id = pop.getUser_id();
				String lookbook_is_liked = pop.getIs_liked();
				 user_name = lookbook_user.getFirst_name() + " "
						+ lookbook_user.getLast_name();
				 url_img = lookbook_user.getAndroid_api_img();
				String userid = lookbook_user.getId();

				StoreLookbookPojo sp = new StoreLookbookPojo(
						"Lookbooks", user_name, url_img, main_image,
						tiny_1, "na", lookbook_likes_count,
						lookbook_hits_text, lookbook_title,
						String.valueOf(cards.size()), lookbook_id,
						"na", lookbook_is_liked, lookbook_slug, userid,
						lookbook_comment_count, "na", width, height,view_count);
				pojolist.add(sp);
				int rnd = new Random().nextInt(colors.length);
				colorlist.add(colors[rnd]);
				pojolist1.add(sp);
				colorlist1.add(colors[rnd]);
			} else {
				LookbookCards ccll = cards.get(0);
				String width = ccll.getMedium_img_w();
				String height = ccll.getMedium_img_h();
				String main_image = ccll.getMedium_img();
				String lookbook_id = pop.getId();
				String lookbook_title = pop.getTitle();
				String lookbook_comment_count = pop
						.getLookbookcomment_count();
				String lookbook_likes_count = pop.getLikes_count();
				String lookbook_hits_text = pop.getHits_text();
				String view_count = pop.getView_count();
				String lookbook_slug = pop.getSlug();
				String lookbook_user_id = pop.getUser_id();
				String lookbook_is_liked = pop.getIs_liked();
				user_name = lookbook_user.getFirst_name() + " "
						+ lookbook_user.getLast_name();
				url_img = lookbook_user.getAndroid_api_img();
				String userid = lookbook_user.getId();

				StoreLookbookPojo sp = new StoreLookbookPojo(
						"Lookbooks", user_name, url_img, main_image,
						"na", "na", lookbook_likes_count,
						lookbook_hits_text, lookbook_title,
						String.valueOf(cards.size()), lookbook_id,
						"na", lookbook_is_liked, lookbook_slug, userid,
						lookbook_comment_count, "na", width, height,view_count);
				pojolist.add(sp);
				int rnd = new Random().nextInt(colors.length);
				colorlist.add(colors[rnd]);
				pojolist1.add(sp);
				colorlist1.add(colors[rnd]);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

		
	

	

	return null;
}

protected void onPostExecute(Void result) {

	try {

		// progressBar.setVisibility(View.GONE);
		adapter = new StoreFeatureAdapter(getActivity(), pojolist,
				colorlist,url_img,user_name);

		show_item.setAdapter(adapter);
		show_item.setExpanded(true);
		adapter.notifyDataSetChanged();
	} catch (Exception e) {
		// TODO: handle exception
	}
}

}
	
	
	
	
}
