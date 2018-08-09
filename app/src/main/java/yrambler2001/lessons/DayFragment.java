package yrambler2001.lessons;


import android.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;

import java.util.TimeZone;

/**
 * Created by Юра on 31.03.2017.
 */

public class DayFragment extends Fragment {

    private int viewDay;
    ListView listLesson;
    UIUpdater uiu;
    //ProgressBar time;
    TextView lesson;//, //length;
    Drawable drLesson, drBreak;

    String sLnB[] = {"08:30", "09:15", "09:25", "10:10", "10:30", "11:15", "11:25", "12:10", "12:30", "13:15", "13:25", "14:10", "14:20", "15:05"};
    int special = 0;
    Interval[] LnB = new Interval[sLnB.length - 1];
    DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");

    public static DayFragment newInstance(int page) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putInt("viewDay", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDay = getArguments().getInt("viewDay", 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dayfragment, container, false);
        listLesson = (ListView) view.findViewById(R.id.lv_lesson);
        drLesson = getResources().getDrawable(R.drawable.dr_lesson);
        drBreak = getResources().getDrawable(R.drawable.dr_break);
        //time = (ProgressBar) view.findViewById(R.id.pb_time);
        lesson = (TextView) view.findViewById(R.id.tv_lesson);
        //length = (TextView) view.findViewById(R.id.tv_length);
        lesson.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "simpleiriska.ttf"));

        for (int i = 1; i < sLnB.length; i++) {
            LnB[i - 1] = new Interval(new LocalTime(sLnB[i - 1]).toDateTimeToday().withDayOfWeek(viewDay), new LocalTime(sLnB[i]).toDateTimeToday().withDayOfWeek(viewDay));
        }
      /*LnB[0]  = 2017-09-05T08:30:00.000+03:00/2017-09-05T09:15:00.000+03:00
         LnB[1]  = 2017-09-05T09:15:00.000+03:00/2017-09-05T09:25:00.000+03:00
        LnB[2]  = 2017-09-05T09:25:00.000+03:00/2017-09-05T10:10:00.000+03:00
         LnB[3]  = 2017-09-05T10:10:00.000+03:00/2017-09-05T10:30:00.000+03:00
        LnB[4]  = 2017-09-05T10:30:00.000+03:00/2017-09-05T11:15:00.000+03:00
         LnB[5]  = 2017-09-05T11:15:00.000+03:00/2017-09-05T11:25:00.000+03:00
        LnB[6]  = 2017-09-05T11:25:00.000+03:00/2017-09-05T12:10:00.000+03:00
         LnB[7]  = 2017-09-05T12:10:00.000+03:00/2017-09-05T12:30:00.000+03:00
        LnB[8]  = 2017-09-05T12:30:00.000+03:00/2017-09-05T13:15:00.000+03:00
         LnB[9]  = 2017-09-05T13:15:00.000+03:00/2017-09-05T13:25:00.000+03:00
        LnB[10] = 2017-09-05T13:25:00.000+03:00/2017-09-05T14:10:00.000+03:00
         LnB[11] = 2017-09-05T14:10:00.000+03:00/2017-09-05T14:20:00.000+03:00
        LnB[12] = 2017-09-05T14:20:00.000+03:00/2017-09-05T15:05:00.000+03:00*/

        final ListAdapter adapter = new ListAdapter(getActivity());
        listLesson.setAdapter(adapter);

        uiu =
                new UIUpdater().setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        DateTime now = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));
                        //time.setProgressDrawable(drLesson);

                        try {
                            JSONArray thisDay = MainActivity.ja.getJSONArray(viewDay - 1);

                            adapter.update(thisDay);
                            adapter.setNow(0);

                            adapter.setSpecial((now.getWeekOfWeekyear() & 1) == 1);

                            if (viewDay > 5) {
                                lesson.setText("Уроків не було");
                                //time.setMax(0);
                                //time.setProgress(0);
                            } else if (now.isAfter(LnB[(thisDay.length() - 1) * 2].getEnd())) {
                                //else if ((now.isAfter(LnB[LnB.length - 1].getEnd()))) {
                                lesson.setText("Уроки закінчились");
                                //time.setProgress(thisDay.length());
                                //time.setMax(thisDay.length());
                                adapter.setProgress((thisDay.length()) * 4000);
                            } else if (now.isBefore(LnB[0].getStart())) {
                                lesson.setText("Уроки ще не почались");
                                //time.setMax(thisDay.length());
                                //time.setProgress(0);
                                adapter.setProgress(0);
                            } else
                                for (int i = 0; i < LnB.length; i++) {
                                    if (LnB[i].contains(now)) {
                                        adapter.setCountdown((int) ((LnB[i].getEndMillis() - now.getMillis()) / 60000));
                                        //Log.i("ms", (int)((LnB[i].getEndMillis()-now.getMillis())/60000)+"");
                                        if ((i & 1) == 0) {
                                            lesson.setText("Урок " + ((i + 2) / 2));
                                            adapter.setNow((i + 2) / 2);
                                            int m = (int) (LnB[i].toDurationMillis() / 1000);
                                            int n = ((int) (now.getMillis() - LnB[i].getStartMillis()) / 1000);
                                            adapter.setProgress((((i + 2) / 2) - 1) * 4000 + (n * 4000 / m));
                                            adapter.setBreak(false);
                                        } else {
                                            //time.setProgressDrawable(drBreak);
                                            lesson.setText("Перерва " + ((i + 1) / 2));
                                            int m = (int) (LnB[i].toDurationMillis() / 1000);
                                            int n = ((int) (now.getMillis() - LnB[i].getStartMillis()) / 1000);
                                            adapter.setProgress((((i + 2) / 2)) * 4000 + (n * 4000 / m));
                                            adapter.setBreak(true);
                                        }
                                        lesson.append(" " + fmt.print(LnB[i].getStart()) + "-" + fmt.print(LnB[i].getEnd()) + " " + LnB[i].toDurationMillis() / 60000 + "хв");
                                        //time.setMax((int) (LnB[i].toDurationMillis() / 1000));
                                        //time.setProgress((int) ((now.getMillis() - LnB[i].getStartMillis()) / 1000));
                                        //length.setText((now.getMillis() - LnB[i].getStartMillis()) / 60000 + " з " + LnB[i].toDurationMillis() / 60000 + "хв");
                                    }
                                }
                            //if (time.getMax() <= thisDay.length())
                            //length.setText(time.getProgress() + " з " + time.getMax());
                            lesson.setText(lesson.getText().toString().replace('і', 'i'));
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                });
        uiu.startUpdates();
        return view;
    }
}