package com.zakoopi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zakoopi.R;

public class ZakoopiPoints extends Activity {

	TextView txt_back, txt_zak_earning, txt_lookbook_add, txt_200, txt_point1,
			txt_lookbook_like, txt_50, txt_like_point, txt_lookbook_review,
			txt_20, txt_review_point, txt_zak_coupons, txt_start, txt_nxt_line;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	RelativeLayout rel_back, rel_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zakoopi_points);

		type_face();
		findId();
		click();
	}

	public void findId() {
		txt_back = (TextView) findViewById(R.id.txt);
		txt_zak_earning = (TextView) findViewById(R.id.txt_zak_earning);
		txt_lookbook_add = (TextView) findViewById(R.id.txt_lookbook_add);
		txt_200 = (TextView) findViewById(R.id.txt_200);
		txt_point1 = (TextView) findViewById(R.id.txt_point1);
		txt_lookbook_like = (TextView) findViewById(R.id.txt_lookbook_like);
		txt_50 = (TextView) findViewById(R.id.txt_50);
		txt_like_point = (TextView) findViewById(R.id.txt_like_point);
		txt_lookbook_review = (TextView) findViewById(R.id.txt_lookbook_review);
		txt_20 = (TextView) findViewById(R.id.txt_20);
		txt_review_point = (TextView) findViewById(R.id.txt_review_point);
		txt_zak_coupons = (TextView) findViewById(R.id.txt_zak_coupons);
		
		txt_nxt_line = (TextView) findViewById(R.id.txt_nxt_line);
		txt_start = (TextView) findViewById(R.id.txt_start);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		rel_btn = (RelativeLayout) findViewById(R.id.rel_btn);

		txt_back.setTypeface(typeface_semibold);
		txt_zak_earning.setTypeface(typeface_semibold);
		txt_lookbook_add.setTypeface(typeface_semibold);
		txt_200.setTypeface(typeface_semibold);
		txt_point1.setTypeface(typeface_semibold);
		txt_lookbook_like.setTypeface(typeface_semibold);
		txt_50.setTypeface(typeface_semibold);
		txt_like_point.setTypeface(typeface_semibold);
		txt_lookbook_review.setTypeface(typeface_semibold);
		txt_20.setTypeface(typeface_semibold);
		txt_review_point.setTypeface(typeface_semibold);
		txt_zak_coupons.setTypeface(typeface_regular);
		txt_nxt_line.setTypeface(typeface_regular);
		txt_start.setTypeface(typeface_bold);
		txt_back.setText("Zakoopi Points");

		String upToNCharacters = "For more info contact us at ";

		String more = "<font color='#27B3AD'>info@zakoopi.com</font>";
		txt_nxt_line.setText(Html.fromHtml(upToNCharacters + more));

		txt_nxt_line.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent txtIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				txtIntent.setType("text/plain");
				txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Zakoopi");
				txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, "message");
				startActivity(Intent.createChooser(txtIntent, "Share"));

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

	public void click() {

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		rel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
