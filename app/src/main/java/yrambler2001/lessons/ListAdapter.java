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

import java.util.List;
import java.util.Map;

/**
 * Created by Юра on 14.12.2015.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private int now = 0;
    private int progress = 0;
    private int countdown = 0;
    private int updown = 0;
    private boolean isbreak = false;

    //JSONArray arr;
    private List<Map<String, String>> list;


    ListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return (list == null) ? 0 : list.size() / 2;
    }

    @Override
    public Object getItem(int position) {
        try {
            return list.get(position * 2);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void update(List<Map<String, String>> lNew) {
        list = lNew;
    }

    void setNow(int now) {
        this.now = now;
    }

    void setBreak(boolean isbreak) {
        this.isbreak = isbreak;
    }

    void setProgress(int progress) {
        this.progress = progress;
    }

    void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    void setUpdown(int updown) {
        this.updown = updown;
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

        TextView text1 =  lay.getChildAt(2).findViewById(R.id.upper);
        TextView text2 = lay.getChildAt(2).findViewById(R.id.bottom_left);
        TextView text3 = lay.getChildAt(2).findViewById(R.id.bottom_right);
        TextView text4 = lay.getChildAt(2).findViewById(R.id.time);
        TextView num = ((TextView) ((RelativeLayout) lay.getChildAt(0)).getChildAt(1));
        DonutProgress pb = ((DonutProgress) ((RelativeLayout) lay.getChildAt(0)).getChildAt(0));

        num.setText(String.valueOf(position+1));
        pb.setFinishedStrokeColor(context.getResources().getColor(R.color.dnt_finished_color_lesson));
        num.setTextSize(30);
        int pg = (position) * 4000;
        if ((progress - pg) <= 0) {
            pb.setProgress(0);
        } else if ((progress - pg) < 4000) {
            if (isbreak)
                pb.setFinishedStrokeColor(context.getResources().getColor(R.color.dnt_finished_color_break));
            pb.setProgress(progress - pg);
            num.setText(countdown + "хв");
            num.setTextSize(14);
        } else {
            pb.setProgress(4000);
        }

        if (position == (now - 1)) //1=0
            lay.setBackground(context.getResources().getDrawable(R.drawable.corners_list_chosen));
        else lay.setBackground(context.getResources().getDrawable(R.drawable.corners_list));

        try {

            Map<String, String> hm = list.get(position * 2 + updown);
            text1.setText(hm.get("lesson"));
            text2.setText(hm.get("type"));
            text3.setText(hm.get("cabinet"));
            text4.setText(MainActivity.firstGroup.get(0).get(position*2).get("lesson"));
            if (text2.getText()=="--"){text2.setVisibility(View.GONE);}
            if (text3.getText()=="--"){text3.setVisibility(View.GONE);}
            if (text1.getText()=="--"){text1.setVisibility(View.GONE);}

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lay;
    }
}