package z.base;

import android.content.Context;
import android.widget.ImageView;

import com.acv.rouge.R;

public class ImageLoaderUtils {
	private ImageLoader imageLoader;
	public static ImageLoaderUtils instance;

	public static ImageLoaderUtils getInstance(Context context) {
		return (instance == null ? (instance = new ImageLoaderUtils()) : instance).init(context);
	}

	private ImageLoaderUtils init(Context context) {
		if (imageLoader == null) {
			imageLoader = new ImageLoader(context);
		}
		imageLoader.updateContext(context);
		return this;
	}

	private ImageLoaderUtils() {

	}

	public void displayImageHome(String url, ImageView imageView) {

		if (imageView != null) {
			imageView.setImageResource(R.drawable.tranfer);
		}
		if (!CommonAndroid.isBlank(url)) {
			imageLoader.displayImage(url, imageView, false);
		}
	}
}