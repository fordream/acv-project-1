package z.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.acv.rouge.R;
import com.acv.rouge.rouge.fragment.ContactUsFragment;
import com.acv.rouge.rouge.fragment.OrderFragment;
import com.acv.rouge.rouge.fragment.TrackYourOrderFragment;

public abstract class BaseActivity extends FragmentActivity {

	public enum FragemntAnimation {
		NONE, RIGHT_IN, FADE_IN, TOP_IN, BOTTOM_IN
	}

	protected FragmentManager fragmentManager;
	Fragment mContent = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		fragmentManager = getSupportFragmentManager();
		setContentView(getLayout());
	}

	public abstract int getLayout();

	public abstract int getResMain();

	public final void startFragment(BaseFragment baseFragment, Bundle bundle, FragemntAnimation fanimation) {
		try {
			android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
			int anim_start = R.anim.right_in;
			int anim_end = R.anim.right_out;

			if (fanimation == FragemntAnimation.NONE) {
				anim_start = R.anim.a_nothing;
				anim_end = R.anim.a_nothing;
			} else if (fanimation == FragemntAnimation.RIGHT_IN) {
				anim_start = R.anim.right_in;
				anim_end = R.anim.right_out;
			} else if (fanimation == FragemntAnimation.FADE_IN) {
				anim_start = R.anim.abc_fade_in;
				anim_end = R.anim.abc_fade_out;
			} else if (fanimation == FragemntAnimation.TOP_IN) {
				anim_start = R.anim.abc_slide_in_top;
				anim_end = R.anim.abc_slide_out_top;
			} else if (fanimation == FragemntAnimation.BOTTOM_IN) {
				anim_start = R.anim.abc_slide_in_bottom;
				anim_end = R.anim.abc_slide_out_bottom;
			}

			transaction.setCustomAnimations(anim_start, anim_start, anim_end, anim_end);

			if (bundle != null) {
				baseFragment.setArguments(bundle);
			}

			transaction.add(getResMain(), baseFragment, "" + System.currentTimeMillis());
			transaction.addToBackStack(null);
			transaction.commit();
		} catch (Exception exception) {

		}
	}

	public int getFragemtCount() {
		try {
			List<Fragment> fragments = fragmentManager.getFragments();
			return fragments.size();
		} catch (Exception exception) {
			return 0;
		}
	}

	public void pushChangeLanguage() {

		List<Fragment> fragments = fragmentManager.getFragments();
		if (fragments != null) {
			for (Fragment fragment : fragments) {
				if (fragment instanceof BaseFragment) {
					((BaseFragment) fragment).onChangeLanguage();
				}
			}
		}
	}

	public void onGotoNewTrackOrderFromContactUs() {
	}
}