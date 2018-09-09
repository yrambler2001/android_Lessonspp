package yrambler2001.lessons;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Юра on 31.03.2017.
 */

public class Teachers extends Fragment {

    ListView lvTeachers;
    static TeacherListAdapter adapter;

    public static Teachers newInstance() {
        return new Teachers();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void update() {
        adapter.update(MainActivity.ja);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teachers, container, false);
        lvTeachers =  view.findViewById(R.id.lv_lesson);



        adapter = new TeacherListAdapter(getActivity());
        lvTeachers.setAdapter(adapter);
        //lvTeachers.setAdapter(adapter);


        return view;
    }
}