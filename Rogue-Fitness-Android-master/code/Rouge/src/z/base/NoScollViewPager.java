package z.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

// z.base.NoScollViewPager
public class NoScollViewPager extends ViewPager {
	private boolean isEnableScroll = false;

	public void setEnableScroll(boolean isEnableScroll) {
		this.isEnableScroll = isEnableScroll;
	}

	public NoScollViewPager(Context context) {
		super(context);
	}

	public NoScollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isEnableScroll) {
			return super.onInterceptTouchEvent(arg0);
		}
		return false;
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent arg0) {
	// if (isEnableScroll) {
	// return super.onTouchEvent(arg0);
	// }
	// return true;
	// }

}
