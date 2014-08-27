package com.example.plaan;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TodoBar extends LinearLayout {
	EditText etTodo;

	public TodoBar(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addView(inflater.inflate(R.layout.todo_yellow_bar, null));
		etTodo = (EditText) findViewById(R.id.etTodoText);
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(etTodo, TypefacePlaan.LEAGUE_GOTHIC);
		etTodo.isInEditMode();
		etTodo.setEllipsize(TruncateAt.END);
	}
}
