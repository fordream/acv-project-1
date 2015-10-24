package com.acv.rouge.rouge.fragment;

import z.base.BaseFragment;
import z.base.CommonAndroid;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.db.DataStore;
import com.acv.rouge.db.PackagesTable;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.db.TrackYourOrderTable;
import com.acv.rouge.rouge.MainActivity;
import com.acv.rouge.services.api.RequestMethod;
import com.acv.rouge.services.api.RestClient;
import com.acv.rouge.services.api.TrackOrderRestClientCallBack;
import com.acv.rouge.view.HeaderView;
import com.acv.rouge.view.HeaderView.HeaderViewType;
import com.acv.rouge.view.OrderFooterItemView;
import com.acv.rouge.view.OrderItemFullView;
import com.acv.rouge.view.SettingView.SettingListener;

public class OrderFragment extends BaseFragment {
	// private ListView list;
	private OrderFooterItemView orderfooteritem;
	private LinearLayout list_order_detail;
	private LinearLayout order_loader;
	private ProgressBar order_progessbar;
	private TextView order_error_message;
	private String location = "";
	private HeaderView headerView;
	private View control_pause;
	private View main;

	@SuppressWarnings("unused")
	public OrderFragment() {
		super();
	}

	@Override
	public int getLayout() {
		return R.layout.order;
	}

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) main.getLayoutParams();
			layout.topMargin = -0;
			main.setLayoutParams(layout);
			if (!location.equals(new SettingShareReferentDB(getActivity()).getPositionLocation() + "")) {
				location = new SettingShareReferentDB(getActivity()).getPositionLocation() + "";
				loadData(email, search, order, location);
			}
		}
	};

	@Override
	public void init(View view) {
		view.setVisibility(View.GONE);
		main = CommonAndroid.getView(view, R.id.main);
		control_pause = CommonAndroid.getView(view, R.id.control_pause);
		control_pause.setOnClickListener(this);
		control_pause.setVisibility(View.GONE);
		view.findViewById(R.id.header_control_right).setOnClickListener(this);
		view.findViewById(R.id.header_control_left).setOnClickListener(this);
		order_error_message = CommonAndroid.getView(view, R.id.order_error_message);
		order_loader = CommonAndroid.getView(view, R.id.order_loader);
		order_progessbar = CommonAndroid.getView(view, R.id.order_progessbar);
		headerView = CommonAndroid.getView(view, R.id.header);
		headerView.setType(HeaderViewType.ORDER, getOrderSearch());
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
		orderfooteritem = CommonAndroid.getView(view, R.id.orderfooteritem);
		orderfooteritem.addOrderFragment(this);
		list_order_detail = CommonAndroid.getView(view, R.id.list_order_detail);

		location = getLocation();
		if (DataStore.getInstance().get("f2view", false)) {
			view.setVisibility(View.VISIBLE);
			location = DataStore.getInstance().get("f2view-location", "");
			email = DataStore.getInstance().get("f2view-email", "");
			location2 = DataStore.getInstance().get("f2view-location", "");
			order = DataStore.getInstance().get("f2view-order", "");
			search = DataStore.getInstance().get("f2view-ordersearch", "");

			loadData(email, search, order, location);
		}
	}

	private void updateUi() {
		headerView.setType(HeaderViewType.ORDER, getOrderSearch());
		try {
			list_order_detail.removeAllViews();
			String idTrackOrder = new TrackYourOrderTable(getActivity()).get_IdTrackOrderNowNoDelete(getEmail(), getOrderId(), getOrderSearch(), location);

			Cursor cursor = new PackagesTable(getActivity()).loadPackage(idTrackOrder, location);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					OrderItemFullView itemFullView = new OrderItemFullView(getActivity());
					itemFullView.init(cursor, location);
					list_order_detail.addView(itemFullView);
				}

				cursor.close();
			}

			if (list_order_detail.getChildCount() > 0) {
				order_loader.setVisibility(View.GONE);
			}
		} catch (Exception exception) {

		}
	}

	@Override
	public void loadData(String email, String orderSearch, String order_id, String store) {
		super.loadData(email, orderSearch, order_id, store);
		location2 = store;
		location = store;
		this.search = orderSearch;
		this.email = email;
		this.order = order_id;
		updateUi();
		loadData();

	}

	public void startContactUs() {

		((MainActivity) getActivity()).openContactUsFromOrderDetail(email, getOrderSearch());
	}

	private void loadData() {
		RestClient client = new RestClient("getInfoByNumberAndEmail", getActivity());
		client.addParam("order_number", getOrderSearch());
		client.addParam("email", getEmail());
		client.addParam("token", "7KAh3EPZqXWLxpqgKZ8PmVt");
		client.execute(RequestMethod.GET, new TrackOrderRestClientCallBack(getActivity(), location, getOrderSearch()) {
			@Override
			public void onStart() {
				super.onStart();
				if (list_order_detail.getChildCount() == 0) {
					order_loader.setVisibility(View.VISIBLE);
					order_progessbar.setVisibility(View.VISIBLE);
					order_error_message.setText("");
				} else {
					order_loader.setVisibility(View.GONE);
					order_progessbar.setVisibility(View.GONE);
					order_error_message.setText("");
				}
			}

			public void onSucsses(int responseCode, String responseMessage, String response) {
				super.onSucsses(responseCode, responseMessage, response);
				updateUi();
				if (list_order_detail.getChildCount() == 0) {
					order_loader.setVisibility(View.VISIBLE);
					order_progessbar.setVisibility(View.GONE);
					order_error_message.setText(responseMessage);
				} else {
					order_loader.setVisibility(View.GONE);
					order_progessbar.setVisibility(View.GONE);
					order_error_message.setText("");
				}
			};
		}, false);
	}

	@Override
	public void onFragmentBackPress(Bundle extras) {
		super.onFragmentBackPress(extras);
		if (!location.equals(new SettingShareReferentDB(getActivity()).getPositionLocation() + "")) {
			location = new SettingShareReferentDB(getActivity()).getPositionLocation() + "";
			updateUi();
			loadData();
		}
	}

	@Override
	public void onChangeLanguage() {

	}

	@Override
	public void reload() {
		super.reload();
		ScrollView scrollview = CommonAndroid.getView(getView(), R.id.scrollview);
		if (scrollview != null) {
			scrollview.scrollTo(0, 0);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.control_pause) {
			headerView.closeSetting();
		} else if (v.getId() == R.id.header_control_right) {
			headerView.showSetting();
		} else if (v.getId() == R.id.img_1) {
		} else if (v.getId() == R.id.header_control_left) {
			getActivity().onBackPressed();
		}
	}

	public void load(String email, String search, String location2, String order_number) {
		this.email = email;
		this.location2 = location2;
		location = location2;
		this.search = search;
		this.order = order_number;

		updateUi();
		loadData();
	}

	private String email = "", location2 = "", search = "", order = "";

	public String getEmail() {

		return email;
	}

	public String getOrderId() {
		return order;
	}

	public String getLocation() {
		return location2;

	}

	public String getOrderSearch() {
		return search;

	}

	public void gotoTrackNewOrder() {
		((MainActivity) getActivity()).gotoTrackNewOrderFromOrderDetail();
	}
}