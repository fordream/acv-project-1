package z.base;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.json.JSONObject;

import z.base.api.RequestMethod;
import z.base.api.RestClient;
import z.base.api.RestClientCallBack;
import android.app.Activity;
import android.content.Context;

public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
	public static final void onCreate(Activity activity) {
		Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(activity));
	}

	public static final void sendCrash(final Context context) {

		RestClient client = new RestClient("http://vnpmanager.esy.es/api/crash.php", context);
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("stack.trace")));
			String trace = null;
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line != null) {
					builder.append(line).append("\n");
				}
			}
		} catch (Exception exception) {

		}

		client.addParam("appname", context.getPackageName());
		Calendar calendar = Calendar.getInstance();
		client.addParam(
				"time",
				String.format("%s-%s-%s %s:%s", calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY),
						calendar.get(Calendar.MINUTE)));
		client.addParam("log", builder.toString());
		if (builder.length() > 0) {

			RestClientCallBack restClientCallBack = new RestClientCallBack(context) {
				@Override
				public void onSucssesOnBackground(int responseCode, String responseMessage, String response) {
					super.onSucssesOnBackground(responseCode, responseMessage, response);
					try {
						JSONObject jsonObject = new JSONObject(response);
						if ("1".equals(jsonObject.getString("status"))) {
							context.deleteFile("stack.trace");
						}
					} catch (Exception exception) {
					}
				}

			};

			client.execute(RequestMethod.POST, restClientCallBack, true);
		}

	}

	private Thread.UncaughtExceptionHandler defaultUEH;

	private Activity app = null;

	public CrashExceptionHandler(Activity app) {
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		this.app = app;
	}

	public void uncaughtException(Thread t, Throwable e) {
		StackTraceElement[] arr = e.getStackTrace();
		String report = e.toString() + "\n\n";
		report += "--------- Stack trace ---------\n\n";
		for (int i = 0; i < arr.length; i++) {
			report += "    " + arr[i].toString() + "\n";
		}
		report += "-------------------------------\n\n";

		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		report += "--------- Cause ---------\n\n";
		Throwable cause = e.getCause();
		if (cause != null) {
			report += cause.toString() + "\n\n";
			arr = cause.getStackTrace();
			for (int i = 0; i < arr.length; i++) {
				report += "    " + arr[i].toString() + "\n";
			}
		}
		report += "-------------------------------\n\n";

		try {
			FileOutputStream trace = app.openFileOutput("stack.trace", Context.MODE_PRIVATE);
			trace.write(report.getBytes());
			trace.close();
		} catch (IOException ioe) {
		}

		defaultUEH.uncaughtException(t, e);
	}
}