package org.com.atmarkcafe.sky.customviews.charting;

import z.base.CommonAndroid;
import z.base.Fonts;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.acv.meetmarket.R;

//org.com.atmarkcafe.sky.customviews.charting.MTextView
public class MTextView extends android.widget.TextView {
	private int txtFont = -1;
	private int txtTouch = -1;

	public MTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MTextView, 0, 0);

			try {
				txtFont = a.getInteger(R.styleable.MTextView_txtFont, -1);
				txtTouch = a.getInteger(R.styleable.MTextView_txtTouch, -1);

				if (txtFont != -1) {
					Fonts.getIntance(getContext()).setFont(this, txtFont);
				}

				if (txtTouch == 1) {
					CommonAndroid.setAnimationOnClick(this);
				}

				if (txtTouch != -1) {
					CommonAndroid.setAnimationOnClick(this);
				}
			} finally {
				a.recycle();
			}

		} catch (Exception x) {

		}

	}

}