package yrambler2001.lessons;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Юра on 31.03.2017.
 */

public class Settings extends Fragment {

    public static Settings newInstance() {
        return new Settings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_settings, container, false);
        LabeledSwitch ls = view.findViewById(R.id.switchGr);
ls.setOn(MainActivity.group==2);

        ls.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                MainActivity.group = isOn ? 2 : 1;
                try {
                    FileWriter fw = new FileWriter(new File(getActivity().getApplicationContext().getFilesDir(), "settings"));
                    fw.write((isOn ? 2 : 1) + "");
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        LabeledSwitch lsM = view.findViewById(R.id.switchM);
        lsM.setOn(DayFragment.updown==1);
        lsM.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                DayFragment.updown=(DayFragment.updown==1) ? 0 : 1;
            }
        });
        TextView tv=view.findViewById(R.id.textViewDGB1);
        tv.setText(DayFragment.debugText1);
        return view;
    }
}