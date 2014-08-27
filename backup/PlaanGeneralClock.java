package com.example.plaan;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlaanGeneralClock extends Dialog implements OnClickListener {
	public Activity c;

	TextView tvActivitiesName;
	TextView tvHours;
	TextView tvMinutes;
	TextView tvOne;
	TextView tvTwo;
	TextView tvThree;
	TextView tvFour;
	TextView tvFive;
	TextView tvSix;
	TextView tvSeven;
	TextView tvEight;
	TextView tvNine;
	TextView tvTen;
	TextView tvEleven;
	TextView tvTwelve;
	TextView tv13;
	TextView tv14;
	TextView tv15;
	TextView tv16;
	TextView tv17;
	TextView tv18;
	TextView tv19;
	TextView tv20;
	TextView tv21;
	TextView tv22;
	TextView tv23;
	TextView tv00;
	TextView tv05m;
	TextView tv10m;
	TextView tv15m;
	TextView tv20m;
	TextView tv25m;
	TextView tv30m;
	TextView tv35m;
	TextView tv40m;
	TextView tv45m;
	TextView tv50m;
	TextView tv55m;
	TextView tv00m;
	TextView tvDONE;
	RelativeLayout rlHoursPM;
	RelativeLayout rlHoursAM;
	RelativeLayout rlMinutes;
	ImageView ivPlaanGeneralClock;

	String activitiesName;
	ArrayList<TextView> tvArrayHours;
	int lastClickedHoursId;
	int lastClickedMinutesId;
	boolean hoursTurn;

	GeneralClockListener mDialogResult; // the callback

	public PlaanGeneralClock(Activity a, String activitiesName) {
		super(a);
		this.c = a;
		this.activitiesName = activitiesName;
	}

	public void setDialogResult(
			com.example.plaan.PlaanGeneralClock.GeneralClockListener dialogResult) {
		mDialogResult = dialogResult;
	}

	public interface GeneralClockListener {
		void finish(String result);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plaan_general_clock);

		tvArrayHours = new ArrayList<>();
		lastClickedHoursId = R.id.tv00;
		lastClickedMinutesId = R.id.tv00m;
		hoursTurn = true;

		intializeViews();
		setTypefaces();

		tvActivitiesName.setText(activitiesName);

		for (int i = 0; i < tvArrayHours.size(); i++) {
			tvArrayHours.get(i).setOnClickListener(this);
		}
		tvDONE.setOnClickListener(this);

	}

	private void setTypefaces() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(tvActivitiesName, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvHours, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMinutes, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvDONE, tfp.LEAGUE_GOTHIC);
		for (int i = 0; i < tvArrayHours.size(); i++) {
			tfp.setTypeface(tvArrayHours.get(i), tfp.LEAGUE_GOTHIC);
		}
	}

	private void intializeViews() {
		tvActivitiesName = (TextView) findViewById(R.id.tvActivitiesName);
		tvHours = (TextView) findViewById(R.id.tvHours);
		tvMinutes = (TextView) findViewById(R.id.tvMinutes);
		tvOne = (TextView) findViewById(R.id.tvOne);
		tvTwo = (TextView) findViewById(R.id.tvTwo);
		tvThree = (TextView) findViewById(R.id.tvThree);
		tvFour = (TextView) findViewById(R.id.tvFour);
		tvFive = (TextView) findViewById(R.id.tvFive);
		tvSix = (TextView) findViewById(R.id.tvSix);
		tvSeven = (TextView) findViewById(R.id.tvSeven);
		tvEight = (TextView) findViewById(R.id.tvEight);
		tvNine = (TextView) findViewById(R.id.tvNine);
		tvTen = (TextView) findViewById(R.id.tvTen);
		tvEleven = (TextView) findViewById(R.id.tvEleven);
		tvTwelve = (TextView) findViewById(R.id.tvTwelve);
		tv13 = (TextView) findViewById(R.id.tv13);
		tv14 = (TextView) findViewById(R.id.tv14);
		tv15 = (TextView) findViewById(R.id.tv15);
		tv16 = (TextView) findViewById(R.id.tv16);
		tv17 = (TextView) findViewById(R.id.tv17);
		tv18 = (TextView) findViewById(R.id.tv18);
		tv19 = (TextView) findViewById(R.id.tv19);
		tv20 = (TextView) findViewById(R.id.tv20);
		tv21 = (TextView) findViewById(R.id.tv21);
		tv22 = (TextView) findViewById(R.id.tv22);
		tv23 = (TextView) findViewById(R.id.tv23);
		tv00 = (TextView) findViewById(R.id.tv00);
		tv05m = (TextView) findViewById(R.id.tv05m);
		tv10m = (TextView) findViewById(R.id.tv10M);
		tv15m = (TextView) findViewById(R.id.tv15M);
		tv20m = (TextView) findViewById(R.id.tv20m);
		tv25m = (TextView) findViewById(R.id.tv25m);
		tv30m = (TextView) findViewById(R.id.tv30m);
		tv35m = (TextView) findViewById(R.id.tv35m);
		tv40m = (TextView) findViewById(R.id.tv40m);
		tv45m = (TextView) findViewById(R.id.tv45M);
		tv50m = (TextView) findViewById(R.id.tv50M);
		tv55m = (TextView) findViewById(R.id.tv55m);
		tv00m = (TextView) findViewById(R.id.tv00m);

		tvArrayHours.add(tvOne);
		tvArrayHours.add(tvTwo);
		tvArrayHours.add(tvThree);
		tvArrayHours.add(tvFour);
		tvArrayHours.add(tvFive);
		tvArrayHours.add(tvSix);
		tvArrayHours.add(tvSeven);
		tvArrayHours.add(tvEight);
		tvArrayHours.add(tvNine);
		tvArrayHours.add(tvTen);
		tvArrayHours.add(tvEleven);
		tvArrayHours.add(tvTwelve);
		tvArrayHours.add(tv13);
		tvArrayHours.add(tv14);
		tvArrayHours.add(tv15);
		tvArrayHours.add(tv16);
		tvArrayHours.add(tv17);
		tvArrayHours.add(tv18);
		tvArrayHours.add(tv19);
		tvArrayHours.add(tv20);
		tvArrayHours.add(tv21);
		tvArrayHours.add(tv22);
		tvArrayHours.add(tv23);
		tvArrayHours.add(tv00);
		tvArrayHours.add(tv05m);
		tvArrayHours.add(tv10m);
		tvArrayHours.add(tv15m);
		tvArrayHours.add(tv20m);
		tvArrayHours.add(tv25m);
		tvArrayHours.add(tv30m);
		tvArrayHours.add(tv35m);
		tvArrayHours.add(tv40m);
		tvArrayHours.add(tv45m);
		tvArrayHours.add(tv50m);
		tvArrayHours.add(tv55m);
		tvArrayHours.add(tv00m);

		tvDONE = (TextView) findViewById(R.id.tvDONE);
		rlHoursAM = (RelativeLayout) findViewById(R.id.rlHoursAM);
		rlHoursPM = (RelativeLayout) findViewById(R.id.rlHoursPM);
		rlMinutes = (RelativeLayout) findViewById(R.id.rlMinutes);
		ivPlaanGeneralClock = (ImageView) findViewById(R.id.ivPlaanGeneralClock);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == tvDONE.getId()) {
			if (tvHours.getText().length() == 0) {
				tvHours.setText("00");
			}
			if (tvMinutes.getText().length() == 0) {
				tvMinutes.setText("00");
			}
			String hoursDataThatPassedBack = tvHours.getText().toString()
					+ tvMinutes.getText().toString();
			mDialogResult.finish(hoursDataThatPassedBack);
			PlaanGeneralClock.this.dismiss();
		} else {
			if (hoursTurn) {
				// selecting the hours
				TextView selectedHours = (TextView) v;
				if (lastClickedHoursId != v.getId()) {
					TextView lastClickedHours = (TextView) findViewById(lastClickedHoursId);
					lastClickedHours.setTextColor(Color.rgb(230, 230, 230));
					selectedHours.setTextColor(Color.rgb(255, 204, 0));
					lastClickedHoursId = selectedHours.getId();
				}
				if (selectedHours.getText().length() == 1) {
					tvHours.setText("0" + selectedHours.getText());
				} else {
					tvHours.setText(selectedHours.getText());
				}
				Animation animFadeOut = AnimationUtils.loadAnimation(
						this.getContext(), android.R.anim.fade_out);
				Animation animFadeIn = AnimationUtils.loadAnimation(
						this.getContext(), android.R.anim.fade_in);
				rlHoursAM.setAnimation(animFadeOut);
				rlHoursPM.setAnimation(animFadeOut);
				rlMinutes.setAnimation(animFadeIn);

				rlHoursAM.setVisibility(View.GONE);
				rlHoursPM.setVisibility(View.GONE);
				rlMinutes.setVisibility(View.VISIBLE);

				tvMinutes.setHintTextColor(Color.rgb(211, 78, 44));

				hoursTurn = false;

			} else {
				// selecting minutes. minutesTurn
				TextView selectedMinutes = (TextView) v;
				if (lastClickedMinutesId != v.getId()) {
					TextView lastClickedMinutes = (TextView) findViewById(lastClickedMinutesId);
					lastClickedMinutes.setTextColor(Color.rgb(230, 230, 230));
					selectedMinutes.setTextColor(Color.rgb(255, 204, 0));
					lastClickedMinutesId = selectedMinutes.getId();
				}
				tvMinutes.setText(selectedMinutes.getText());
			}

		}
	}

}
