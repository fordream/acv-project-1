package z.base;

import android.content.Intent;

/**
 * 
 * @author ACV-ANDROID-DEV-01
 * @version 1.0
 * @see save for action and key store.
 */
public class MeetMarketUtils {
	public final class ACTION {
		public static final String UPDATEORDERS = "UPDATEORDERS";
		public static final String UPDATEFEED = "UPDATEFEED";
		public static final String UPDATE_HOME = "UPDATE_HOME";
		public static final String UPDATEUIBYPUSH = "UPDATEUIBYPUSH";
		public static final String ACTION_LOGIN = "ACTION_LOGIN";
	}

	public final class KEY {
		public static final String STORE = "STORE";
		public static final String TYPE = "type";
	}

	public static final boolean ISDEBUG_PUSH = false;
	public static final boolean ISDEBUG_ORDER = false;
	public static final String VERSION = "16";
}