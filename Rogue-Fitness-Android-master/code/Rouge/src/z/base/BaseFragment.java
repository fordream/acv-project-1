package z.base;

import z.base.BaseActivity.FragemntAnimation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.acv.rouge.R;
import com.acv.rouge.db.TrackYourOrderTable;
import com.acv.rouge.view.HeaderView;

public abstract class BaseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

	public void startAnimation(View view) {

	}

	public void startCloseAnimation(View view) {

	}

	public BaseFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public View getView() {
		return v;
	}

	private View v;

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayout(), null);
		view.setOnClickListener(null);
		init(view);
		v = view;
		return view;
	}

	public abstract int getLayout();

	public abstract void init(View view);

	public final void startFragment(BaseFragment baseFragment, Bundle extras, FragemntAnimation fanimation) {
		CommonAndroid.hiddenKeyBoard(getActivity());
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity) getActivity()).startFragment(baseFragment, extras, fanimation);
		}
	}

	public void onFragmentBackPress(Bundle extras) {
		if (getActivity() == null) {
			return;
		}

		// View view = getView();
		//
		// if (view != null) {
		// if (extras == null)
		// view.startAnimation(AnimationUtils.loadAnimation(view.getContext(),
		// R.anim.left_in));
		// else if (!extras.containsKey("changelocation")) {
		// view.startAnimation(AnimationUtils.loadAnimation(view.getContext(),
		// R.anim.left_in));
		// }
		// }
	}

	public abstract void onChangeLanguage();

	public boolean isFinish() {
		if (getActivity() == null) {
			return true;
		}

		return getActivity().isFinishing();
	}

	public void finish() {
		if (getActivity() != null) {
			getActivity().finish();
		}
	}

	public void reload() {

	}

	public void onClickHeaderRight() {

	}

	public void onClickHeaderLeft() {

	}

	public String getEmail() {
		if (getArguments() != null && getArguments().containsKey("email"))
			return getArguments().getString("email");

		return "";
	}

	public String getOrderId() {
		if (getArguments() != null && getArguments().containsKey("order_id"))
			return getArguments().getString("order_id");
		return "";
	}

	public String getLocation() {
		if (getArguments() != null && getArguments().containsKey("location"))
			return getArguments().getString("location");
		return "";
	}

	public String getOrderSearch() {
		if (getArguments() != null && getArguments().containsKey(TrackYourOrderTable.ordersearch))
			return getArguments().getString(TrackYourOrderTable.ordersearch);
		return "";
	}

	public String getData() {
		return "";
	}

	public void onStartAnimationOpen() {
		// if (getView() != null && getActivity() != null) {
		// getView().startAnimation(AnimationUtils.loadAnimation(getActivity(),
		// R.anim.left_in));
		// }
	}

	/**
	 * 
	 * @param email
	 * @param orderSearch
	 * @param order_id
	 * @param store
	 */

	public void loadData(String email, String orderSearch, String order_id, String store) {
	}

	public void reLoadData() {

	}

	public boolean isOpenSetting() {
		HeaderView headerView = CommonAndroid.getView(getView(), R.id.header);
		if (headerView != null)
			return headerView.isShownSetting();

		return false;
	}

	public void closeMenu() {
		HeaderView headerView = CommonAndroid.getView(getView(), R.id.header);
		if (headerView != null)
			headerView.closeSetting();
	}
}