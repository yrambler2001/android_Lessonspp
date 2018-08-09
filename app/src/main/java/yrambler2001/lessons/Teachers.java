package yrambler2001.lessons;


import android.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Юра on 31.03.2017.
 */

public class Teachers extends Fragment {

    ListView listLesson;


    public static Teachers newInstance() {
        Teachers fragment = new Teachers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teachers, container, false);
        listLesson = (ListView) view.findViewById(R.id.lv_lesson);



        //final ListAdapter adapter = new ListAdapter(getActivity());
         List<String> arrayList = Arrays.asList( "test1", "test2");


        listLesson.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList));
        //listLesson.setAdapter(adapter);


        return view;
    }
}