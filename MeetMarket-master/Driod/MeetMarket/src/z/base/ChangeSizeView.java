package z.base;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.acv.meetmarket.R;

public class ChangeSizeView {
	private static ChangeSizeView instance = new ChangeSizeView();

	public interface ChangLayoutSizeListener {
		public void onStart();

		public void onSuccess();
	}

	public boolean hasView(View view) {
		return views.contains(view);
	}

	public static ChangeSizeView getInstance() {

		if (instance == null)
			instance = new ChangeSizeView();
		return instance;
	}

	private List<View> views = new ArrayList<View>();

	public void startChangLayoutSize(int fromY, final int toY, final View view, long time, ChangLayoutSizeListener changLayoutSizeListener) {

		if (views.contains(view)) {
			return;
		} else {
			if (changLayoutSizeListener != null) {
				changLayoutSizeListener.onStart();
			}
			views.add(view);
			changLayoutSize(fromY, toY, view, time, changLayoutSizeListener);
		}
	}

	/**
	 * 
	 * @param fromY
	 * @param toY
	 * @param view
	 */
	private void changLayoutSize(int fromY, final int toY, final View view, final long time, final ChangLayoutSizeListener changLayoutSizeListener) {
		if (fromY < 0 || toY < 0) {
			views.remove(view);
			if (changLayoutSizeListener != null) {
				changLayoutSizeListener.onSuccess();
			}
			return;
		}

		int dy = (int) view.getContext().getResources().getDimension(R.dimen.dimen_20dp);
		if (time == 0) {
			dy = (int) view.getContext().getResources().getDimension(R.dimen.dimen_20dp);
		}
		if (fromY < toY) {
			fromY = fromY + dy;

			if (fromY > toY) {
				fromY = toY;
			}
		} else if (fromY > toY) {
			fromY = fromY - dy;

			if (fromY < toY) {
				fromY = toY;
			}
		}

		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = fromY;
		view.setLayoutParams(layoutParams);

		if (Math.abs(toY - fromY) > 0) {
			final int newFromY = fromY;
			view.postDelayed(new Runnable() {

				@Override
				public void run() {
					changLayoutSize(newFromY, toY, view, time, changLayoutSizeListener);
				}
			}, time);
		} else {
			views.remove(view);
			if (changLayoutSizeListener != null) {
				changLayoutSizeListener.onSuccess();
			}
		}
	}

	public ChangeSizeView() {
	}

}
