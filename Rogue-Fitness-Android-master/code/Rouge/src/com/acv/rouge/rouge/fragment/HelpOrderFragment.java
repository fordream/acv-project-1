package com.acv.rouge.rouge.fragment;

import z.base.BaseFragment;
import z.base.CommonAndroid;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.acv.rouge.R;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.services.RougeService;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;

@SuppressLint("SetJavaScriptEnabled")
public class HelpOrderFragment extends BaseFragment {
	private WebView videoview;
	private String linkYoutube = "";
	private WebView video;
	private static final String YOUTUBELINK = "https://www.youtube.com/embed/_UfFYjUcIaI?autoplay=1&vq=small&rel=0";
	private ProgressBar progress;
	private BroadcastReceiver updateLinkYoutube = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String message = intent.getStringExtra("message");

			if (CommonAndroid.isBlank(linkYoutube)) {
				linkYoutube = new SettingShareReferentDB(getActivity()).getLinkYouTobe();
			}
			if (!linkYoutube.equals(new SettingShareReferentDB(getActivity()).getLinkYouTobe())) {
				linkYoutube = new SettingShareReferentDB(getActivity()).getLinkYouTobe();
			}
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		video.loadUrl("about:blank");
		if (getActivity() != null) {
			getActivity().unregisterReceiver(updateLinkYoutube);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		linkYoutube = new SettingShareReferentDB(getActivity()).getLinkYouTobe();
	}

	@Override
	public void onResume() {
		super.onResume();

		getActivity().registerReceiver(updateLinkYoutube, new IntentFilter("updateLinkYouTobe"));
		video.loadUrl(YOUTUBELINK);
	}

	YouTubePlayer.OnInitializedListener youtubeListener = new OnInitializedListener() {

		@Override
		public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1, boolean arg2) {
			CommonAndroid.showDialogComfirm(getActivity(), "okie");
		}

		@Override
		public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
			CommonAndroid.showDialogComfirm(getActivity(), "false");
		}
	};

	@Override
	public void init(View view) {
		progress = CommonAndroid.getView(view, R.id.progress);
		videoview = CommonAndroid.getView(view, R.id.web_1);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		view.findViewById(R.id.main).setOnClickListener(null);

		ImageView img_1 = CommonAndroid.getView(view, R.id.img_1);
		video = CommonAndroid.getView(view, R.id.web_1);

		view.findViewById(R.id.rl_2).setVisibility(View.GONE);
		video.getSettings().setJavaScriptEnabled(true);
		video.getSettings().setPluginState(PluginState.ON);
		video.setWebChromeClient(new WebChromeClient() {

		});

		video.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);

				if (YOUTUBELINK.equals(failingUrl)) {
					video.loadUrl("about:blank");
				}

				progress.setVisibility(View.GONE);
			}
		});

		if (getArguments().containsKey("top")) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img_1.getLayoutParams();
			layoutParams.topMargin = getArguments().getInt("top");
			img_1.setLayoutParams(layoutParams);
		}

		TextView text_2 = (TextView) view.findViewById(R.id.text_2);
		String str = "<b>Please note:</b> Detailed information is not available<br></br>for international Orders, standard shipping<br></br>information will be displayed. If you wish to use<br></br>our standard order tracking service, <a href=''>please click here</a>.";
		text_2.setText(Html.fromHtml(str));
		text_2.setMovementMethod(LinkMovementMethod.getInstance());

		/**
		 * Call service for ger link youtube
		 */
		Intent service = new Intent(getActivity(), RougeService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.GETLINKYOUTOBE);
		service.putExtras(extras);
		getActivity().startService(service);
	}

	@Override
	public void onChangeLanguage() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (v.getId() == R.id.header_control_right) {
		} else if (v.getId() == R.id.img_1) {
		}
	}

	public HelpOrderFragment() {
	}

	@Override
	public int getLayout() {
		return R.layout.helporder;
	}

}
