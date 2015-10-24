package z.base.social;

import java.util.Map;
import java.util.Set;

import org.brickred.socialauth.Career;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import z.base.LogUtils;
import android.content.Context;
import android.os.Bundle;

import com.acv.meetmarket.R;
import com.acv.meetmarket.db.DataStore;

/**
 * 
 * @author ACV-ANDROID-DEV-01
 * @version 1.0
 * @see class for login and get information of Linkin.
 */
public class SocialManager {
	private static SocialManager instance = new SocialManager();
	private Context context;
	private DialogListener responseListener = null;

	public void setResponseListener(DialogListener responseListener) {
		this.responseListener = responseListener;
	}

	public static SocialManager getInstance() {
		return instance == null ? instance = new SocialManager() : instance;
	}

	public void init(Context xcontext) {
		this.context = xcontext;
	}

	private SocialAuthAdapter adapter;

	public void authorize() {
		adapter.authorize(context, Provider.LINKEDIN);
	}

	private SocialManager() {
		adapter = new SocialAuthAdapter(new DialogListener() {
			@Override
			public void onComplete(Bundle values) {

				// final String providerName =
				// values.getString(SocialAuthAdapter.PROVIDER);
				if (context != null) {
					// Toast.makeText(context, providerName + " connected " +
					// adapter.getUserProfile().getFirstName(),
					// Toast.LENGTH_SHORT).show();
				}

				if (responseListener != null) {
					responseListener.onComplete(values);
				}

			}

			@Override
			public void onError(SocialAuthError error) {
				error.printStackTrace();

				if (responseListener != null) {
					responseListener.onError(error);
				}
			}

			@Override
			public void onCancel() {
				if (responseListener != null) {
					responseListener.onCancel();
				}
			}

			@Override
			public void onBack() {
				if (responseListener != null) {
					responseListener.onBack();
				}
			}
		});

		adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);

		try {
			adapter.addConfig(Provider.LINKEDIN, "bh82t52rdos6", "zQ1LLrGbhDZ36fH8", null);
		} catch (Exception exception) {
			LogUtils.es("errorx", exception);
		}
	}

	public SocialAuthAdapter getAdapter() {
		return adapter;
	}

	/**
	 * 
	 * @return email of linkin
	 */
	public String getEmail() {
		if (adapter.getUserProfile() != null) {
			return adapter.getUserProfile().getEmail();
		}
		return "";
	}

	/**
	 * 
	 * @return full name or (last name and first name) of linkin
	 */
	public String getName() {

		if (adapter.getUserProfile() != null) {

			if (adapter.getUserProfile().getDisplayName() != null) {
				return adapter.getUserProfile().getDisplayName();
			}
			if (adapter.getUserProfile().getFullName() != null) {
				return adapter.getUserProfile().getFullName();
			}
			return adapter.getUserProfile().getLastName() + " " + adapter.getUserProfile().getFirstName();
		}
		return "";
	}

	/**
	 * 
	 * @return link avatar of linkin
	 */
	public String getAvatar() {
		if (adapter.getUserProfile() != null) {
			return adapter.getUserProfile().getProfileImageURL();
		}
		return "";
	}

	/**
	 * 
	 * @return check status login
	 */

	public boolean isLogin() {
		DataStore.getInstance().init(context);
		return DataStore.getInstance().get("login", false);
	}

	public Profile getUserProfile() {
		return adapter.getUserProfile();
	}

}