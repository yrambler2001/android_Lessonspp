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
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    TextView lesson;
    Drawable drLesson, drBreak;
    int hashcodes[] = new int[]{0, 0, 0, 0, 0};
    List<String> lLnB = new ArrayList<>();


    static int updown = 0;
    static String debugText1 = "";
    //static int group = 1; //1 or 2
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
            int hc = MainActivity.firstGroup.hashCode();
            if (hashcodes[0] != hc) {
                hashcodes[0]=hc;
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
        listLesson = view.findViewById(R.id.lv_lesson);
        drLesson = getResources().getDrawable(R.drawable.dr_lesson);
        drBreak = getResources().getDrawable(R.drawable.dr_break);
        lesson = view.findViewById(R.id.tv_lesson);
        lesson.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "simpleiriska.ttf"));


        final ListAdapter adapter = new ListAdapter(getActivity());
        listLesson.setAdapter(adapter);

        uiu =
                new UIUpdater().setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        DateTime now = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));

                        updateLnB();
                        List<List<Map<String, String>>> lst = (MainActivity.group == 1) ? MainActivity.firstGroup : MainActivity.secondGroup;
                        if ((lst.size() > 0)) {
                            List<Map<String, String>> lThisDay;
                            if (viewDay <= (lst.size() - 1)) {
                                lThisDay = lst.get(Math.min(lst.size() - 1, viewDay));

                                adapter.update(lThisDay);
                            } else lThisDay = new ArrayList<>();


                            adapter.setNow(0);

                            //adapter.setUpdown((now.getWeekOfWeekyear() & 1) == 1);
                            adapter.setUpdown(updown);
                            if (viewDay > 5) {
                                lesson.setText("Пар не було");

                            } else if (now.isAfter(LnB[(lThisDay.size() / 2 - 1) * 2].getEnd())) {
                                lesson.setText("Пари закінчились");

                                adapter.setProgress((lThisDay.size() / 2) * 4000);
                            } else if (now.isBefore(LnB[0].getStart())) {
                                lesson.setText("Пари ще не почались");
                                adapter.setProgress(0);
                            } else
                                for (int i = 0; i < LnB.length - 1; i++) {
                                    if (LnB[i] == null) {
                                        Log.i("FF", "FF");
                                    }
                                    if (LnB[i].contains(now)) {
                                        adapter.setCountdown((int) ((LnB[i].getEndMillis() - now.getMillis()) / 60000));
                                        if ((i & 1) == 0) {
                                            lesson.setText("Пара " + ((i + 2) / 2));
                                            adapter.setNow((i + 2) / 2);
                                            int m = (int) (LnB[i].toDurationMillis() / 1000);
                                            int n = ((int) (now.getMillis() - LnB[i].getStartMillis()) / 1000);
                                            adapter.setProgress((((i + 2) / 2) - 1) * 4000 + (n * 4000 / m));
                                            adapter.setBreak(false);
                                        } else {
                                            lesson.setText("Перерва " + ((i + 1) / 2));
                                            int m = (int) (LnB[i].toDurationMillis() / 1000);
                                            int n = ((int) (now.getMillis() - LnB[i].getStartMillis()) / 1000);
                                            adapter.setProgress((((i + 2) / 2)) * 4000 + (n * 4000 / m));
                                            adapter.setBreak(true);
                                        }
                                        lesson.append(" " + fmt.print(LnB[i].getStart()) + "-" + fmt.print(LnB[i].getEnd()) + " " + LnB[i].toDurationMillis() / 60000 + "хв");
                                    }
                                }
                            lesson.setText(lesson.getText().toString().replace('і', 'i'));
                            adapter.notifyDataSetChanged();
                            //} catch (Exception e) {
                            //    e.printStackTrace();
                            //}
                        }
                    }
                });
        uiu.startUpdates();
        return view;
    }
}