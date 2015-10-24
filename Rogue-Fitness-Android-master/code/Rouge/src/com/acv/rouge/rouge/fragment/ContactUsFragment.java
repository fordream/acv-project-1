package com.acv.rouge.rouge.fragment;

import z.base.BaseFragment;
import z.base.CommonAndroid;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.db.DataStore;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.rouge.MainActivity;
import com.acv.rouge.services.api.RequestMethod;
import com.acv.rouge.services.api.RestClient;
import com.acv.rouge.services.api.RestClientCallBack;
import com.acv.rouge.view.HeaderView;
import com.acv.rouge.view.HeaderView.HeaderViewType;
import com.acv.rouge.view.SettingView.SettingListener;

public class ContactUsFragment extends BaseFragment {
	private EditText contactus_email, contactus_question;
	private View control_pause;
	private View txt_error_qestion, txt_error_email;
	private View contactus_cuccess;
	private TextView contactus_order_number;
	private View main;

	public ContactUsFragment() {
	}

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) main.getLayoutParams();
			layout.topMargin = -0;
			main.setLayoutParams(layout);

		}
	};

	@Override
	public int getLayout() {
		return R.layout.contactus;
	}

	private HeaderView headerView;

	@Override
	public void loadData(String email, String search, String order_id, String store) {
		super.loadData(email, search, order_id, store);
		this.search = search;
		headerView.setType(HeaderViewType.CONTACTUS_FORM, search);
		getView().findViewById(R.id.contactus_cuccess).setVisibility(View.GONE);
		CommonAndroid.setText(getView(), R.id.contactus_order_number, String.format(getString(R.string.text_8), search));
		CommonAndroid.setText(getView(), R.id.contactus_comfirm_order_number, String.format(getString(R.string.contactus_success_order_form), search));
		CommonAndroid.setText(getView(), R.id.contactus_email, email);
		contactus_question.setText("");
		txt_error_qestion.setVisibility(View.GONE);
		txt_error_email.setVisibility(View.GONE);
	}

	@Override
	public void init(View view) {
		main = CommonAndroid.getView(view, R.id.main);
		view.setVisibility(View.GONE);
		contactus_order_number = CommonAndroid.getView(view, R.id.contactus_order_number);
		contactus_cuccess = CommonAndroid.getView(view, R.id.contactus_cuccess);
		txt_error_qestion = CommonAndroid.getView(view, R.id.txt_error_qestion);
		txt_error_email = CommonAndroid.getView(view, R.id.txt_error_email);
		control_pause = CommonAndroid.getView(view, R.id.control_pause);
		control_pause.setOnClickListener(this);
		control_pause.setVisibility(View.GONE);
		headerView = CommonAndroid.getView(view, R.id.header);
		headerView.setVisibility(View.VISIBLE);
		headerView.setType(HeaderViewType.CONTACTUS_FORM, getOrderSearch());
		headerView.setSettingListener(new SettingListener() {

			@Override
			public void open() {
				control_pause.setVisibility(View.VISIBLE);
				CommonAndroid.hiddenKeyBoard(getActivity());
				RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) main.getLayoutParams();
				layout.topMargin = -(int) getActivity().getResources().getDimension(R.dimen.dimen_3dp);
				main.setLayoutParams(layout);
			}

			@Override
			public void close() {
				control_pause.setVisibility(View.GONE);
				handler.sendEmptyMessageDelayed(0, 10);
			}
		});

		CommonAndroid.setText(view, R.id.contactus_order_number, String.format(getString(R.string.text_8), getOrderSearch()));
		CommonAndroid.setText(view, R.id.contactus_comfirm_order_number, String.format(getString(R.string.contactus_success_order_form), getOrderSearch()));
		CommonAndroid.setText(view, R.id.contactus_cancel, Html.fromHtml(String.format("<u>%s</u>", getString(R.string.cancel))));
		CommonAndroid.setText(view, R.id.contactus_email, getEmail());

		contactus_email = CommonAndroid.getView(view, R.id.contactus_email);
		contactus_question = CommonAndroid.getView(view, R.id.contactus_question);

		view.findViewById(R.id.contactus_submit).setOnClickListener(this);
		view.findViewById(R.id.contactus_cancel).setOnClickListener(this);
		view.findViewById(R.id.header_control_right).setOnClickListener(this);
		view.findViewById(R.id.header_control_left).setOnClickListener(this);
		view.findViewById(R.id.contactus_cuccess).setOnClickListener(this);
		view.findViewById(R.id.contactus_scuccess_go_back).setOnClickListener(this);
		view.findViewById(R.id.contactus_scuccess_track_new_order).setOnClickListener(this);

		if (DataStore.getInstance().get("f3view", false)) {
			view.setVisibility(View.VISIBLE);

			email = DataStore.getInstance().get("f3view-email", "");
			search = DataStore.getInstance().get("f3view-ordersearch", "");
			contactus_question.setText(DataStore.getInstance().get("f3view-question", ""));
			load(email, search);
		}
	}

	@Override
	public void onChangeLanguage() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		CommonAndroid.hiddenKeyBoard(getActivity());
		if (v.getId() == R.id.control_pause) {
			headerView.closeSetting();
		} else if (v.getId() == R.id.header_control_right) {
			headerView.showSetting();
		} else if (v.getId() == R.id.header_control_left) {
			getActivity().onBackPressed();
		} else if (v.getId() == R.id.contactus_cancel) {
			getActivity().onBackPressed();
		} else if (v.getId() == R.id.contactus_scuccess_go_back) {
			getActivity().onBackPressed();
		} else if (v.getId() == R.id.contactus_scuccess_track_new_order) {
			((MainActivity) getActivity()).gotoNewTrackOrderFromContactUs();
		} else if (v.getId() == R.id.contactus_submit) {
			txt_error_email.setVisibility(View.GONE);
			txt_error_qestion.setVisibility(View.GONE);

			boolean canCallApi = true;
			if (!CommonAndroid.VNPPartternChecked.isEmail(contactus_email.getText().toString())) {
				txt_error_email.setVisibility(View.VISIBLE);
				canCallApi = false;
			}

			if (CommonAndroid.isBlank(contactus_question.getText().toString().trim())) {
				txt_error_qestion.setVisibility(View.VISIBLE);
				canCallApi = false;
			}

			if (canCallApi) {
				// https://warmup.roguefitness.com/shippingtracker/api/submitInquiry?token=7KAh3EPZqXWLxpqgKZ8PmVt
				// Content-Type: application/json
				// {"email":"jgrim@roguefitness.com","order_id":"USA123456","text":"Hello world, just trying stuff out!"}
				RestClient restClient = new RestClient("submitInquiry", getActivity());
				restClient.addHeader("Content-Type", "application/json");
				restClient.addParam("email", contactus_email.getText().toString().trim());
				restClient.addParam("order_id", search);
				restClient.addParam("text", contactus_question.getText().toString().trim());
				restClient.execute(RequestMethod.GET, new RestClientCallBack(getActivity()) {
					@Override
					public void onSucsses(int responseCode, String responseMessage, String response) {
						super.onSucsses(responseCode, responseMessage, response);

						boolean success = CommonAndroid.getStringJsonString(response, "success").equals("1");
						if (success) {
							getView().findViewById(R.id.contactus_cuccess).setVisibility(View.VISIBLE);
						} else {
							if (getActivity() != null) {
								CommonAndroid.showDialogComfirm(getActivity(), getActivity().getString(R.string.error_contactus));
							}
						}
					}
				}, true);

			}
		}
	}

	String search = "";
	String email = "";

	public void load(String email, String order_id) {
		this.email = email;
		this.search = order_id;
		headerView.setType(HeaderViewType.CONTACTUS_FORM, getOrderSearch());
		contactus_email.setText(getEmail());
		contactus_order_number.setText(String.format(getString(R.string.text_8), getOrderSearch()));
		contactus_cuccess.setVisibility(View.GONE);
	}

	public String getEmail() {
		return email;

	}

	public String getOrderSearch() {
		return search;
	}

	public String getQuestion() {
		return contactus_question.getText().toString();
	}
}