package com.example.plaan;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.plaan.R;

public class Transition_animation_example extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transition_animation_example);

		((Button) findViewById(R.id.btn1))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageView img = (ImageView) findViewById(R.id.img1);
						moveViewToScreenCenter(img);
						img = (ImageView) findViewById(R.id.img2);
						moveViewToScreenCenter(img);
						img = (ImageView) findViewById(R.id.img3);
						moveViewToScreenCenter(img);
						img = (ImageView) findViewById(R.id.img4);
						moveViewToScreenCenter(img);
					}
				});
	}

	private void moveViewToScreenCenter(View view) {
		RelativeLayout root = (RelativeLayout) findViewById(R.id.rootLayout);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

		int originalPos[] = new int[2];
		view.getLocationOnScreen(originalPos);

		int xDest = dm.widthPixels / 2;
		xDest -= (view.getMeasuredWidth() / 2);
		int yDest = dm.heightPixels / 2 - (view.getMeasuredHeight() / 2)
				- statusBarOffset;

		TranslateAnimation anim = new TranslateAnimation(0, xDest
				- originalPos[0], 0, yDest - originalPos[1]);
		anim.setDuration(1000);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}
}
