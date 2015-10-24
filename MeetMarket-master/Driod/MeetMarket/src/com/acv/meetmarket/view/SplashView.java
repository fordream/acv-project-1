package com.acv.meetmarket.view;

import z.base.CommonAndroid;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.meetmarket.R;

public class SplashView extends LinearLayout {
	private View splash_btn_signin;
	private ViewPager splash_viewpager;
	private com.viewpagerindicator.CirclePageIndicator indicator;

	public SplashView(Context context) {
		super(context);
		init();
	}

	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SplashView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.splash, this);
		splash_btn_signin = CommonAndroid.getView(this, R.id.splash_btn_signin);
		splash_viewpager = CommonAndroid.getView(this, R.id.splash_viewpager);
		indicator = CommonAndroid.getView(this, R.id.indicator);

		splash_viewpager.setAdapter(new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View layout = CommonAndroid.getView(container.getContext(), R.layout.splash_item, null);
				TextView splash_item_text = CommonAndroid.getView(layout, R.id.splash_item_text);
				ImageView splash_item_img = CommonAndroid.getView(layout, R.id.splash_item_img);

				if (position == 1) {
					splash_item_text.setText(R.string.text_2);
					splash_item_img.setImageResource(R.drawable.mm_onboarding_image1);
				} else if (position == 2) {
					splash_item_text.setText(R.string.text_3);
					splash_item_img.setImageResource(R.drawable.mm_onboarding_image2);
				} else {
					splash_item_text.setText(R.string.text_4);
					splash_item_img.setImageResource(R.drawable.mm_onboarding_image3);
				}
				container.addView(layout);
				return layout;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((LinearLayout) object);
			}

			@Override
			public int getCount() {
				return 3;
			}

		});
		indicator.setViewPager(splash_viewpager);
	}

	public void setOnClickSign(OnClickListener onClickListener) {
		splash_btn_signin.setOnClickListener(onClickListener);
	}

	public void enableSignButton(boolean b) {
		//splash_btn_signin.setEnabled(b);
	}
}
