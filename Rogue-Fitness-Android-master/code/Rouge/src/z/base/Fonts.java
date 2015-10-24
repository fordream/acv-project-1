package z.base;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.TextView;

public class Fonts {
	private Context context;
	private Map<String, Typeface> maps = new HashMap<String, Typeface>();
	private String[] fonts = new String[] {//
	"fonts/helvetica-neue-bold-italic.ttf"//
			, "fonts/helvetica-neue-bold.ttf"//
			, "fonts/helvetica-neue-condensed-black.ttf"//
			, "fonts/helvetica-neue-condensed-bold.ttf"//
			, "fonts/helvetica-neue-italic.ttf"//
			, "fonts/helvetica-neue-light-italic.ttf"//
			, "fonts/helvetica-neue-light.ttf"//
			, "fonts/helvetica-neue-medium.ttf"//
			, "fonts/helvetica-neue-regular.ttf"//
			, "fonts/helvetica-neue-ultra-light-italic.ttf"//
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

	public void setFontNormal(TextView normalTextView) {
		if (normalTextView != null) {
			Typeface typeface = maps.get("fonts/helvetica-neue-light.ttf");
			if (typeface != null) {
				normalTextView.setTypeface(typeface);
			}
		}

	}

	public void setFontBold(TextView normalTextView) {
		if (normalTextView != null) {
			Typeface typeface = maps.get("fonts/helvetica-neue-medium.ttf");
			if (typeface != null) {
				normalTextView.setTypeface(typeface);
			}
		}

	}
}