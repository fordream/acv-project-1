package com.acv.rouge.rouge.fragment;

import java.util.regex.Pattern;

import z.base.BaseFragment;
import z.base.CommonAndroid;
import z.base.RougeUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.db.DataStore;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.db.TrackYourOrderTable;
import com.acv.rouge.rouge.MainActivity;
import com.acv.rouge.services.RougeService;
import com.acv.rouge.view.HeaderView;
import com.acv.rouge.view.HeaderView.HeaderViewType;
import com.acv.rouge.view.SettingView.SettingListener;

public class TrackYourOrderFragment extends BaseFragment {
	private com.acv.rouge.view.TrackYourOrderListView list;
	private View header;
	private HeaderView headerView;
	private View control_pause;
	private TextView txt_error_order, txt_error_email;
	private View main;

	public TrackYourOrderFragment() {
	}

	@Override
	public int getLayout() {
		return R.layout.trackyourorder;
	}

	@Override
	public String getEmail() {
		return edt_trackorder_header_email.getText().toString().trim();
	}

	private EditText edt_trackorder_header_email;

	@Override
	public String getOrderSearch() {
		return ((TextView) CommonAndroid.getView(header, R.id.edt_trackorder_header_order_number)).getText().toString().trim();
	}

	@Override
	public void loadData(String email, String orderSearch, String order_id, String store) {
		super.loadData(email, orderSearch, order_id, store);
		CommonAndroid.setText(header, R.id.edt_trackorder_header_email, email);
		CommonAndroid.setText(header, R.id.edt_trackorder_header_order_number, orderSearch);

//		if (RougeUtils.ISDEBUG_ORDER) {
//			// http://www.roguefitness.com/shippingtracker/?order=USA338603&email=nopaincrossfit%40gmail.com
//			// CommonAndroid.setText(header, R.id.edt_trackorder_header_email,
//			// "nopaincrossfit@gmail.com");
//			// CommonAndroid.setText(header,
//			// R.id.edt_trackorder_header_order_number, "USA338603");
//
//			// http://www.roguefitness.com/shippingtracker/?order=USA438894&email=jacobjuhl%40e-isco.com
//			CommonAndroid.setText(header, R.id.edt_trackorder_header_email, "jacobjuhl@e-isco.com");
//			CommonAndroid.setText(header, R.id.edt_trackorder_header_order_number, "USA438894");
//
//			// http://www.roguefitness.com/shippingtracker/?order=USA428962&email=lawrencelee1911%40yahoo.com
//			CommonAndroid.setText(header, R.id.edt_trackorder_header_email, "lawrencelee1911@yahoo.com");
//			CommonAndroid.setText(header, R.id.edt_trackorder_header_order_number, "USA428962");
//		}
		adapter.getFilter().filter("");
	}

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) main.getLayoutParams();
			layout.topMargin = -0;
			main.setLayoutParams(layout);

		}
	};

	@Override
	public void init(View view) {
		view.setVisibility(View.GONE);
		main = CommonAndroid.getView(view, R.id.main);
		control_pause = CommonAndroid.getView(view, R.id.control_pause);
		control_pause.setOnClickListener(this);
		control_pause.setVisibility(View.GONE);
		headerView = CommonAndroid.getView(view, R.id.header);
		headerView.setType(HeaderViewType.TRACKYOURORDER);
		headerView.setSettingListener(new SettingListener() {

			@Override
			public void open() {
				control_pause.setVisibility(View.VISIBLE);
				CommonAndroid.hiddenKeyBoard(getActivity());

				RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) main.getLayoutParams();
				layout.topMargin = -(int) getActivity().getResources().getDimension(R.dimen.dimen_3dp);
				main.setLayoutParams(layout);
			}

			@Override
			public void close() {
				control_pause.setVisibility(View.GONE);
				handler.sendEmptyMessageDelayed(0, 10);
			}
		});

		list = CommonAndroid.getView(view, R.id.listtrackyourorder);

		view.findViewById(R.id.header_control_right).setOnClickListener(this);
		view.findViewById(R.id.header_control_left).setOnClickListener(this);

		header = CommonAndroid.getView(getActivity(), R.layout.trackyourorder_header, null);
		header.findViewById(R.id.img_1).setOnClickListener(this);

		edt_trackorder_header_email = CommonAndroid.getView(header, R.id.edt_trackorder_header_email);

		txt_error_order = CommonAndroid.getView(header, R.id.txt_error_order);
		txt_error_email = CommonAndroid.getView(header, R.id.txt_error_email);
		// height = '+1px' style='background-color:#E6E6FA' 
		String text = getString(R.string.text_1) + " <a href='http://learnmore.com'> <u>Learn More</u>&nbsp;  </a>";
		Spanned textSpan = Html.fromHtml(text);
		TextView trackyourorder_header_text_help = CommonAndroid.getView(header, R.id.trackyourorder_header_text_help);
		trackyourorder_header_text_help.setMovementMethod(linkMovementMethod);
		trackyourorder_header_text_help.setText(textSpan);

		ImageSpan is = new ImageSpan(getActivity(), R.drawable.rogue_icon_learnmore);
		SpannableString s = new SpannableString(Html.fromHtml(text));
		s.setSpan(is, s.toString().length() - 1, 0 + s.toString().length(), 0);
		trackyourorder_header_text_help.setText(s);

		final TextView edt_trackorder_header_order_number = CommonAndroid.getView(header, R.id.edt_trackorder_header_order_number);
		final TextView edt_trackorder_header_order_number_text = CommonAndroid.getView(header, R.id.edt_trackorder_header_order_number_text);

		edt_trackorder_header_order_number.addTextChangedListener(new TextWatcher() {
			int length = -1;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				length = edt_trackorder_header_order_number.getText().toString().length();
			}

			@Override
			public void afterTextChanged(Editable s) {

				edt_trackorder_header_order_number_text.setText(edt_trackorder_header_order_number.getText().toString().toUpperCase());
			}
		});

		header.findViewById(R.id.edt_trackorder_header_btn_track).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_error_email.setVisibility(View.GONE);
				txt_error_order.setVisibility(View.GONE);
				TextView edt_trackorder_header_email = CommonAndroid.getView(header, R.id.edt_trackorder_header_email);
				final TextView edt_trackorder_header_order_number = CommonAndroid.getView(header, R.id.edt_trackorder_header_order_number);
				final String email = edt_trackorder_header_email.getText().toString();
				String search = edt_trackorder_header_order_number.getText().toString().toUpperCase();
				boolean canTrack = true;
				if (CommonAndroid.isBlank(search)) {
					txt_error_order.setText("Please Enter a Valid Order Number");
					txt_error_order.setVisibility(View.VISIBLE);
					canTrack = false;
				}
				Pattern p = Pattern.compile("[^a-zA-Z0-9]");
				boolean hasSpecialChar = p.matcher(search).find();
				if(hasSpecialChar){
					txt_error_order.setText("Please use only alphanumeric characters");
					txt_error_order.setVisibility(View.VISIBLE);
					canTrack = false;
				}
				if (!CommonAndroid.VNPPartternChecked.isEmail(email)) {
					txt_error_email.setVisibility(View.VISIBLE);
					canTrack = false;
				}

				if (canTrack) {
					CommonAndroid.hiddenKeyBoard(getActivity());

					Bundle extras = new Bundle();
					extras.putString("email", email.trim());
					extras.putString(TrackYourOrderTable.ordersearch, search);
					extras.putString(TrackYourOrderTable.orderNumber, "");
					extras.putString("location", new SettingShareReferentDB(getActivity()).getPositionLocation() + "");

					((MainActivity) getActivity()).openOrderDetail(email.trim(), search, "", new SettingShareReferentDB(getActivity()).getPositionLocation() + "");

				}
			}
		});
		if (DataStore.getInstance().get("f1view", false)) {
			view.setVisibility(View.VISIBLE);

			CommonAndroid.setText(header, R.id.edt_trackorder_header_email, DataStore.getInstance().get("f1view-email", ""));
			CommonAndroid.setText(header, R.id.edt_trackorder_header_order_number, DataStore.getInstance().get("f1view-ordersearch", ""));
		}

		list.addHeaderView(header);
		list.setAdapter(adapter = new TrackYourOrderAdapter(getActivity(), null));
		adapter.getFilter().filter("");
	}

	@Override
	public void onFragmentBackPress(Bundle extras) {
		super.onFragmentBackPress(extras);
		list.setAdapter(adapter = new TrackYourOrderAdapter(getActivity(), null));
		adapter.getFilter().filter("");
	}

	public void reLoadData() {
		adapter.getFilter().filter("");
	}

	@Override
	public void onChangeLanguage() {
		list.setAdapter(adapter = new TrackYourOrderAdapter(getActivity(), null));
		adapter.getFilter().filter("");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.control_pause) {
			headerView.closeSetting();
		} else if (v.getId() == R.id.header_control_right) {
			headerView.showSetting();
		} else if (v.getId() == R.id.header_control_left) {

			getActivity().onBackPressed();
		}
	}

	private class TrackYourOrderItemView extends LinearLayout implements View.OnClickListener {
		private boolean isOpen = false;
		private RelativeLayout rl_1;

		public TrackYourOrderItemView(Context context) {
			super(context);
			CommonAndroid.getView(getContext(), R.layout.trackyourorder_item, this);

			rl_1 = CommonAndroid.getView(this, R.id.rl_1);
			findViewById(R.id.rl_2).setOnTouchListener(createTouch());
			findViewById(R.id.img_2).setOnClickListener(this);
			findViewById(R.id.img_2).setClickable(false);
		}

		@SuppressLint("ClickableViewAccessibility")
		private OnTouchListener createTouch() {
			return new OnTouchListener() {
				private int startX = 0, firstStartX = 0;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					int eventX = (int) event.getX();
					int max = (int) getResources().getDimension(R.dimen.dimen_45dp);
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						findViewById(R.id.rl_1).setBackgroundColor(Color.parseColor("#555555"));
						z.base.text.BoldTextView text =  (z.base.text.BoldTextView)findViewById(R.id.text_1);
						text.setTextColor(Color.parseColor("#f1f1f1"));
						startX = eventX;
						firstStartX = eventX;
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						if (startX == -1000) {
							startX = eventX;
							firstStartX = eventX;
						}

						int dx = startX - eventX;

						if (Math.abs(dx) > 10) {
							list.sendEnableScroll(false);
							startX = eventX;

							RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_1.getLayoutParams();
							params.rightMargin = params.rightMargin + dx;

							if (params.rightMargin < 0) {
								params.rightMargin = 0;
							} else if (params.rightMargin >= max) {
								params.rightMargin = max;
							}

							rl_1.setLayoutParams(params);
						}

					} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
						findViewById(R.id.rl_1).setBackgroundColor(Color.parseColor("#f1f1f1"));
						z.base.text.BoldTextView text =  (z.base.text.BoldTextView)findViewById(R.id.text_1);
						text.setTextColor(Color.parseColor("#555555"));
						list.sendEnableScroll(true);
						startX = -1000;
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_1.getLayoutParams();

						if (firstStartX == eventX) {
							if (!isOpen) {
								// Bundle extras = new Bundle();
								// extras.putString(TrackYourOrderTable.orderNumber,
								// xid);
								//
								// extras.putString(TrackYourOrderTable.ordersearch,
								// ordersearch);
								// extras.putString("email", email);
								// extras.putString("location", location);
								((MainActivity) getActivity()).openOrderDetail(email.trim(), ordersearch, xid, location);
								// startFragment(new OrderFragment(), extras,
								// FragemntAnimation.RIGHT_IN);
								//
								//
								//
								// if (getActivity() instanceof Main2Activity) {
								// ((Main2Activity)
								// getActivity()).openOrderDetail(email.trim(),
								// ordersearch, new
								// SettingShareReferentDB(getActivity()).getPositionLocation()
								// + "", xid);
								// }
							}
						}

						if (params.rightMargin <= max / 2) {
							params.rightMargin = 0;
							findViewById(R.id.img_2).setClickable(false);
						} else if (params.rightMargin >= max / 2) {
							params.rightMargin = max;
							findViewById(R.id.img_2).setClickable(true);
						}

						rl_1.setLayoutParams(params);

						if (params.rightMargin == max) {
							isOpen = true;
						} else {
							isOpen = false;
						}
					}

					return true;
				}
			};
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.img_2) {
				if (isOpen) {
					CommonAndroid.showDialogOkcancel(getContext(), getString(R.string.remove_order), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							close();
							adapter.remove(xid, ordersearch, location, email);

							/**
							 * update register praser
							 */
							RougeService.pushUpdateTrackOrderListToParse(getContext());
						}
					});
				}
			}
		}

		protected void close() {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_1.getLayoutParams();
			params.rightMargin = 0;
			rl_1.setLayoutParams(params);
		}

		public void setItem(Cursor cursor, TrackYourOrderAdapter adapter) {
			xid = CommonAndroid.getString(cursor, TrackYourOrderTable.orderNumber);
			email = CommonAndroid.getString(cursor, TrackYourOrderTable.email);
			location = CommonAndroid.getString(cursor, TrackYourOrderTable.storeSettingOnApp);
			ordersearch = CommonAndroid.getString(cursor, TrackYourOrderTable.ordersearch);
			this.adapter = adapter;

			((TextView) findViewById(R.id.text_1)).setText(ordersearch);
		}

		private String ordersearch;
		private String xid;
		private String email;
		private String location;
		private TrackYourOrderAdapter adapter;
	}

	/**
	 * 
	 */

	private TrackYourOrderAdapter adapter;

	private class TrackYourOrderAdapter extends CursorAdapter {

		public TrackYourOrderAdapter(Context context, Cursor c) {
			super(context, c, true);
		}

		public void remove(String orderNumber, String ordersearch, String location, String email) {
			new TrackYourOrderTable(mContext).removeTrackOrder(orderNumber, ordersearch, location, email);
			getFilter().filter("");
		}

		@Override
		public Filter getFilter() {
			return new Filter() {

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					changeCursor((Cursor) results.values);
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();
					results.values = new TrackYourOrderTable(mContext).querryNoDelete();
					return results;
				}
			};
		}

		@Override
		public void bindView(View convertView, Context context, Cursor cursor) {
			if (convertView == null) {
				convertView = new TrackYourOrderItemView(context);
			}

			((TrackYourOrderItemView) convertView).setItem(cursor, this);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup arg2) {
			TrackYourOrderItemView convertView = new TrackYourOrderItemView(context);
			((TrackYourOrderItemView) convertView).setItem(cursor, this);
			return convertView;
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
						ImageView img_2 = CommonAndroid.getView(header, R.id.img_2);

						int top = img_2.getTop();

						top = top + ((View) img_2.getParent()).getTop() + header.getTop();
						top = top + (int) getResources().getDimension(R.dimen.dimen_30dp);

						Bundle extras = new Bundle();
						extras.putInt("top", top);
						// Intent intent = new Intent(getActivity(),
						// YoutubeActivity.class);
						// intent.putExtras(extras);
						// getActivity().startActivity(intent);

						// Intent ac = new Intent(Intent.ACTION_VIEW);

						getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=_UfFYjUcIaI")));
						// startFragment(new HelpOrderFragment(), extras,
						// FragemntAnimation.FADE_IN);
					}

					return true;
				}
			}

			return super.onTouchEvent(widget, buffer, event);
		}
	};

	public void mReload() {
		if (adapter != null)
			adapter.getFilter().filter("");
	}

}
