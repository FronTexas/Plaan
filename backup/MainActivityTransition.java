package com.example.plaan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivityTransition extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_transition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void showNext(View view){
		Intent intent = new Intent(this,NextActivityTransition.class);
		startActivity(intent);
		overridePendingTransition(R.anim.open_next, R.anim.close_main);
	}
	
	
	
	

}
