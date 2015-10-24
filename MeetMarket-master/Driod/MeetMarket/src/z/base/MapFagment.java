package z.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//com.google.android.gms.maps.MapFragment
public class MapFagment extends SupportMapFragment implements OnMarkerClickListener, OnMarkerDragListener, OnInfoWindowClickListener {
	public static final int ROOMSIZE = 14;
	private GoogleMap mMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public MapFagment() {
		super();

	}

	// public void addMaker(Clinic clinic) {
	// BitmapDescriptor icon = null;
	//
	// try {
	// icon = BitmapDescriptorFactory.fromResource(R.drawable.s7apoint);
	// } catch (Exception exception) {
	// }
	//
	// try {
	// double latitude = Double.parseDouble(clinic.latitude);
	// double longitude = Double.parseDouble(clinic.longitude);
	// LatLng mLocation = new LatLng(latitude, longitude);
	// GoogleMap googleMap = getMap();
	// MarkerOptions myMarkerOptions = new MarkerOptions();
	// myMarkerOptions.position(mLocation);
	// myMarkerOptions.title(clinic.name);
	// myMarkerOptions.snippet(clinic.address);
	// if (icon != null) {
	// myMarkerOptions.icon(icon);
	// }
	//
	// com.google.android.gms.maps.model.Marker marker = googleMap
	// .addMarker(myMarkerOptions);
	// } catch (Exception exception) {
	//
	// }
	// }

	public MarkerOptions addMaker(String latitude, String longitude, BitmapDescriptor icon) {
		try {
			return addMaker(Double.parseDouble(latitude), Double.parseDouble(longitude), icon);
		} catch (Exception exception) {

		}
		return null;
	}

	public void gotoPosition(String latitude, String longitude) {
		try {
			gotoPosition(Double.parseDouble(latitude), Double.parseDouble(longitude));
		} catch (Exception exception) {
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mMap = getMap();

	}

	@Override
	public void onResume() {
		super.onResume();

		mMap = getMap();
		if (mMap != null) {
			mMap.setOnMarkerClickListener(this);
			mMap.setOnMarkerDragListener(this);
			mMap.setOnInfoWindowClickListener(this);
		}
	}

	public static final void updateMap(Context context, String data) {
		Intent intent = new Intent("UPDATEMAP");
		intent.putExtra("data", data);
		context.sendBroadcast(intent);
	}

	@Override
	public void onStop() {
		super.onStop();
		// getActivity().unregisterReceiver(broadcastReceiver);
	}

	// private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	//
	// }
	// };

	// Convert a view to bitmap
	// public static Bitmap createDrawableFromView(Context context, View view) {
	// DisplayMetrics displayMetrics = new DisplayMetrics();
	// ((Activity) context).getWindowManager().getDefaultDisplay()
	// .getMetrics(displayMetrics);
	// view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT));
	// view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
	// view.layout(0, 0, displayMetrics.widthPixels,
	// displayMetrics.heightPixels);
	// view.buildDrawingCache();
	// Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
	// view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
	//
	// Canvas canvas = new Canvas(bitmap);
	// view.draw(canvas);
	//
	// return bitmap;
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void clearMap() {
		GoogleMap googleMap = getMap();
		if (googleMap != null)
			googleMap.clear();
	}

	// public void gotoMyPosition() {
	// Location location = VNPLocationUtils.getInstance().lastKnownLocation;
	// if (location != null) {
	// BitmapDescriptor icon = null;
	//
	// try {
	// icon = BitmapDescriptorFactory
	// .fromResource(R.drawable.myposition);
	// } catch (Exception exception) {
	//
	// }
	// addMaker(location.getLatitude(), location.getLongitude(), icon);
	// gotoPosition(location.getLatitude(), location.getLongitude());
	// }
	// }

	public void gotoPosition(double lat, double log) {
		try {
			getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), ROOMSIZE));
		} catch (Exception exception) {
		}
	}

	public MarkerOptions addMaker(double lat, double longitude, BitmapDescriptor icon) {
		try {
			LatLng mLocation = new LatLng(lat, longitude);
			GoogleMap googleMap = getMap();
			MarkerOptions myMarkerOptions = new MarkerOptions();
			myMarkerOptions.position(mLocation);

			if (icon != null) {
				myMarkerOptions.icon(icon);
			}

			googleMap.addMarker(myMarkerOptions);
			return myMarkerOptions;
		} catch (Exception exception) {
		}
		return null;
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
	}

}