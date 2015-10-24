package com.acv.rouge.rouge;

import z.base.CommonAndroid;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.rouge.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	public static final String API_KEY = "AIzaSyCe6tORd9Ch4lx-9Ku5SQ476uS9OtZYsWA";
	public static final String VIDEO_ID = "_UfFYjUcIaI";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helporderyoutube);

		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayerview);

		if (CommonAndroid.hasPackage("com.google.android.youtube", this)) {
			youTubePlayerView.initialize(API_KEY, this);
		} else {
			CommonAndroid.showDialogOkcancel(this, getString(R.string.need_you_tube), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.youtube")));
				}
			});
		}
		ImageView img_1 = CommonAndroid.getView(this, R.id.img_1);
		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("top")) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img_1.getLayoutParams();
			layoutParams.topMargin = getIntent().getExtras().getInt("top");
			img_1.setLayoutParams(layoutParams);
		}

		TextView text_2 = (TextView) findViewById(R.id.text_2);
		String str = "<b>Please note:</b> Detailed information is not available<br></br>for international Orders, standard shipping<br></br>information will be displayed. If you wish to use<br></br>our standard order tracking service, <a href=''>please click here</a>.";
		text_2.setText(Html.fromHtml(str));
		text_2.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
	}

	@Override
	public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			player.loadVideo(VIDEO_ID);

		}
	}
}