package com.example.plaan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class FirstPage extends Activity {

	TextView tvPlaan;
	TypefacePlaan tfp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first_page);

		tvPlaan = (TextView) findViewById(R.id.tvPlaan);

		tfp = new TypefacePlaan(getApplicationContext());
		tfp.setTypeface(tvPlaan, TypefacePlaan.PACIFICO);

		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					//					Intent i = new Intent(FirstPage.this, ScrollingUI.class);
					//					startActivity(i);
					Intent i = new Intent(FirstPage.this, CalendarActivity.class);
					startActivity(i);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
