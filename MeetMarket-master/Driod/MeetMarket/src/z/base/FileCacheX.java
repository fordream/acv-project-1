package z.base;

import java.io.File;

import android.content.Context;

public class FileCacheX {
	private File cacheDir;

	public FileCacheX(Context context) {
		// Find the dir to save cached images
		String path = "Android/data/" + context.getPackageName() + "/img";
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			// LazyList
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), path);
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for
		// the demo.
		String filename = url;//String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

}
