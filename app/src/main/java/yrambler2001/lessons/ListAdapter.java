package yrambler2001.lessons;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 * Created by Юра on 14.12.2015.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private int now = 0;
    private int progress = 0;
    private int countdown = 0;
    private boolean special = true;
    private boolean isbreak = false;

    JSONArray arr;


    public ListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return (arr == null) ? 0 : arr.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return arr.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(JSONArray arrNew) {
        arr = arrNew;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public void setBreak(boolean isbreak) {
        this.isbreak = isbreak;
    }

    public void setProgress(int progress) {this.progress = progress;}

    public void setCountdown(int countdown) {this.countdown = countdown;}

    public void setSpecial(boolean special) {
        this.special = special;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout lay;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            lay = (LinearLayout) inflater.inflate(
                    R.layout.list_item, null);
        } else lay = (LinearLayout) convertView;

        TextView text1 = ((TextView) ((LinearLayout) lay.getChildAt(2)).getChildAt(0));
        TextView text2 = ((TextView) ((LinearLayout) lay.getChildAt(2)).getChildAt(1));
        TextView num = ((TextView) ((RelativeLayout) lay.getChildAt(0)).getChildAt(1));
        DonutProgress pb = ((DonutProgress) ((RelativeLayout) lay.getChildAt(0)).getChildAt(0));

        num.setText(String.valueOf(position + 1));
        pb.setFinishedStrokeColor(context.getResources().getColor(R.color.dnt_finished_color_lesson));
        num.setTextSize(30);
        int pg = (position) * 4000;
        if ((progress - pg) <= 0) {
            pb.setProgress(0);
        } else if ((progress - pg) < 4000) {
            if (isbreak) pb.setFinishedStrokeColor(context.getResources().getColor(R.color.dnt_finished_color_break));
            pb.setProgress(progress - pg);
            num.setText(countdown+"хв");
            num.setTextSize(14);
        } else {
            pb.setProgress(4000);
        }

        if (position == (now - 1)) //1=0
            lay.setBackground(context.getResources().getDrawable(R.drawable.corners_list_chosen));
        else lay.setBackground(context.getResources().getDrawable(R.drawable.corners_list));

        try {

            Object jn = arr.get(position);
            if (jn instanceof JSONArray) {
                text1.setText(((JSONArray) jn).getJSONObject(special ? 1 : 0).getString("Lesson"));
                text2.setText(((JSONArray) jn).getJSONObject(special ? 1 : 0).getString("Cabinet"));
            } else {
                text1.setText(((JSONObject) jn).getString("Lesson"));
                text2.setText(((JSONObject) jn).getString("Cabinet"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lay;
    }
}