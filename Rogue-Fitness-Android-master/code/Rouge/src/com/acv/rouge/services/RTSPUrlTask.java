package com.acv.rouge.services;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import z.base.LogUtils;
import android.os.AsyncTask;

public class RTSPUrlTask extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... urls) {
		String response = getRTSPVideoUrl(urls[0]);
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
	}

	public String getRTSPVideoUrl(String urlYoutube) {
		try {
			String gdy = "http://gdata.youtube.com/feeds/api/videos/";
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			String id = extractYoutubeId(urlYoutube);
			URL url = new URL(gdy + id);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			Document doc = dBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");
			String cursor = urlYoutube;
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node != null) {
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap<String, String> maps = new HashMap<String, String>();
					for (int j = 0; j < nodeMap.getLength(); j++) {
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					if (maps.containsKey("yt:format")) {
						String f = maps.get("yt:format");
						if (maps.containsKey("url"))
							cursor = maps.get("url");
						if (f.equals("1"))
							return cursor;
					}
				}
			}
			return cursor;
		} catch (Exception ex) {
			return urlYoutube;
		}
	}

	public String extractYoutubeId(String url) throws MalformedURLException {
		String query = new URL(url).getQuery();
		String[] param = query.split("&");
		String id = null;
		for (String row : param) {
			String[] param1 = row.split("=");
			if (param1[0].equals("v")) {
				id = param1[1];
			}
		}
		return id;
	}
}
