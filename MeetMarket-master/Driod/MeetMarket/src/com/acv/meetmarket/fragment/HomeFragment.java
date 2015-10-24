package com.acv.meetmarket.fragment;

import z.base.CommonAndroid;
import z.base.MeetMarketBaseFragment;
import z.base.MeetMarketUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;

import com.acv.meetmarket.MainActivity;
import com.acv.meetmarket.R;
import com.acv.meetmarket.db.HomeTable;
import com.acv.meetmarket.service.MeetMarketService;
import com.acv.meetmarket.service.MeetMarketService.TypeMeetMarketService;
import com.acv.meetmarket.view.HomeItemView;

public class HomeFragment extends MeetMarketBaseFragment {

	private ListView list;
	private HomeAdapter adapter;

	@Override
	public void init(View view) {
		super.init(view);
		list = CommonAndroid.getView(view, R.id.list);
		HomeItemView homeItemView = new HomeItemView(getActivity());
		list.addFooterView(homeItemView);

		homeItemView.setOnClickListener(new AddNewMeeting());

		if (adapter == null) {
			adapter = new HomeAdapter(getActivity());
		}

		list.setAdapter(adapter);
		adapter.getFilter().filter("");

		/**
		 * add example
		 */

		Intent intent = new Intent(getActivity(), MeetMarketService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(MeetMarketUtils.KEY.TYPE, TypeMeetMarketService.HOME);
		intent.putExtras(extras);
		getActivity().startService(intent);
	}

	@Override
	public void onDestroy() {
		if (adapter.getCursor() != null)
			adapter.getCursor().close();
		super.onDestroy();
	}

	private class HomeAdapter extends CursorAdapter {

		public HomeAdapter(Context context) {
			super(context, null, true);
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			if (arg0 == null) {
				arg0 = new HomeItemView(arg1);
			}

			((HomeItemView) arg0).initData(arg2);
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {

			HomeItemView homeItemView = new HomeItemView(arg0);
			bindView(homeItemView, arg0, arg1);
			return homeItemView;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					changeCursor((Cursor) results.values);
					notifyDataSetChanged();
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();

					results.values = new HomeTable(getActivity()).querryAllDataOfUser("truongvv@sinhvu.com");
					return results;
				}
			};
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter(MeetMarketUtils.ACTION.UPDATE_HOME));
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (adapter != null) {
				adapter.getFilter().filter("");
			}
		}
	};

	private class AddNewMeeting implements OnClickListener {

		@Override
		public void onClick(View v) {
			((MainActivity) getActivity()).createNewMeeting();
		}
	}

	@Override
	public int getLayout() {
		return R.layout.home;
	}
}
