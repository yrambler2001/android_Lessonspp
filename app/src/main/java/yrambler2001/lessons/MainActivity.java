package yrambler2001.lessons;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends Activity {



    static JSONArray ja = new JSONArray();
    static List<List<Map<String, String>>> firstGroup= new ArrayList<>(), secondGroup = new ArrayList<>();
static int group=1;

    static String days[]=new String[]{"Вчителі","Понеділок","Вівторок","Середа","Четвер","П'ятниця","Субота","Неділя","Налаштування"};
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
        vpPager.setOffscreenPageLimit(9);

        StringBuilder tchrs = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(this.getFilesDir(), "teachers")));
            String line;
            while ((line = br.readLine()) != null) {
                tchrs.append(line);
                tchrs.append("\r\n");
            }
            br.close();
            ja = new JSONArray(tchrs.toString());
        } catch (Exception e) {
            show("Перший запуск\r\nСписок має завантажитись з інтернету");
        }


        StringBuilder lsns = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(this.getFilesDir(), "lessons_tntu")));
            String line;
            while ((line = br.readLine()) != null) {
                lsns.append(line);
                lsns.append("\r\n");
            }
            br.close();
            TableGetter.upd(Jsoup.parse(lsns.toString()));
        } catch (Exception e) {
            show("Перший запуск\r\nСписок має завантажитись з інтернету");
        }
        StringBuilder on = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(this.getFilesDir(), "settings")));
            String line;
            while ((line = br.readLine()) != null) {
                on.append(line);
                on.append("\r\n");
            }
            br.close();

            MainActivity.group = on.toString().contains("1") ? 1 : 2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        new DownloadLessonsTask(this).execute();
    }
}
