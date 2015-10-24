package com.acv.meetmarket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.BaseActivity;
import z.base.CommonAndroid;
import z.base.Fonts;
import z.base.LogUtils;
import z.base.Fonts.LoadFontCallBacK;
import z.base.MeetMarketBaseFragment;
import z.base.MeetMarketUtils;
import z.base.VNPLocationUtils;
import z.base.api.RequestMethod;
import z.base.api.RestClient;
import z.base.api.RestClientCallBack;
import z.base.item.MapData;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acv.meetmarket.db.DataStore;
import com.acv.meetmarket.db.HomeTable;
import com.acv.meetmarket.db.UserTable;
import com.acv.meetmarket.dialog.F36;
import com.acv.meetmarket.fragment.CreateANewMeetingFragment;
import com.acv.meetmarket.fragment.HomeFragment;
import com.acv.meetmarket.fragment.ProfileFragment;
import com.acv.meetmarket.service.MeetMarketService;
import com.acv.meetmarket.service.MeetMarketService.TypeMeetMarketService;
import com.acv.meetmarket.view.MenuView;
import com.acv.meetmarket.view.SplashView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import eu.janmuller.android.simplecropimage.CropImage;
import eu.janmuller.android.simplecropimage.example.InternalStorageContentProvider;

public class MainActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout map_border;
	private TextView mapchoose_choose;
	private TextView mapchoose_text;
	private F36 f36;
	private BroadcastReceiver broadcastReceiverPush = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			HomeTable table = new HomeTable(MainActivity.this);
			String userId = new UserTable(MainActivity.this).getUserId();
			String _id = table.getLastPushItem(userId);
			showPush(_id);
		}
	};
	private BroadcastReceiver loginBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {


			if (intent.getExtras().getBoolean("login")) {
				handler.sendEmptyMessage(HANDLER_START);
			} else {
				CommonAndroid.showDialogComfirm(MainActivity.this, getString(R.string.error_login));
			}
		}
	};

	private void showPush(String _id) {
		if (f36 == null) {
			f36 = new F36(MainActivity.this) {
				public void onClickOkie(String _id2) {
					super.onClickOkie(_id2);
				};
			};
		} else if (!f36.isShowing()) {
			f36 = new F36(MainActivity.this);
		}

		if (!f36.isShowing() && !CommonAndroid.isBlank(_id)) {
			f36.updateId(_id);
			f36.show();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			if (msg.what == HANDLER_SCREEN_INFOR) {
				list.get(1).getView().setVisibility(View.VISIBLE);
				((MeetMarketBaseFragment) list.get(1)).onShowData();
				header_menu.setVisibility(View.GONE);
				list.get(2).getView().setVisibility(View.GONE);
				header_notification.setImageResource(R.drawable.mm_header_icon_button);
			} else if (msg.what == HANDLER_LOGIN) {
				handler.sendEmptyMessage(HANDLER_GETINFOR);
			} else if (msg.what == HANDLER_MESSAGE) {
				CommonAndroid.showDialogComfirm(MainActivity.this, msg.obj.toString());
			} else if (HANDLER_GETINFOR == msg.what) {
				getInfor();
			} else if (HANDLER_START == msg.what) {
				loginSucess();
			} else if (HANDLER_MENU == msg.what) {
				int position = msg.arg1;
				if (position == 1) {
					for (int i = 0; i < list.size(); i++) {
						if (i > 1) {
							list.get(i).getView().setVisibility(View.GONE);
						}
					}

					((MeetMarketBaseFragment) list.get(2)).onShowData();
				} else if (position == 0) {
					for (int i = 0; i < list.size(); i++) {
						list.get(i).getView().setVisibility(View.GONE);
					}
					list.get(0).getView().setVisibility(View.VISIBLE);
				}
			}
		}
	};
	private static final int HANDLER_SCREEN_INFOR = 5;
	private static final int HANDLER_MESSAGE = -2;
	private static final int HANDLER_LOGIN = -1;
	private static final int HANDLER_GETINFOR = -3;
	private static final int HANDLER_START = -4;
	private static final int HANDLER_MENU = 0;
	private List<Fragment> list = new ArrayList<Fragment>();

	public static final String PACKAGE_MOBILE_SDK_SAMPLE_APP = "com.acv.meetmarket";

	private void loginSucess() {
		main.setVisibility(View.VISIBLE);
		for (Fragment fragment : list) {
			startFragment(fragment, null, FragemntAnimation.NONE);
		}

		if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("push")) {
			Intent intent = new Intent(MeetMarketUtils.ACTION.UPDATEUIBYPUSH);
			sendBroadcast(intent);
		}
	}

	private void getInfor() {
		Intent intent = new Intent(this, MeetMarketService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(MeetMarketUtils.KEY.TYPE, TypeMeetMarketService.GETINFOR);
		intent.putExtras(extras);
		startService(intent);
		// TODO
		// StringBuilder url = new StringBuilder();
		// url.append("https://api.linkedin.com/v1/people/~");
		// url.append(":(");
		// url.append("id");
		// url.append(",first-name");
		// url.append(",last-name");
		// url.append(",picture-url");
		// url.append(",skills");
		// url.append(",headline");
		// url.append(",publications");
		// url.append(",last-modified-timestamp");
		// url.append(",proposal-comments");
		// url.append(",associations");
		// url.append(",interests");
		// url.append(",patents");
		// url.append(",languages");
		// url.append(",certifications");
		// url.append(",educations");
		// url.append(",date-of-birth");
		// url.append(")");

//		APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
//
//		apiHelper.getRequest(MainActivity.this, url.toString(), new ApiListener() {
//
//			@Override
//			public void onApiSuccess(ApiResponse response) {
//				JSONObject object = response.getResponseDataAsJson();
//				DataStore.getInstance().init(MainActivity.this);
//				DataStore.getInstance().save("headline", CommonAndroid.getString(object, "headline"));
//				DataStore.getInstance().save("lastName", CommonAndroid.getString(object, "lastName"));
//				DataStore.getInstance().save("firstName", CommonAndroid.getString(object, "firstName"));
//				DataStore.getInstance().save("id", CommonAndroid.getString(object, "id"));
//				if (CommonAndroid.isBlank(DataStore.getInstance().get("pictureUrl", "")))
//					DataStore.getInstance().save("pictureUrl", CommonAndroid.getString(object, "pictureUrl"));
//				try {
//					JSONArray skills = object.getJSONObject("skills").getJSONArray("values");
//					if (CommonAndroid.isBlank(DataStore.getInstance().get("skills", "")))
//						DataStore.getInstance().save("skills", skills.toString());
//				} catch (Exception e) {
//				}
//
//				handler.sendEmptyMessage(HANDLER_START);
//			}
//
//			@Override
//			public void onApiError(LIApiError error) {
//			}
//		});
	}

	public void onBackPressed() {
		CommonAndroid.hiddenKeyBoard(this);

		if (drawer_layout.isDrawerOpen(Gravity.START) || drawer_layout.isDrawerOpen(Gravity.END)) {
			animationActionMenuChange(drawer_layout.isDrawerOpen(Gravity.END), false);
			return;
		}

		if (map_border.getVisibility() == View.VISIBLE) {
			map_border.setVisibility(View.GONE);

			return;
		}

		if (((MeetMarketBaseFragment) list.get(1)).isShow()) {
			updateProfile();
			header_menu.setVisibility(View.VISIBLE);
			header_notification.setImageResource(R.drawable.mm_header_icon_chat);
			((MeetMarketBaseFragment) list.get(1)).hiddenView();
			return;
		} else if (((MeetMarketBaseFragment) list.get(2)).isShow()) {
			// ((MeetMarketBaseFragment) list.get(2)).hiddenView();
			// return;
		}

		finish();
		return;
	}

	private void updateProfile() {
		String abount = ((ProfileFragment) list.get(1)).getAbout();
		DataStore.getInstance().save("about", abount);
		DataStore.getInstance().save("skills", ((ProfileFragment) list.get(1)).getSkills().toString());
		Intent intent = new Intent(this, MeetMarketService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(MeetMarketUtils.KEY.TYPE, TypeMeetMarketService.UPDATEINTORTOPARSE);
		intent.putExtras(extras);
		startService(intent);
	}

	private LoadFontCallBacK fontCallBacK = new LoadFontCallBacK() {

		@Override
		public void onSuccess() {
			if (CommonAndroid.isLogin(MainActivity.this)) {
				handler.sendEmptyMessage(HANDLER_LOGIN);
			} else {
				addSplash();
			}
		}
	};

	@Override
	public int getLayout() {
		return R.layout.main;
	}

	private void addSplash() {
		splashView = new SplashView(MainActivity.this);
		main_splash.addView(splashView);
		splashView.setOnClickSign(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				splashView.enableSignButton(false);
				Scope scope = Scope.build(Scope.R_FULLPROFILE, Scope.R_CONTACTINFO, Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.RW_COMPANY_ADMIN);
				LISessionManager.getInstance(getApplicationContext()).init(MainActivity.this, scope, new AuthListener() {

					@Override
					public void onAuthSuccess() {
						handler.sendEmptyMessage(HANDLER_LOGIN);
					}

					@Override
					public void onAuthError(LIAuthError error) {
						Message msg = new Message();
						msg.what = HANDLER_MESSAGE;
						msg.obj = getString(R.string.error_login);
						handler.sendMessage(msg);
					}
				}, true);

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.header_notification) {

			if (map_border.getVisibility() == View.VISIBLE) {
				map_border.setVisibility(View.GONE);

				header_menu.setVisibility(View.VISIBLE);
				header_notification.setImageResource(R.drawable.mm_header_icon_chat);
				return;
			}
			if (((MeetMarketBaseFragment) list.get(1)).isShow()) {
				onBackPressed();
				return;
			}

			if (drawer_layout.isDrawerOpen(Gravity.END) || drawer_layout.isDrawerOpen(Gravity.START)) {
			} else {
			}
		} else if (v.getId() == R.id.header_menu) {

			if (((MeetMarketBaseFragment) list.get(1)).isShow()) {
				return;
			}

			if (drawer_layout.isDrawerOpen(Gravity.END) || drawer_layout.isDrawerOpen(Gravity.START)) {
				animationActionMenuChange(true, false);
			} else {
				menuview.initData(onItemClickListener, clickListener);
				animationActionMenuChange(true, true);
			}
		} else if (v.getId() == R.id.mapchoose_choose) {
			// TODO show map
			String mapText = mapchoose_text.getText().toString();
			if (CommonAndroid.isBlank(mapText)) {
				CommonAndroid.showDialogComfirm(this, getString(R.string.error_choose_location));
				return;
			}

			((CreateANewMeetingFragment) list.get(2)).addLocation(xlat, xlng, mapText);
			onBackPressed();
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			animationActionMenuChange(true, false);
			handler.sendEmptyMessageDelayed(HANDLER_SCREEN_INFOR, 200);
		}
	};
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			animationActionMenuChange(true, false);

			Message message = new Message();
			message.what = HANDLER_MENU;
			message.arg1 = position;
			handler.sendMessageDelayed(message, 200);
		}
	};
	private boolean animationRun = false;

	private void animationActionMenuChange(final boolean isMenu, final boolean isOpen) {
		if (animationRun) {
			return;
		}
		animationRun = true;
		if (!isOpen) {
			drawer_layout.closeDrawers();
		} else {
			drawer_layout.openDrawer(isMenu ? Gravity.END : Gravity.START);
		}

		long duration = 200;
		AlphaAnimation animation = isOpen ? new AlphaAnimation(1f, 0.3f) : new AlphaAnimation(0.3f, 1f);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		main_content.setAnimation(animation);

		/**
		 * animation for icon menu
		 */
		if (isMenu) {
			if (isOpen) {
				header_menu_img2.setVisibility(View.VISIBLE);
			}

			Animation a1 = AnimationUtils.loadAnimation(MainActivity.this, isOpen ? R.anim.left_out : R.anim.left_in);
			a1.setDuration(duration);
			Animation a2 = AnimationUtils.loadAnimation(MainActivity.this, isOpen ? R.anim.right_in : R.anim.right_out);
			a2.setDuration(duration);
			a1.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					animationRun = false;
					header_menu_img1.setVisibility(isOpen ? View.GONE : View.VISIBLE);
					header_menu_img2.setVisibility(!isOpen ? View.GONE : View.VISIBLE);
				}
			});

			header_menu_img1.startAnimation(a1);
			header_menu_img2.startAnimation(a2);
		} else {
			Animation a1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);
			a1.setDuration(duration);
			a1.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					animationRun = false;
				}
			});
			header_notification.startAnimation(a1);
		}

	}

	private GoogleMap supportMap;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		VNPLocationUtils.getInstance().init(getApplication());
		VNPLocationUtils.getInstance().requestLocationUpdate();
		FragmentManager fmanager = getSupportFragmentManager();
		Fragment fragment = fmanager.findFragmentById(R.id.map);
		SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
		supportMap = supportmapfragment.getMap();

		map_border = CommonAndroid.getView(this, R.id.map_border);
		mapchoose_choose = CommonAndroid.getView(this, R.id.mapchoose_choose);
		mapchoose_text = CommonAndroid.getView(this, R.id.mapchoose_text);
		mapchoose_choose.setOnClickListener(this);
		header_menu_img1 = CommonAndroid.getView(this, R.id.header_menu_img1);
		header_menu_img2 = CommonAndroid.getView(this, R.id.header_menu_img2);
		menuview = CommonAndroid.getView(this, R.id.menuview);
		menuview.setOnClickListenerClose(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animationActionMenuChange(true, false);
			}
		});

		// int x = 1/0;

		header_menu = CommonAndroid.getView(this, R.id.header_menu);
		header_notification = CommonAndroid.getView(this, R.id.header_notification);
		main_content = CommonAndroid.getView(this, R.id.main_content);

		header_menu.setOnClickListener(this);
		header_notification.setOnClickListener(this);

		list.add(new HomeFragment());
		list.add(new ProfileFragment());
		list.add(new CreateANewMeetingFragment());
		// list.add(new MapFagment());
		main_splash = CommonAndroid.getView(this, R.id.main_splash);
		drawer_layout = CommonAndroid.getView(this, R.id.drawer_layout);
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		drawer_layout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int arg0) {

			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {

			}

			@Override
			public void onDrawerOpened(View arg0) {
			}

			@Override
			public void onDrawerClosed(View arg0) {
			}
		});

		menuleft = CommonAndroid.getView(this, R.id.menuleft);
		menuright = CommonAndroid.getView(this, R.id.menuright);
		main = CommonAndroid.getView(this, R.id.main);
		main.setVisibility(View.GONE);
		Fonts.getIntance(this).loadFonts(fontCallBacK);

		registerReceiver(broadcastReceiverPush, new IntentFilter(MeetMarketUtils.ACTION.UPDATEUIBYPUSH));

		registerReceiver(loginBroadcast, new IntentFilter(MeetMarketUtils.ACTION.ACTION_LOGIN));

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiverPush);
		unregisterReceiver(loginBroadcast);
		super.onDestroy();
	}

	@Override
	public int getResMain() {
		return R.id.main_content;
	}

	private SplashView splashView;
	private FrameLayout main_splash, main, menuleft, menuright, main_content;
	private DrawerLayout drawer_layout;
	private MenuView menuview;
	private RelativeLayout header_menu;
	private ImageView header_notification;
	private ImageView header_menu_img1, header_menu_img2;
	private static final int PICK_IMAGE = 100;
	private static final int PICK_CAMERA = 101;
	private static final int PICK_CROP = 102;

	public void changeImageInfor() {
		CommonAndroid.hiddenKeyBoard(this);
		Builder builder = new Builder(this);
		builder.setItems(new String[] { getString(R.string.chupanhmoi), getString(R.string.chonanhcosan) }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 1) {
					Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, PICK_IMAGE);
				} else {
					createMFileTemp();
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					Uri mImageCaptureUri = null;
					String state = Environment.getExternalStorageState();
					if (Environment.MEDIA_MOUNTED.equals(state)) {
						mImageCaptureUri = Uri.fromFile(mFileTemp);
					} else {
						mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
					}
					cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
					cameraIntent.putExtra("return-data", true);
					startActivityForResult(cameraIntent, PICK_CAMERA);
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
			try {
				InputStream inputStream = getContentResolver().openInputStream(data.getData());
				createMFileTemp();

				FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
				copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
				startCropImage();
			} catch (Exception e) {

			}
		} else if (requestCode == PICK_CAMERA && resultCode == Activity.RESULT_OK) {
			startCropImage();
		} else if (PICK_CROP == requestCode && resultCode == Activity.RESULT_OK) {
			String path = data.getStringExtra(CropImage.IMAGE_PATH);
			if (path == null) {
				return;
			}

			uploadAvatar(path);
		}
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	private void startCropImage() {
		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, 2);
		intent.putExtra(CropImage.ASPECT_Y, 2);
		startActivityForResult(intent, PICK_CROP);
	}

	private void uploadAvatar(final String path) {

		DataStore.getInstance().save("pictureUrl", CommonAndroid.fileImageToBase64(this, path));
		((ProfileFragment) list.get(1)).updateAvatar();

	}

	private File mFileTemp;

	private void createMFileTemp() {
		mFileTemp = new File(cacheDir(this), System.currentTimeMillis() + InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME);
	}

	public static File cacheDir(Context activity) {
		String state = Environment.getExternalStorageState();
		File cacheDir = null;
		String path = "Android/data/" + activity.getPackageName() + "/LazyList";

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), path);
		} else {
			cacheDir = activity.getCacheDir();
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		return cacheDir;
	}

	BitmapDescriptor icon = null;

	public void openMap(double xlat2, double xlng2, String text) {

		header_menu.setVisibility(View.GONE);
		header_notification.setImageResource(R.drawable.mm_header_icon_button);

		try {
			icon = BitmapDescriptorFactory.fromResource(R.drawable.mm_createmeeting_map_pin);
		} catch (Exception exception) {

		}

		map_border.setVisibility(View.VISIBLE);
		mapchoose_text.setText("");
		if (supportMap != null) {
			// gotoPosition(0, 0);
			supportMap.setMyLocationEnabled(true);
			supportMap.clear();
			supportMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng arg0) {
					supportMap.clear();
					addMaker(arg0.latitude, arg0.longitude, icon);
				}
			});

			if (!CommonAndroid.isBlank(text)) {
				mapchoose_text.setText(text);
				addMaker(xlat2, xlng2, icon);
				gotoPosition(xlat2, xlng2);
			} else {
				if (VNPLocationUtils.getInstance().lastKnownLocation != null) {
					gotoPosition(VNPLocationUtils.getInstance().lastKnownLocation.getLatitude(), VNPLocationUtils.getInstance().lastKnownLocation.getLongitude());
				}
			}
		}

	}

	public void gotoPosition(double lat, double log) {
		try {
			supportMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 7));
		} catch (Exception exception) {
		}
	}

	private double xlat, xlng;

	public MarkerOptions addMaker(final double lat, final double longitude, final BitmapDescriptor icon) {
		String url = "http://maps.googleapis.com/maps/api/geocode/json";
		RestClient restClient = new RestClient(url, this);
		restClient.addParam("latlng", String.format("%s,%s", lat, longitude));
		restClient.addParam("sensor", "false");
		restClient.execute(RequestMethod.GET, new RestClientCallBack(this) {
			@Override
			public void onSucsses(int responseCode, String responseMessage, String response) {
				super.onSucsses(responseCode, responseMessage, response);
				// CommonAndroid.showDialogComfirm(MainActivity.this, response);
				List<MapData> list = new ArrayList<MapData>();
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONArray array = jsonObject.getJSONArray("results");

					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						String formatted_address = CommonAndroid.getString(item, "formatted_address");
						MapData data = new MapData();
						data.setFormatted_address(formatted_address);
						data.setLat(lat + "");
						data.setLng(longitude + "");
						list.add(data);
					}
				} catch (Exception exception) {
				}

				if (list.size() == 0) {
					CommonAndroid.showDialogComfirm(MainActivity.this, getString(R.string.error_search_location));
				} else {
					xlat = lat;
					xlng = longitude;
					mapchoose_text.setText(list.get(0).getFormatted_address());

					try {
						LatLng mLocation = new LatLng(lat, longitude);
						MarkerOptions myMarkerOptions = new MarkerOptions();
						myMarkerOptions.position(mLocation);

						if (icon != null) {
							myMarkerOptions.icon(icon);
						}

						supportMap.addMarker(myMarkerOptions);
					} catch (Exception exception) {
					}
				}
			}
		}, true);

		return null;
	}

	public void createNewMeeting() {
		Message message = new Message();
		message.what = HANDLER_MENU;
		message.arg1 = 1;
		handler.sendMessage(message);
	}
}