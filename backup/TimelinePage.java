package com.example.plaan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimelinePage extends Activity {
	RelativeLayout rlPlaanText;
	TextView tvPlaanText;
	RelativeLayout rlMainContent;
	RelativeLayout rlActivityGroup;
	TextView tvTime;
	ImageView ivYellowCircle;
	ImageView ivGreyLine;
	RelativeLayout rlActivitySetter;

	TextView tvInterval;
	RelativeLayout rlActivitySleep;
	TextView tvAddActivities;
	RelativeLayout rlSTART;
	TextView tvSTART;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline_page);

		initializeViews();

	}

	private void initializeViews() {

	}

}
