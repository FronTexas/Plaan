package com.example.plaan;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;

public class Weather {
	String URL = "http://api.openweathermap.org/data/2.5/weather?q=Jakarta,id";

	public Weather() {
		new Read().execute("text");
	}

	private JSONObject constructJSONObject() throws ClientProtocolException,
			IOException, JSONException, URISyntaxException {
		URI website = new URI(
				"http://api.openweathermap.org/data/2.5/weather?q=Jakarta,id");
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		request.setURI(website);
		HttpResponse response = client.execute(request);
		HttpEntity e = response.getEntity();
		String data = EntityUtils.toString(e);
		
		JSONObject jObj = new JSONObject(data);
		
		Location loc = new Location("");
		
		JSONObject corrdObj = jObj.getJSONObject("coord");
		
		loc.setLatitude((float)jObj.getDouble("lat"));
		loc.setLongitude((float) jObj.getDouble("lon"));
		
		JSONObject sysObj = jObj.getJSONObject("sys");

		return jObj;
	}

	public class Read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				JSONObject lastTweet = constructJSONObject();
				return lastTweet.getString(params[0]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		}

	}

}
