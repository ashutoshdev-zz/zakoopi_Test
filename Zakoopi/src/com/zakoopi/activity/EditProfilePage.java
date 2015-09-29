package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zakoopi.R;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.ProgressHUD;
import com.zakoopi.helper.Variables;
import com.zakoopi.user.model.User;
import com.zakoopi.user.model.UserDetails;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class EditProfilePage extends FragmentActivity implements
		OnCancelListener {

	RelativeLayout rel_back;
	RelativeLayout rel_submit;
	EditText edt_yourself, editname, editlocation, edtage;
	//, edtgender;
	CircleImageView user_pic;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	private SharedPreferences pro_user_pref,prefs_gcm;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_id,pro_user_age, pro_user_gender,gcm_id;
	TextView counter;
	ImageView changepic;
	// ImageView removepic;
	String about;
	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	public static final int REQUEST_CODE_UPDATE_PIC = 0x1;
	AsyncHttpClient client;
	TextView txt_edit_profile, txt_submit;
	private String user_email;
	ProgressHUD mProgressHUD;
	private String user_password;
	final static int DEFAULT_TIMEOUT = 40 * 1000;

	// image croper
	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;

	private static final int ROTATE_NINETY_DEGREES = 90;

	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";

	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";

	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;

	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
	public File file;
	Bitmap croppedImage;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	RadioGroup radio_sex_group;
	RadioButton radioSex_male,radioSex_female,radioSexButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.profile_edit_user);
		client = ClientHttp.getInstance(EditProfilePage.this);
		cd = new ConnectionDetector(getApplicationContext());
		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Edit Profile of " + pro_user_name);
		t.send(new HitBuilders.AppViewBuilder().build());
		 prefs_gcm = getSharedPreferences("GCM", 0);
		 gcm_id = prefs_gcm.getString("REG_ID", "asgdj");

		user_pref();
		type_face();
		findId();
		setTypeface();
		setUserPref_data();
		click();

	}

	/**
	 * User Login SharedPreferences
	 */

	public void user_pref() {

		pro_user_pref = getSharedPreferences("User_detail", 0);
		try {

			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");
			pro_user_location = pro_user_pref
					.getString("user_location", "4267");
			pro_user_age = pro_user_pref.getString("user_age", "aasdf");
			pro_user_gender = pro_user_pref.getString("user_gender", "145");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");
			user_id = pro_user_pref.getString("user_id", "0");
			about = pro_user_pref.getString("about", "no");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Set User Pref Data
	 */
	public void setUserPref_data() {

		try {

			Picasso.with(getApplicationContext()).load(pro_user_pic_url)
					.into(user_pic);
			editname.setText(pro_user_name);
			if (pro_user_location.equals("4267")) {
				editlocation.setText("");
			} else {
				editlocation.setText(pro_user_location);
			}
			
			if (pro_user_age.equals("aasdf")) {
				edtage.setText("");
			}else {
				edtage.setText(pro_user_age);	
			}
			
			 radioSex_male = (RadioButton) findViewById(R.id.radioMale);
			 radioSex_female = (RadioButton) findViewById(R.id.radioFemale);
			 
			if (pro_user_gender.equals("145") ) {
			} else if (pro_user_gender.equals("false")) {
				radioSex_male.setChecked(true);
			} else if (pro_user_gender.equals("true")) {
				radioSex_female.setChecked(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(EditProfilePage.this).reportActivityStart(
				this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(EditProfilePage.this).reportActivityStop(
				this);
	}

	/**
	 * Find ID's
	 */

	public void findId() {
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		rel_submit = (RelativeLayout) findViewById(R.id.rel_submit);
		counter = (TextView) findViewById(R.id.txt_counter);
		edt_yourself = (EditText) findViewById(R.id.edt_yourself);
		edtage = (EditText) findViewById(R.id.txt_age);
		radio_sex_group = (RadioGroup) findViewById(R.id.radioSex);
		//edtgender = (EditText) findViewById(R.id.txt_gender);
		editname = (EditText) findViewById(R.id.txt_user_name);
		editlocation = (EditText) findViewById(R.id.txt_location_name);
		user_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
		counter.setText("0/140");
		changepic = (ImageView) findViewById(R.id.img_change_pic);
		txt_edit_profile = (TextView) findViewById(R.id.txt_edit_profile);
		txt_submit = (TextView) findViewById(R.id.txt_submit);
		txt_edit_profile.setTypeface(typeface_semibold);
		txt_edit_profile.setText("Edit Profile");
		txt_submit.setTypeface(typeface_bold);
		if (about.equals("no")) {

			edt_yourself
					.setHint("Write about yourself with in 140 characters...");
		} else {

			edt_yourself.setText(about);
		}

		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(140);
		edt_yourself.setFilters(FilterArray);

		try {
			counter.setText(+edt_yourself.getText().toString().length()
					+ "/140");
		} catch (Exception e) {
			// TODO: handle exception
		}

		edt_yourself.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				try {

					counter.setText(s.length() + "/140");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		
		

		changepic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(getPickImageChooserIntent(), 200);

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Profile pic change on Edit Profile")
						.setAction("Profile pic change by " + pro_user_name)
						.setLabel("Edit Profile").build());
			}
		});

		rel_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					int selectedId = radio_sex_group.getCheckedRadioButtonId();
					 radioSexButton = (RadioButton) findViewById(selectedId);
					if ((edtage.getText().toString().length() >= 2) || (edtage.getText().toString().length() == 0)) {
						if (radioSexButton.getText().toString().equals("Male") || radioSexButton.getText().toString().equals("Female") || radioSexButton.getText().toString().equals("")) {
							upload();
						}else {
							Toast.makeText(EditProfilePage.this, "Please select gender", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(EditProfilePage.this, "Please enter age in maximum 2 digit", Toast.LENGTH_SHORT).show();
					}
					
					Tracker t = ((UILApplication) getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Edit Profile done")
							.setAction("Edit Profile done by " + pro_user_name)
							.setLabel("Edit Profile").build());
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showAlertDialog(EditProfilePage.this,
							"No Internet Connection",
							"You don't have internet connection.", false);
				}

			}
		});

	}

	/**
	 * Typeface
	 */
	public void type_face() {

		typeface_semibold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
	}

	/**
	 * Set Font on TextView
	 */

	public void setTypeface() {
		try {

			edt_yourself.setTypeface(typeface_regular);
			editname.setTypeface(typeface_regular);
			editlocation.setTypeface(typeface_regular);
			edtage.setTypeface(typeface_regular);
			radioSex_male.setTypeface(typeface_regular);
			radioSex_female.setTypeface(typeface_regular);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Click Listener
	 */

	public void click() {
		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	/*
	 * Upload data for editing user profile
	 */

	@SuppressWarnings("deprecation")
	public void upload() {
		//Log.e("GE_MAIN", edtgender.getText().toString());
		String profile_URL = getResources().getString(R.string.base_url)
				+ "users/updateProfile/" + user_id + ".json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		String name = editname.getText().toString();
		String fname[] = name.split(" ");
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {
			
			
			
			if (file == null) {
				
				if (radioSexButton.getText().toString().equals("Male")) {
					params.put("id", user_id);
					params.put("email", user_email);
					params.put("first_name", fname[0]);
					params.put("last_name", fname[1]);
					params.put("age", edtage.getText().toString());
					params.put("gender", "0");
					params.put("location", editlocation.getText().toString());
					params.put("about", edt_yourself.getText().toString());
					params.put("device_token", gcm_id);
					
					
				} else {
					params.put("id", user_id);
					params.put("email", user_email);
					params.put("first_name", fname[0]);
					params.put("last_name", fname[1]);
					params.put("age", edtage.getText().toString());
					params.put("gender", "1");
					params.put("location", editlocation.getText().toString());
					params.put("about", edt_yourself.getText().toString());
					params.put("device_token", gcm_id);
				}
				
			} else {
				
				if (radioSexButton.getText().toString().equals("Male")) {
					params.put("id", user_id);
					params.put("email", user_email);
					params.put("first_name", fname[0]);
					params.put("last_name", fname[1]);
					params.put("age", edtage.getText().toString());
					params.put("gender", "0");
					params.put("location", editlocation.getText().toString());
					params.put("about", edt_yourself.getText().toString());
					params.put("file_img", file);
					params.put("device_token", gcm_id);
					
					
				} else {
					params.put("id", user_id);
					params.put("email", user_email);
					params.put("first_name", fname[0]);
					params.put("last_name", fname[1]);
					params.put("age", edtage.getText().toString());
					params.put("gender", "1");
					params.put("location", editlocation.getText().toString());
					params.put("about", edt_yourself.getText().toString());
					params.put("file_img", file);
					params.put("device_token", gcm_id);
				}
			}
			
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		client.post(profile_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				mProgressHUD = ProgressHUD.show(EditProfilePage.this,
						"Processing...", true, true, EditProfilePage.this);
				mProgressHUD.setCancelable(false);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st = "";
					String st1 = "";
					while ((st = br.readLine()) != null) {

						st1 = st1 + st;

					}

					show_user(st1);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {

				mProgressHUD.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		mProgressHUD.dismiss();
	}

	public void show_user(String show) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		// Log.e("SHOW", "SHOW");
		User main_user1 = gson.fromJson(jsonReader, User.class);
		// UserDetails main_user = gson.fromJson(jsonReader, UserDetails.class);
		// Log.e("SHOW1", "SHOW1");
		UserDetails main_user = main_user1.getUser();

		new MyApp1().execute(main_user);
	}

	private class MyApp1 extends AsyncTask<UserDetails, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(UserDetails... params) {
			UserDetails main_user = params[0];
			SharedPreferences preferences = getSharedPreferences("User_detail",
					0);
			Editor editor = preferences.edit();

			try {

				editor.putString("user_id", main_user.getId());
				editor.putString("user_email", main_user.getEmail());
				editor.putString("user_firstName", main_user.getFirst_name());
				editor.putString("user_lastName", main_user.getLast_name());
				editor.putString("user_location", main_user.getLocation());
				editor.putString("user_uid", main_user.getUid());
				editor.putString("user_age", main_user.getAge());
				editor.putString("user_gender", main_user.getGender());
				editor.putString("user_rewardPoints", main_user.getPoints());
				editor.putString("user_reviewCount",
						main_user.getPro_review_count());
				editor.putString("user_likeCount",
						main_user.getPro_likes_count());
				editor.putString("user_fbLink", main_user.getFb_link());
				editor.putString("user_twitterLink",
						main_user.getTwitter_link());
				editor.putString("user_otherWebsite",
						main_user.getOther_website());
				editor.putString("user_image", main_user.getAndroid_api_img());
				editor.putString("password", user_password);
				editor.putString("about", main_user.getAbout());
				editor.commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			mProgressHUD.dismiss();
			// add stuff here
			customDailog(getResources().getString(R.string.profile));
		}

	}

	public void customDailog(String data) {

		final Dialog dd = new Dialog(EditProfilePage.this);
		dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setCancelable(false);
		dd.setContentView(R.layout.editprofile_dialog);
		dd.show();
		mProgressHUD.dismiss();
		final Button img1 = (Button) dd.findViewById(R.id.button1);
		final TextView ttt = (TextView) dd.findViewById(R.id.txt_message);
		ttt.setTypeface(typeface_semibold);
		img1.setTypeface(typeface_bold);
		try {
			ttt.setText(data);
		} catch (Exception e) {
			// TODO: handle exception
		}

		img1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Variables.edt_profil.equals("new");
				SharedPreferences preferences = getSharedPreferences(
						"User_detail", 0);
				String photo = preferences.getString("user_image", "kkk");
				String fname = preferences.getString("user_firstName", "kkgk");
				String lname = preferences.getString("user_lastName", "kkdk");
				String address = preferences
						.getString("user_location", "kdgkk");
				String age = preferences.getString("user_age", "asd");
				String gender = preferences.getString("user_gender", "asdf");
				Intent intent = new Intent();
				intent.putExtra("img", photo);
				intent.putExtra("name", fname + " " + lname);
				intent.putExtra("loc", address);
				intent.putExtra("age", age);
				intent.putExtra("gender", gender);
				setResult(2, intent);
				dd.dismiss();
				finish();
			}
		});

	}

	public void cropDialog(Uri imageUri) {

		final Dialog dd = new Dialog(EditProfilePage.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setContentView(R.layout.image_cropper);

		RelativeLayout rel_btn_1 = (RelativeLayout) dd
				.findViewById(R.id.rel_btn_1);
		RelativeLayout rel_btn_2 = (RelativeLayout) dd
				.findViewById(R.id.rel_btn_2);
		TextView txt_choose = (TextView) dd.findViewById(R.id.txt_choose);
		TextView txt_rotate = (TextView) dd.findViewById(R.id.txt_rotate);
		TextView txt_crop = (TextView) dd.findViewById(R.id.txt_crop);
		txt_choose.setTypeface(typeface_bold);
		txt_rotate.setTypeface(typeface_semibold);
		txt_crop.setTypeface(typeface_semibold);

		final CropImageView cropImageView = (CropImageView) dd
				.findViewById(R.id.CropImageView);

		cropImageView.setImageUri(imageUri);

		final Button rotateButton = (Button) dd.findViewById(R.id.button1);
		rel_btn_1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				cropImageView.rotateImage(ROTATE_NINETY_DEGREES);

			}
		});

		cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES,
				DEFAULT_ASPECT_RATIO_VALUES);

		rel_btn_2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				croppedImage = cropImageView.getCroppedImage();
				storeImage(croppedImage);
				dd.cancel();
			}
		});

		dd.show();

	}

	private void storeImage(Bitmap imageData) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/Zakoopi/profile");
		myDir.mkdirs();

		String fname = "Image_" + System.currentTimeMillis() + ".jpg";
		file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			imageData.compress(Bitmap.CompressFormat.JPEG, 90, out);

			// ImageView croppedImageView = (ImageView)
			// findViewById(R.id.croppedImageView);
			user_pic.setImageBitmap(imageData);

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri imageUri = getPickImageResultUri(data);
			cropDialog(imageUri);

		}
	}

	public Intent getPickImageChooserIntent() {

		// Determine Uri of camera image to save.
		Uri outputFileUri = getCaptureImageOutputUri();

		List<Intent> allIntents = new ArrayList<Intent>();
		PackageManager packageManager = getPackageManager();

		// collect all camera intents
		Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(res.activityInfo.packageName);
			if (outputFileUri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			}
			allIntents.add(intent);
		}

		// collect all gallery intents
		Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
		galleryIntent.setType("image/*");
		List<ResolveInfo> listGallery = packageManager.queryIntentActivities(
				galleryIntent, 0);
		for (ResolveInfo res : listGallery) {
			Intent intent = new Intent(galleryIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(res.activityInfo.packageName);
			allIntents.add(intent);
		}

		// the main intent is the last in the list (fucking android) so pickup
		// the useless one
		Intent mainIntent = allIntents.get(allIntents.size() - 1);
		for (Intent intent : allIntents) {
			if (intent.getComponent().getClassName()
					.equals("com.android.documentsui.DocumentsActivity")) {
				mainIntent = intent;
				break;
			}
		}
		allIntents.remove(mainIntent);

		// Create a chooser from the main intent
		Intent chooserIntent = Intent
				.createChooser(mainIntent, "Select source");

		// Add all other intents
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				allIntents.toArray(new Parcelable[allIntents.size()]));

		return chooserIntent;
	}

	/**
	 * Get URI to image received from capture by camera.
	 */
	private Uri getCaptureImageOutputUri() {
		Uri outputFileUri = null;
		File getImage = getExternalCacheDir();
		if (getImage != null) {
			outputFileUri = Uri.fromFile(new File(getImage.getPath(),
					"pickImageResult.jpeg"));
		}
		return outputFileUri;
	}

	/**
	 * Get the URI of the selected image from
	 * {@link #getPickImageChooserIntent()}.<br/>
	 * Will return the correct URI for camera and gallery image.
	 *
	 * @param data
	 *            the returned data of the activity result
	 */
	public Uri getPickImageResultUri(Intent data) {
		boolean isCamera = true;
		if (data != null) {
			String action = data.getAction();
			isCamera = action != null
					&& action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
		}
		return isCamera ? getCaptureImageOutputUri() : data.getData();
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(final Context context, String title,
			String message, Boolean status) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Title
		alertDialog.setTitle("No Internet Connection!");

		// Setting Dialog Message
		alertDialog.setMessage("Enable Internet Connection.");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						context.startActivity(intent);
						dialog.cancel();
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Dismiss",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
