package z.base;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.acv.meetmarket.R;

/**
 * 
 * @author ACV-ANDROID-DEV-01
 * @version 1.0
 */
public abstract class MeetMarketBaseFragment extends BaseFragment {

	@Override
	public void init(View view) {
	}

	@Override
	public void onChangeLanguage() {

	}

	public void onShowData() {
		if (getView() != null && getView().getVisibility() == View.GONE) {
			//getView().startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));
		}
	}

	public boolean isShow() {
		return (getView() != null && getView().getVisibility() == View.VISIBLE);
	}

	public void hiddenView() {
		if (getView() != null) {
//			Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_to_center);
//			animation.setFillAfter(false);
//			animation.setAnimationListener(new AnimationListener() {
//
//				@Override
//				public void onAnimationStart(Animation animation) {
//
//				}
//
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//
//				}
//
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					if (getView() != null) {
//						getView().setVisibility(View.GONE);
//					}
//				}
//			});
//			getView().startAnimation(animation);
			
			getView().setVisibility(View.GONE);
		}

	}
}