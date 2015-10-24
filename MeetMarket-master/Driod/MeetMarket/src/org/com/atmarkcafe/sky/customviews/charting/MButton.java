package org.com.atmarkcafe.sky.customviews.charting;

import z.base.CommonAndroid;
import z.base.Fonts;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.acv.meetmarket.R;

//com.acv.cheerz.customviews.charting.MEditText
public class MButton extends android.widget.Button {
	private int txtFont = -1;
	private int txtTouch = -1;

	public MButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MTextView, 0, 0);

		try {
			txtFont = a.getInteger(R.styleable.MTextView_txtFont, -1);
			txtTouch = a.getInteger(R.styleable.MTextView_txtTouch, -1);
			if (txtFont != -1) {
				Fonts.getIntance(getContext()).setFont(this, txtFont);
			}

			if (txtTouch != -1) {
				CommonAndroid.setAnimationOnClick(this);
			}
		} finally {
			a.recycle();
		}

		try {
			// ((SkypeApplication)
			// getContext().getApplicationContext()).setFonts(txtFont, this);
		} catch (Exception x) {

		}
	}

}