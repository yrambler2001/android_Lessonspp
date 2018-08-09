package yrambler2001.lessons;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TimeZone;

public class MainActivity extends Activity {



    static JSONArray ja = new JSONArray();
    static String days[]=new String[]{"Вчителі","Понеділок","Вівторок","Середа","Четвер","П'ятниця","Субота","Неділя"};
    public static String intToDay(int day) {
        return days[day-1];
    }

    public void show(final String msg) {
        show(this, msg);
    }

    public static void show(final Context context, final String msg) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.i("YUP", msg);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JodaTimeAndroid.init(this);

        ViewPager vpPager = findViewById(R.id.vpPager);
        vpPager.setAdapter(new PagerAdapter(getFragmentManager()));

        PagerSlidingTabStrip tabs = findViewById(R.id.tabs);
        tabs.setViewPager(vpPager);


        vpPager.setCurrentItem(new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault())).getDayOfWeek());
        vpPager.setOffscreenPageLimit(8);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(this.getFilesDir(), "lessons")));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\r\n");
            }
            br.close();
            ja = new JSONArray(text.toString());
        } catch (Exception e) {
            show("Перший запуск\r\nСписок має завантажитись з інтернету");
        }
        new DownloadLessonsTask(this).execute();
    }
}
