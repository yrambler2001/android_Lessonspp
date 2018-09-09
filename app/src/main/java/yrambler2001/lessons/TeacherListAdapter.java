package yrambler2001.lessons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by Юра on 14.12.2015.
 */
public class TeacherListAdapter extends BaseAdapter {

    private Context context;


    //JSONArray arr;
    private JSONArray list=MainActivity.ja;


    TeacherListAdapter(Context context) {
        this.context = context;
    }



    @Override
    public int getCount() {
        return (list == null) ? 0 : list.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return list.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void update(JSONArray aNew) {
        list = aNew;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout lay;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            lay = (LinearLayout) inflater.inflate(
                    R.layout.teachers_list_item, null);
        } else lay = (LinearLayout) convertView;
                 lay.setBackground(context.getResources().getDrawable(R.drawable.corners_list));

        TextView text1 =  lay.findViewById(R.id.first);
        TextView text2 = lay.findViewById(R.id.second);
        try {
            text1.setText(list.getJSONObject(position).get("lesson").toString());
            text2.setText(list.getJSONObject(position).get("teacher").toString().replace(",","\r\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lay;
    }
}