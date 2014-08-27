package com.example.plaan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class Homepage extends Activity implements OnClickListener {
	ImageView plaanYourDayArrow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);

		this.plaanYourDayArrow = (ImageView) findViewById(R.id.plaanYourDayArrow);
		this.plaanYourDayArrow.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		// if(view.equals((TextView) findViewById(R.id.wtdwsButton))){
		Intent myIntent = new Intent(Homepage.this, ScrollingUI.class);
		Homepage.this.startActivity(myIntent);
		// }

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
