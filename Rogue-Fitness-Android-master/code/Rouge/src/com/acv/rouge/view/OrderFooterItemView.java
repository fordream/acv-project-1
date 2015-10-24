package com.acv.rouge.view;

import z.base.CommonAndroid;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.acv.rouge.R;
import com.acv.rouge.rouge.fragment.OrderFragment;

//com.acv.rouge.view.OrderFooterItemView
public class OrderFooterItemView extends LinearLayout implements OnClickListener {

	public OrderFooterItemView(Context context) {
		super(context);
		CommonAndroid.getView(getContext(), R.layout.order_footer, this);

		findViewById(R.id.order_footer_contactus).setOnClickListener(this);
		findViewById(R.id.order_footer_trackaneworder).setOnClickListener(this);
	}

	public OrderFooterItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(getContext(), R.layout.order_footer, this);

		findViewById(R.id.order_footer_contactus).setOnClickListener(this);
		findViewById(R.id.order_footer_trackaneworder).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.order_footer_contactus) {
			orderFragment.startContactUs();
		} else if (v.getId() == R.id.order_footer_trackaneworder) {
			orderFragment.gotoTrackNewOrder();
		}
	}

	private OrderFragment orderFragment;

	public void addOrderFragment(OrderFragment orderFragment) {
		this.orderFragment = orderFragment;
	}
}
