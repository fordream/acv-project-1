package com.acv.rouge.view;

import z.base.BaseFragment;
import z.base.CommonAndroid;
import z.base.BaseActivity.FragemntAnimation;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.rouge.fragment.HelpOrderFragment;
import com.acv.rouge.rouge.fragment.OrderFragment;

//com.acv.rouge.view.HeaderTrackOrderView
public class HeaderTrackOrderView extends LinearLayout implements OnClickListener {
	private BaseFragment baseFragment;

	public void setBaseFragment(BaseFragment baseFragment) {
		this.baseFragment = baseFragment;
	}

	public HeaderTrackOrderView(Context context) {
		super(context);
		CommonAndroid.getView(getContext(), R.layout.trackyourorder_header, this);
		init();
	}

	public HeaderTrackOrderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(getContext(), R.layout.trackyourorder_header, this);
		init();
	}

	public HeaderTrackOrderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		CommonAndroid.getView(getContext(), R.layout.trackyourorder_header, this);
		init();
	}

	private void init() {
		findViewById(R.id.img_1).setOnClickListener(this);
		findViewById(R.id.edt_trackorder_header_btn_track).setOnClickListener(this);
		TextView trackyourorder_header_text_help = CommonAndroid.getView(this, R.id.trackyourorder_header_text_help);
		String text = getContext().getString(R.string.text_1) + "<u><a href='http://learnmore.com'>Learn More</a></u>";
		Spanned textSpan = Html.fromHtml(text);

		// http://www.roguefitness.com/shippingtracker/?order=USA338603&email=nopaincrossfit%40gmail.com
		CommonAndroid.setText(this, R.id.edt_trackorder_header_email, "nopaincrossfit@gmail.com");
		CommonAndroid.setText(this, R.id.edt_trackorder_header_order_number, "USA338603");

		// http://www.roguefitness.com/shippingtracker/?order=USA438894&email=jacobjuhl%40e-isco.com
		// CommonAndroid.setText(header, R.id.edt_trackorder_header_email,
		// "=jacobjuhl@e-isco.com");
		// CommonAndroid.setText(header,
		// R.id.edt_trackorder_header_order_number, "USA438894");

		// http://www.roguefitness.com/shippingtracker/?order=USA428962&email=lawrencelee1911%40yahoo.com
		// CommonAndroid.setText(header, R.id.edt_trackorder_header_email,
		// "lawrencelee1911@yahoo.com");
		// CommonAndroid.setText(header,
		// R.id.edt_trackorder_header_order_number, "USA428962");

		trackyourorder_header_text_help.setMovementMethod(linkMovementMethod);
		trackyourorder_header_text_help.setText(textSpan);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.img_1) {

		} else if (v.getId() == R.id.edt_trackorder_header_btn_track) {
			TextView edt_trackorder_header_email = CommonAndroid.getView(this, R.id.edt_trackorder_header_email);
			final TextView edt_trackorder_header_order_number = CommonAndroid.getView(this, R.id.edt_trackorder_header_order_number);
			final String email = edt_trackorder_header_email.getText().toString();

			if (!CommonAndroid.VNPPartternChecked.isEmail(email)) {
				CommonAndroid.showDialogComfirm(getContext(), getContext().getString(R.string.error_email));
			} else {
				CommonAndroid.hiddenKeyBoard(baseFragment.getActivity());
				Bundle extras = new Bundle();
				extras.putString("email", email.trim());
				extras.putString("order_id", edt_trackorder_header_order_number.getText().toString().trim());
				extras.putString("location", new SettingShareReferentDB(getContext()).getPositionLocation() + "");
				baseFragment.startFragment(new OrderFragment(), extras, FragemntAnimation.RIGHT_IN);
			}
		}
	}

	private LinkMovementMethod linkMovementMethod = new LinkMovementMethod() {
		public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				int x = (int) event.getX();
				int y = (int) event.getY();

				x -= widget.getTotalPaddingLeft();
				y -= widget.getTotalPaddingTop();

				x += widget.getScrollX();
				y += widget.getScrollY();

				Layout layout = widget.getLayout();
				int line = layout.getLineForVertical(y);
				int off = layout.getOffsetForHorizontal(line, x);

				URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
				if (link.length != 0) {
					String url = link[0].getURL();

					if ("http://learnmore.com".equals(url)) {
						ImageView img_2 = CommonAndroid.getView(HeaderTrackOrderView.this, R.id.img_2);

						int top = img_2.getTop();

						top = top + ((View) img_2.getParent()).getTop() + HeaderTrackOrderView.this.getTop();
						top = top + (int) getResources().getDimension(R.dimen.dimen_30dp);

						Bundle extras = new Bundle();
						extras.putInt("top", top);
						baseFragment.startFragment(new HelpOrderFragment(), extras, FragemntAnimation.FADE_IN);
					}

					return true;
				}
			}

			return super.onTouchEvent(widget, buffer, event);
		}
	};

}
