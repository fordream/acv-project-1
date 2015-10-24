package z.base;

import java.util.HashMap;
import java.util.Map;

import org.com.atmarkcafe.sky.customviews.charting.MTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.TextView;

public class Fonts {
	private Context context;
	private Map<String, Typeface> maps = new HashMap<String, Typeface>();
	private String[] fonts = new String[] {//
	"Roboto-Bold_15.ttf"//
			, "Roboto-Light_15.ttf"//
	};//

	private Fonts() {

	}

	private void init(Context context) {
		if (context != null && this.context == null) {
			this.context = context;
		}
	}

	public void loadFonts(final LoadFontCallBacK loadFontCallBacK) {
		new AsyncTask<String, String, String>() {
			public String doInBackground(String[] params) {
				for (String font : fonts) {
					if (maps.get(font) == null) {
						maps.put(font, CommonAndroid.FONT.getTypefaceFromAsset(font, context));
					}

				}
				return null;
			};

			public void onPostExecute(String result) {

				if (loadFontCallBacK != null) {
					loadFontCallBacK.onSuccess();
				}
			};
		}.execute("");
	}

	private static Fonts instance = new Fonts();

	public static Fonts getIntance(Context context) {
		if (instance == null) {
			instance = new Fonts();
		}

		instance.init(context);

		return instance;
	}

	public interface LoadFontCallBacK {
		public void onSuccess();
	}

	public void setFont(TextView mTextView, int txtFont) {
		if (mTextView != null && maps.containsKey(fonts[txtFont])) {
			//mTextView.setTypeface(maps.get(fonts[txtFont]));
		}
	}
}