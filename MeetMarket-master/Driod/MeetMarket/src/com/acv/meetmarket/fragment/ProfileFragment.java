package com.acv.meetmarket.fragment;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.CommonAndroid;
import z.base.ImageLoader;
import z.base.MeetMarketBaseFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.acv.meetmarket.MainActivity;
import com.acv.meetmarket.R;
import com.acv.meetmarket.db.DataStore;
import com.acv.meetmarket.db.UserTable;
import com.acv.meetmarket.service.MeetMarketService;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ProfileFragment extends MeetMarketBaseFragment {

	@Override
	public void onShowData() {
		super.onShowData();

		DataStore.getInstance().init(getActivity());
		getView().setVisibility(View.VISIBLE);
		final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, getString(R.string.loading));

		new Thread() {
			public void run() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
				query.whereEqualTo("linkedinid", new UserTable(getActivity()).getUserId());
				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> commentList, ParseException e) {
						if (commentList != null && commentList.size() >= 1) {
							ParseObject object = commentList.get(0);
							DataStore.getInstance().init(getActivity());
							DataStore.getInstance().save("pictureUrl", object.getString("icon"));
							DataStore.getInstance().save("skills", object.getString("skills"));
							DataStore.getInstance().save("about", object.getString("about"));
							DataStore.getInstance().save("cover", object.getString("cover"));
						}

						dialog.dismiss();

						updateData();
					}
				});
			};
		}.start();
	}

	private void updateData() {

		profile_name.setText(DataStore.getInstance().get("lastName", "") + " " + DataStore.getInstance().get("firstName", ""));
		String headline = DataStore.getInstance().get("headline", "");
		if (headline.contains("at")) {
			profile_company_positon.setText(headline.substring(0, headline.indexOf("at")));
			profile_company.setText(headline.substring(headline.indexOf("at") + 3, headline.length()));
		}

		String avatar = DataStore.getInstance().get("pictureUrl", "");

		if (CommonAndroid.isBlank(avatar)) {
			avatar = "https://static.licdn.com/scds/common/u/images/themes/katy/ghosts/person/ghost_person_200x200_v1.png";
		}

		ImageLoader.getInstance(getActivity()).displayAvatar(avatar, profile_icon);
		String skills = DataStore.getInstance().get("skills", "");
		profile_skills.removeAllViews();
		try {
			JSONArray array = new JSONArray(skills);
			for (int i = 0; i < array.length() && i < 3; i++) {
				JSONObject object = array.getJSONObject(i);
				String id = CommonAndroid.getString(object, "id");
				String name = CommonAndroid.getString(object.getJSONObject("skill"), "name");

				SkillProfileItemView skillProfileItemView = new SkillProfileItemView(getActivity());
				profile_skills.addView(skillProfileItemView);
				skillProfileItemView.updateData(id, name);
			}
		} catch (Exception e) {
		}

		profile_about_you.setText(DataStore.getInstance().get("about", ""));

		profile_scroll.postDelayed(new Runnable() {
			@Override
			public void run() {
				profile_scroll.scrollTo(0, 0);
			}
		}, 10);
	}

	@Override
	public void init(View view) {
		super.init(view);
		view.setVisibility(View.GONE);
		profile_scroll = CommonAndroid.getView(view, R.id.profile_scroll);
		profile_icon = CommonAndroid.getView(view, R.id.profile_icon);
		profile_skills = CommonAndroid.getView(view, R.id.profile_skills);
		profile_name = CommonAndroid.getView(view, R.id.profile_name);
		profile_company = CommonAndroid.getView(view, R.id.profile_company);
		profile_company_positon = CommonAndroid.getView(view, R.id.profile_company_positon);
		profile_about_you = CommonAndroid.getView(view, R.id.profile_about_you);
		profile_about_you_lenth = CommonAndroid.getView(view, R.id.profile_about_you_lenth);
		profile_icon.setOnClickListener(this);

		profile_name.setText("");
		profile_company.setText("");
		profile_company_positon.setText("");
		profile_about_you.setText("");

		profile_about_you.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int length = profile_about_you.getText().toString().length();
				profile_about_you_lenth.setText(getActivity().getString(R.string.text_13).replace("0", length + ""));
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.profile_icon) {
			((MainActivity) getActivity()).changeImageInfor();
		}
	}

	private void removeSkill(final View view, final String id, String name) {
		CommonAndroid.hiddenKeyBoard(getActivity());
		CommonAndroid.showDialogOkcancel(getActivity(), getString(R.string.message_check_remove_skill), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				((LinearLayout) view.getParent()).removeView(view);
			}
		});
	}

	@Override
	public int getLayout() {
		return R.layout.profile;
	}

	private ScrollView profile_scroll;
	private LinearLayout profile_skills;
	private ImageView profile_icon;
	private TextView profile_name, profile_company, profile_company_positon, profile_about_you_lenth;
	private EditText profile_about_you;

	private class SkillProfileItemView extends LinearLayout implements OnClickListener {
		private EditText profile_skill_item_edt;
		private ImageView profile_skill_img;

		@Override
		public void onClick(View v) {
			removeSkill(SkillProfileItemView.this, xid, name);
		}

		public SkillProfileItemView(Context context) {
			super(context);
			CommonAndroid.getView(getContext(), R.layout.profileskill_item, this);
			profile_skill_item_edt = CommonAndroid.getView(this, R.id.profile_skill_item_edt);
			profile_skill_img = CommonAndroid.getView(this, R.id.profile_skill_img);
			profile_skill_img.setOnClickListener(this);
			profile_skill_item_edt.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					name = profile_skill_item_edt.getText().toString();
				}
			});
		}

		private String xid, name;

		public void updateData(String id, String name) {
			profile_skill_item_edt.setText(name);
			this.xid = id;
			this.name = name;
		}

	}

	public String getAbout() {
		if (profile_about_you != null)
			return profile_about_you.getText().toString();
		return "";
	}

	public JSONArray getSkills() {
		// String skills = DataStore.getInstance().get("skills", "");
		JSONArray array = new JSONArray();
		try {
			// array = new JSONArray(skills);
		} catch (Exception e1) {
		}
		if (profile_skills != null) {
			for (int i = 0; i < profile_skills.getChildCount(); i++) {
				SkillProfileItemView child = (SkillProfileItemView) profile_skills.getChildAt(i);

				try {
					JSONObject object = new JSONObject();
					object.put("id", child.xid);
					JSONObject skill = new JSONObject();
					skill.put("name", child.name);
					object.put("skill", skill);
					array.put(object);
					// for (int j = 0; j < array.length(); j++) {
					// JSONObject json = array.getJSONObject(j);
					// if (child.xid.equals(CommonAndroid.getString(json,
					// "id"))) {
					// array.put(j, object);
					// break;
					// }
					// }
				} catch (Exception e) {
				}
			}
		}
		return array;
	}

	public void updateAvatar() {
		String avatar = DataStore.getInstance().get("pictureUrl", "");

		if (CommonAndroid.isBlank(avatar)) {
			avatar = "https://static.licdn.com/scds/common/u/images/themes/katy/ghosts/person/ghost_person_200x200_v1.png";
		}

		ImageLoader.getInstance(getActivity()).displayAvatar(avatar, profile_icon);
	}
}