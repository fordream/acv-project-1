package com.acv.rouge.rouge.fragment;

import z.base.BaseFragment;
import z.base.CommonAndroid;
import z.base.ImageLoaderUtils;
import z.base.RougeUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.db.ProductFeedTable;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.rouge.MainActivity;
import com.acv.rouge.services.RougeService;
import com.acv.rouge.services.RougeService.SERVICE_TYPE;
import com.acv.rouge.view.HeaderView;
import com.acv.rouge.view.SettingView.SettingListener;

public class HomeFragment extends BaseFragment {
	private View main;
	private ListView list;
	private HomeAdapter homeAdapter;
	private ProgressBar home_progressbar;
	private TextView no_products;
	
	private String location = "";
	private HeaderView headerView;
	private View control_pause;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String store = intent.getStringExtra(RougeUtils.KEY.STORE);
			if (store.equals(location) && getActivity() != null) {
				updateData();
			}
		}
	};

	public void reLoadData() {
		location = new SettingShareReferentDB(getActivity()).getPositionLocation() + "";
		updateData();
	};

	public void onCreate(Bundle savedInstanceState) {
		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(RougeUtils.ACTION.UPDATEFEED));
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	public HomeFragment() {
		super();
	}

	@Override
	public int getLayout() {
		return R.layout.home;
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
				updateData();
				loadData();
			}
		}
	};

	@Override
	public void init(View view) {
		main = CommonAndroid.getView(view, R.id.main);
		location = new SettingShareReferentDB(getActivity()).getPositionLocation() + "";
		headerView = CommonAndroid.getView(view, R.id.header);
		headerView.setSettingListener(new SettingListener() {

			@Override
			public void open() {
				control_pause.setVisibility(View.VISIBLE);

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

		control_pause = CommonAndroid.getView(view, R.id.control_pause);
		control_pause.setVisibility(View.GONE);
		control_pause.setOnClickListener(this);
		view.findViewById(R.id.header_control_right).setOnClickListener(this);
		view.findViewById(R.id.text_tract_your_order).setOnClickListener(this);

		home_progressbar = CommonAndroid.getView(view, R.id.home_progressbar);
		no_products = CommonAndroid.getView(view, R.id.no_products);
		no_products.setVisibility(View.GONE);

		home_progressbar.setVisibility(View.GONE);
		list = CommonAndroid.getView(view, R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String url = CommonAndroid.getString((Cursor) parent.getItemAtPosition(position), ProductFeedTable.url);
				if (!CommonAndroid.callWeb(getActivity(), url)) {
					String format = String.format(getString(R.string.error_have_not_web), url);
					CommonAndroid.showDialogComfirm(getActivity(), format);
				}
			}
		});

		list.setAdapter(homeAdapter = new HomeAdapter(getActivity()));
		loadData();
		updateData();
	}

	private void loadData() {
		Intent intent = new Intent(getActivity(), RougeService.class);
		Bundle bundle = RougeService.createBunde(SERVICE_TYPE.PRODUCT_FEED);
		bundle.putString(RougeUtils.KEY.STORE, location);

		intent.putExtras(bundle);
		getActivity().startService(intent);
	}

	protected void updateData() {
		homeAdapter.getFilter().filter("");
	}

	@Override
	public void onChangeLanguage() {

	}

	@Override
	public void onFragmentBackPress(Bundle extras) {
		super.onFragmentBackPress(extras);
		if (getActivity() != null) {
			if (!location.equals(new SettingShareReferentDB(getActivity()).getPositionLocation() + "")) {
				location = new SettingShareReferentDB(getActivity()).getPositionLocation() + "";
				updateData();
				loadData();
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.control_pause) {
			headerView.closeSetting();
			control_pause.setVisibility(View.GONE);
		} else if (v.getId() == R.id.header_control_right) {
			headerView.showSetting();
			control_pause.setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.text_tract_your_order) {
			// startFragment(new TrackYourOrderFragment(), null,
			// FragemntAnimation.RIGHT_IN);
			((MainActivity) getActivity()).openTrackOrderFromHome();
			//
			// if (getActivity() instanceof Main2Activity) {
			// ((Main2Activity) getActivity()).gotoTrackOrder();
			// }
		}
	}

	private class GotoUrlOnClickListener implements View.OnClickListener {
		private String url = "";

		public GotoUrlOnClickListener(String url) {
			this.url = url;
		}

		@Override
		public void onClick(View v) {
			CommonAndroid.callWeb(getActivity(), url);
		}
	}

	private class HomeAdapter extends CursorAdapter {

		public HomeAdapter(Context context) {
			super(context, null, true);
		}

		@Override
		public void bindView(View convertView, Context context, Cursor cursor) {
			if (convertView == null) {
				convertView = CommonAndroid.getView(context, R.layout.home_item, null);
			}
			ImageView img = CommonAndroid.getView(convertView, R.id.img_1);
			ImageLoaderUtils.getInstance(context).displayImageHome(CommonAndroid.getString(cursor, ProductFeedTable.small_image), img);

			// convertView.findViewById(R.id.ll_1).setOnClickListener(new
			// GotoUrlOnClickListener(CommonAndroid.getString(cursor,
			// ProductFeedTable.url)));

			if (getCount() - 1 == cursor.getPosition()) {
				convertView.findViewById(R.id.view_3).setVisibility(View.GONE);
			} else {
				convertView.findViewById(R.id.view_3).setVisibility(View.VISIBLE);
			}
		}

		@Override
		public Filter getFilter() {
			return new Filter() {

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					changeCursor((Cursor) results.values);
					if(((Cursor) results.values).getCount() == 0){
						no_products.setVisibility(View.VISIBLE);
					}else{
						no_products.setVisibility(View.GONE);
					}
					if (getCount() > 0) {
						home_progressbar.setVisibility(View.GONE);
					}
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();
					results.values = new ProductFeedTable(mContext).querryProductFeed(location);
					return results;
				}
			};
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup group) {
			View convertView = CommonAndroid.getView(context, R.layout.home_item, null);
			bindView(convertView, context, cursor);
			return convertView;
		}
	}
}