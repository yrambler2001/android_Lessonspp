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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    List<String> lLnB = new ArrayList<>();


    int special = 0;
    Interval[] LnB;
    DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");

    public static DayFragment newInstance(int page) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putInt("viewDay", page);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateLnB() {
        if (MainActivity.firstGroup.size() > 0) {
            List<Map<String, String>> l = MainActivity.firstGroup.get(0);
            for (int i = 0; i < l.size(); i++) {
                for (String a : l.get(i).get("lesson").split("-"))
                    if (!lLnB.contains(a)) lLnB.add(a);
            }
            LnB = new Interval[lLnB.size()];
            for (int i = 1; i < lLnB.size(); i++) {
                LnB[i - 1] = new Interval(new LocalTime(lLnB.get(i - 1)).toDateTimeToday().withDayOfWeek(viewDay), new LocalTime(lLnB.get(i)).toDateTimeToday().withDayOfWeek(viewDay));
            }
        }
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


        final ListAdapter adapter = new ListAdapter(getActivity());
        listLesson.setAdapter(adapter);

        uiu =
                new UIUpdater().setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        DateTime now = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));
                        //time.setProgressDrawable(drLesson);

                        //try {
                        //JSONArray thisDay = MainActivity.ja.getJSONArray(viewDay - 1);
                        updateLnB();
                        if (MainActivity.firstGroup.size()>0){
                        List lThisDay = MainActivity.firstGroup.get(Math.min(5, viewDay));

                        adapter.update(lThisDay);
                        adapter.setNow(0);

                        adapter.setSpecial((now.getWeekOfWeekyear() & 1) == 1);

                        if (viewDay > 5) {
                            lesson.setText("Пар не було");
                            //time.setMax(0);
                            //time.setProgress(0);
                        } else if (now.isAfter(LnB[(lThisDay.size() / 2 - 1) * 2].getEnd())) {
                            //else if ((now.isAfter(LnB[LnB.length - 1].getEnd()))) {
                            lesson.setText("Пари закінчились");
                            //time.setProgress(thisDay.length());
                            //time.setMax(thisDay.length());
                            adapter.setProgress((lThisDay.size() / 2) * 4000);
                        } else if (now.isBefore(LnB[0].getStart())) {
                            lesson.setText("Пари ще не почались");
                            //time.setMax(thisDay.length());
                            //time.setProgress(0);
                            adapter.setProgress(0);
                        } else
                            for (int i = 0; i < LnB.length-1; i++) {
                            if (LnB[i]==null)
                            {
                                Log.i("FF","FF");
                            }
                                if (LnB[i].contains(now)) {
                                    adapter.setCountdown((int) ((LnB[i].getEndMillis() - now.getMillis()) / 60000));
                                    //Log.i("ms", (int)((LnB[i].getEndMillis()-now.getMillis())/60000)+"");
                                    if ((i & 1) == 0) {
                                        lesson.setText("Пара " + ((i + 2) / 2));
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
                        //} catch (Exception e) {
                        //    e.printStackTrace();
                        //}
                    }
                }});
        uiu.startUpdates();
        return view;
    }
}