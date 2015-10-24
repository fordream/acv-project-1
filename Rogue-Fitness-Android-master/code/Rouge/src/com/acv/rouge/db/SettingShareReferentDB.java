package com.acv.rouge.db;

import android.content.Context;

import com.acv.rouge.R;

public class SettingShareReferentDB extends ShareReferentDB {

	public SettingShareReferentDB(Context context) {
		super(context);
	}

	public int[] icons() {
		return new int[] {//
		R.drawable.rogue_icon_flag_usa// 0
//				, R.drawable.rogue_icon_flag_canada// 1
				, R.drawable.rogue_icon_flag_eu// 2
				, R.drawable.rogue_icon_flag_aus// 3
				, R.drawable.rogue_icon_flag_supply };
	}

	public int[] strs() {
		return new int[] {//
		R.string.location_rogue_usa// 0
//				, R.string.location_rogue_canada// 1
				, R.string.location_rogue_europe// 2
				, R.string.location_rogue_australia// 3
				, R.string.location_rogue_supply };
	}

	public int getPositionLocation() {
		return get("location", 0);
	}

	public void saveLocation(int i) {
		save("location", i);
	}

	public boolean isEnablePush() {
		return get("isEnablePush", true);
	}

	public void setEnablePush(boolean checked) {
		save("isEnablePush", checked);
	}

	public void setLinkYouTobe(String result) {
		save("setLinkYouTobe", result);
	}

	public String getLinkYouTobe() {
		return get("setLinkYouTobe", "");
	}
}