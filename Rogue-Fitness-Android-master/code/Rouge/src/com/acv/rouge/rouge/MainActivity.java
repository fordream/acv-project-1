package com.acv.rouge.rouge;

import java.util.ArrayList;
import java.util.List;

import z.base.BaseActivity;
import z.base.BaseFragment;
import z.base.Fonts;
import z.base.Fonts.LoadFontCallBacK;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.acv.rouge.R;
import com.acv.rouge.db.DataStore;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.db.TrackYourOrderTable;
import com.acv.rouge.rouge.fragment.ContactUsFragment;
import com.acv.rouge.rouge.fragment.HomeFragment;
import com.acv.rouge.rouge.fragment.OrderFragment;
import com.acv.rouge.rouge.fragment.TrackYourOrderFragment;
import com.acv.rouge.services.RougeService;

public class MainActivity extends BaseActivity {
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();

	private PushDialog dialogPush;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			showDialogPush(intent.getStringExtra("numberorder"), intent.getStringExtra("email"), intent.getStringExtra("alert"));
		}
	};

	protected void onPause() {
		super.onPause();
		if (listFragment.size() > 0) {
			// TODO
			DataStore.getInstance().init(this);
			DataStore.getInstance().save("f1view", (listFragment.get(1).getView().getVisibility() == View.VISIBLE));
			DataStore.getInstance().save("f1view", (listFragment.get(1).getView().getVisibility() == View.VISIBLE));
			DataStore.getInstance().save("f1view-email", (listFragment.get(1).getEmail()));
			DataStore.getInstance().save("f1view-ordersearch", (listFragment.get(1).getOrderSearch()));

			/*
			 * F2 Order
			 */
			DataStore.getInstance().save("f2view", (listFragment.get(2).getView().getVisibility() == View.VISIBLE));
			DataStore.getInstance().save("f2view-email", (listFragment.get(2).getEmail()));
			DataStore.getInstance().save("f2view-ordersearch", (listFragment.get(2).getOrderSearch()));
			DataStore.getInstance().save("f2view-order", (listFragment.get(2).getOrderId()));
			DataStore.getInstance().save("f2view-location", (listFragment.get(2).getLocation()));

			/**
			 * F3 Contact Us
			 */
			DataStore.getInstance().save("f3view", (listFragment.get(3).getView().getVisibility() == View.VISIBLE));
			DataStore.getInstance().save("f3view-ordersearch", (listFragment.get(3).getOrderSearch()));
			DataStore.getInstance().save("f3view-order", (listFragment.get(3).getOrderId()));
			DataStore.getInstance().save("f3view-email", (listFragment.get(3).getEmail()));
			DataStore.getInstance().save("f3view-location", (listFragment.get(3).getLocation()));
			DataStore.getInstance().save("f3view-question", ((ContactUsFragment) listFragment.get(3)).getQuestion());
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void showDialogPush(String order_id, String email, String alert) {
		if (dialogPush != null) {
			dialogPush.dismiss();
			dialogPush = null;
		}

		if (dialogPush == null) {
			dialogPush = new PushDialog(this, order_id, email, alert) {
				@Override
				public void openOrder(String order_id, String email, String store) {
					for (BaseFragment f : listFragment) {
						if (f.getView() != null && f.getView().getVisibility() == View.VISIBLE) {
							if (f.isOpenSetting()) {
								f.closeMenu();
							}
						}
					}
					gotoOrderDetailFromPush(email, order_id, store);
				}
			};
		}

		if (!dialogPush.isShowing()) {
			try {
				dialogPush.show();
			} catch (Exception exception) {

			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * register version
		 */
		Intent intent = new Intent(this, RougeService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.UPLOADVERSIONTOPUSH);
		intent.putExtras(extras);
		startService(intent);

		DataStore.getInstance().init(this);
		if (getFragemtCount() > 0) {
			return;
		}

		listFragment.add(new HomeFragment());
		listFragment.add(new TrackYourOrderFragment());
		listFragment.add(new OrderFragment());
		listFragment.add(new ContactUsFragment());

		Fonts.getIntance(this).loadFonts(new LoadFontCallBacK() {

			@Override
			public void onSuccess() {

				// startFragment(new HomeFragment(), null,
				// FragemntAnimation.RIGHT_IN);

				for (BaseFragment f : listFragment) {
					startFragment(f, null, FragemntAnimation.NONE);
				}

				if (getIntent().hasExtra("numberorder") && getIntent().hasExtra("email") && getIntent().hasExtra("alert")) {
					showDialogPush(getIntent().getStringExtra("numberorder"), getIntent().getStringExtra("email"), getIntent().getStringExtra("alert"));
				}
			}
		});

		registerReceiver(broadcastReceiver, new IntentFilter("com.acv.rouge.push.PushBroadcastReceiver.updateUi"));
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();

	}

	@Override
	public int getLayout() {
		return R.layout.activity_main;
	}

	@Override
	public int getResMain() {
		return R.id.frame;
	}

	private boolean isRunAnimation = false;

	@Override
	public void onBackPressed() {
		if (isRunAnimation) {
			return;
		}

		BaseFragment f1 = null, f2 = null;
		for (BaseFragment f : listFragment) {
			if (f.getView() != null && f.getView().getVisibility() == View.VISIBLE) {
				if (f.isOpenSetting()) {
					f.closeMenu();
					return;
				}
				if (f1 == null) {
					f1 = f;
				} else {
					if (f2 == null) {
						f2 = f;
					} else {
						f1 = f2;
						f2 = f;
					}
				}
			}
		}

		if (f1 == null && f2 == null) {
			finish();
			return;
		} else if (f1 != null && f2 == null) {
			finish();
			return;
		} else {
			final BaseFragment f = f2;
			isRunAnimation = true;
			AnimationListener listener = new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					f.getView().setVisibility(View.GONE);
					isRunAnimation = false;
				}
			};

			if (f1 instanceof TrackYourOrderFragment) {
				f1.reLoadData();
			} else if (f1 instanceof HomeFragment) {
				f1.reLoadData();
			}
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.right_out);
			animation.setAnimationListener(listener);
			f2.getView().startAnimation(animation);
			f1.getView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));

			return;
		}

		// super.onBackPressed();
	}

	public void openTrackOrderFromHome() {
		listFragment.get(1).getView().setVisibility(View.VISIBLE);
		listFragment.get(1).getView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
		listFragment.get(0).getView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
		listFragment.get(1).loadData("", "", "", new SettingShareReferentDB(this).getPositionLocation() + "");
	}

	public void gotoTrackNewOrderFromOrderDetail() {
		listFragment.get(1).getView().setVisibility(View.VISIBLE);
		listFragment.get(1).loadData("", "", "", new SettingShareReferentDB(this).getPositionLocation() + "");
		onBackPressed();
	}

	/**
	 * 
	 * @param email
	 * @param orderSearch
	 * @param order
	 * @param location
	 */
	public void openOrderDetail(String email, String orderSearch, String order, String location) {
		listFragment.get(2).getView().setVisibility(View.VISIBLE);
		listFragment.get(2).getView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
		listFragment.get(1).getView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
		listFragment.get(2).loadData(email, orderSearch, order, location);
		listFragment.get(2).reload();
	}

	/**
	 * 
	 * @param email
	 * @param order_id
	 * @param store
	 */
	private void gotoOrderDetailFromPush(String email, String order_id, String store) {
		listFragment.get(3).getView().setVisibility(View.GONE);
		listFragment.get(2).getView().setVisibility(View.VISIBLE);
		String orderSearch = new TrackYourOrderTable(this).getOrderSearch(email, order_id, store);
		listFragment.get(2).loadData(email, orderSearch, order_id, store);
		listFragment.get(2).reload();
	}

	public void openContactUsFromOrderDetail(String email, String search) {
		listFragment.get(3).getView().setVisibility(View.VISIBLE);
		listFragment.get(3).getView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
		listFragment.get(2).getView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
		listFragment.get(3).loadData(email, search, search, "");
	}

	public void gotoNewTrackOrderFromContactUs() {
		listFragment.get(2).getView().setVisibility(View.GONE);
		listFragment.get(1).getView().setVisibility(View.VISIBLE);
		listFragment.get(1).loadData("", "", "", new SettingShareReferentDB(this).getPositionLocation() + "");
		onBackPressed();
	}
}