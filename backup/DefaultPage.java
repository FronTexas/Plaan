package com.example.plaan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DefaultPage extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultpage);
		Button testing = (Button) findViewById(R.id.testingButton);
		testing.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		
	}
	
}
