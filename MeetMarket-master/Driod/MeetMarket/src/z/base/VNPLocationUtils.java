package z.base;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class VNPLocationUtils {
	private static VNPLocationUtils instance = new VNPLocationUtils();
	private LocationManager locationManager;
	public Location lastKnownLocation;
	private Context mContext;
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			if (location != null) {
				lastKnownLocation = location;
			}

			Log.e("lastKnownLocation", lastKnownLocation + " onLocationChanged");
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.e("lastKnownLocation", lastKnownLocation + " onStatusChanged");
		}

		public void onProviderEnabled(String provider) {
			// toast("Enable : " + provider);
			Log.e("lastKnownLocation", lastKnownLocation + " onProviderEnabled");

		}

		public void onProviderDisabled(String provider) {
			// toast("Disabled : " + provider);
			Log.e("lastKnownLocation", lastKnownLocation + " onProviderDisabled");
		}
	};

	// private void toast(String text) {
	// // Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	// }

	private VNPLocationUtils() {
	}

	public void init(Context context) {
		mContext = context;
		locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean isEnableGPSPrvider() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public boolean isEnableNetworkPrvider() {
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	public void requestLocationUpdate() {
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		if (isEnableNetworkPrvider()) {
			locationProvider = LocationManager.NETWORK_PROVIDER;
		} else if (isEnableGPSPrvider()) {
			locationProvider = LocationManager.GPS_PROVIDER;
		}

		if (locationProvider != null) {
			locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
			lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		}

		Log.e("lastKnownLocation", lastKnownLocation + " onProviderDisabled");
	}

	public void removeUpdates() {
		locationManager.removeUpdates(locationListener);
	}

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			return true;
		}

		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		if (isSignificantlyNewer) {
			return true;
		} else if (isSignificantlyOlder) {
			return false;
		}

		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public static VNPLocationUtils getInstance() {
		return instance == null ? (instance = new VNPLocationUtils()) : instance;
	}

	public static double getDistance(LatLng LatLng1, LatLng LatLng2) {
		double distance = 0;
		Location locationA = new Location("A");
		locationA.setLatitude(LatLng1.latitude);
		locationA.setLongitude(LatLng1.longitude);
		Location locationB = new Location("B");
		locationB.setLatitude(LatLng2.latitude);
		locationB.setLongitude(LatLng2.longitude);
		distance = locationA.distanceTo(locationB);
		distance = round(distance / 1000, 2);
		return distance;
	}

	public static double round(double distance, int i) {
		distance = (long) (distance * (10 ^ i));
		return distance / (10 ^ i);
	}
}