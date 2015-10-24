package com.acv.rouge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

//com.acv.rouge.view.CapitalizedTextView
public class CapitalizedTextView extends EditText {

	public CapitalizedTextView(Context context) {
		super(context);
	}

	public CapitalizedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CapitalizedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setText(CharSequence text, BufferType type) {
		super.setText(text.toString().toUpperCase(), type);
	}

}