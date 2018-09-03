package yrambler2001.lessons;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Юра on 13.02.2016.
 */
public class DownloadLessonsTask extends AsyncTask<Void, Void, JSONArray>
{
	Context context;
	boolean ok;

	public DownloadLessonsTask(Context context)
	{
		super();
		this.context = context;
	}

	protected JSONArray doInBackground(Void... v)
	{
		TableGetter.init();

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://yrambler2001.narod.ru/yrambler2001/LessonsPlus/Lessons.json");
		try
		{
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200)
			{
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {builder.append(line);}
			} else {ok = false;}
			FileWriter fw = new FileWriter(new File(context.getFilesDir(), "lessons"));
			fw.write(builder.toString());
			fw.close();
			this.ok = true;
			return new JSONArray(builder.toString());
		} catch (Exception e)
		{
			this.ok = false;
			e.printStackTrace();
			return new JSONArray();
		}
	}

	protected void onPostExecute(JSONArray downloaded)
	{
		if (this.ok)
		{
			MainActivity.show(context, "Оновлено");
			MainActivity.ja = downloaded;
		} else
		{MainActivity.show(context, "Помилка оновлення");}
	}
}


