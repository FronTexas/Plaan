package com.example.plaan;

import android.app.Activity;
import android.os.Bundle;

public class NextActivityTransition extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next_activity_transition);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.open_main,R.anim.close_next);
	}
	
	
	
	

}
