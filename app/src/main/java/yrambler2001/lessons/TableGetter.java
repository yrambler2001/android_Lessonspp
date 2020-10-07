package yrambler2001.lessons;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class TableGetter {


    static void init(Context context) {
        try {
            Document doc;
            String url = "http://tntu.edu.ua/?p=uk/schedule&s=fis-sp31";

            doc = Jsoup.connect(url).get();
            FileWriter fw = new FileWriter(new File(context.getFilesDir(), "lessons_tntu"));
            fw.write(doc.html());
            fw.close();
            upd(doc);
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
    }

    static void upd(Document doc) {

        Element script = doc.getElementById("ScheduleWeek")
                .getElementsByTag("table").last();
        DayFragment.updown = doc.getElementsByTag("h3").select(".Black").text().contains("перший") ? 0 : 1;
        DayFragment.debugText1="Насправді "+((DayFragment.updown==0)?"перший":"другий")+" тиждень\n\n\n\t App by Yuriy Synyshyn";
        script.getElementsByTag("tbody");
        Elements linesInTable = script.getElementsByTag("table").first().getElementsByTag("tr");
        //String[] sHeader = new String[20];
        int[] iHeader = new int[20];
        String[][][] sLesson = new String[20][20][3];
        int q;
        Elements headr = linesInTable.get(0).getElementsByTag("th");
        int k = 0;

        for (int h = 0; h < headr.size(); h++) {
            Element curel = headr.get(h);
            int a = curel.hasAttr("colspan") ? 2 : 1;
            iHeader[k] = a;
            //sHeader[k] = curel.text();
            k++;
            iHeader[k] = a;
            //sHeader[k] = curel.text();
            k++;
        }
        int v = 0;
        for (int z = 0; z < linesInTable.size(); z++) {
            boolean b = false;
            Elements fline = linesInTable.get(z).getElementsByTag("td");
            for (int h = 0; h < fline.size(); h++) {
                Element e = fline.get(h);
                Element ee = e.getElementsByTag("div").first();
                if (e.html().contains("LessonPeriod") && (!e.hasAttr("rowspan"))) {
                    b = true;
                }
                String[] s = new String[]{"--", "--", "--"};
                if (ee != null) s[0] = ee.text();
                for (q = 0; q < sLesson.length; q++)
                    if (sLesson[v][q][0] == null) {
                        sLesson[v][q] = s;
                        Elements eInfo = e.getElementsByClass("info");
                        if (eInfo.size() > 0) {
                            String[] sInfo = eInfo.first().html().split("<br>");
                            if (sInfo.length == 2) {
                                s[1] = sInfo[0].substring(0, sInfo[0].indexOf('\n'));
                                s[2] = sInfo[1].substring(sInfo[1].lastIndexOf('>') + 1).replaceFirst(" ", "");
                            }
                        }
                        if (iHeader[q] == 2) {
                            if (e.hasAttr("rowspan"))//вниз
                                sLesson[v + 1][q] = s;
                            if (e.hasAttr("colspan"))//вбік
                                sLesson[v][q + 1] = s;
                            if (e.hasAttr("colspan") && e.hasAttr("rowspan"))
                                sLesson[v + 1][q + 1] = s;
                        } else if (iHeader[q] == 1) {
                            sLesson[v][q + 1] = s;
                            if (e.hasAttr("rowspan")) {
                                sLesson[v + 1][q] = s;
                                sLesson[v + 1][q + 1] = s;
                            }
                        }
                        break;
                    }

                //Log.i("tbl", "OK h " + h);
            }
            if (b) {
                sLesson[v + 1] = sLesson[v];
                v++;
            }
            //Log.i("tbl", "OK v " + v);

            v++;
        }


        List<List<Map<String, String>>> firstGroup = new ArrayList<>(), secondGroup = new ArrayList<>();

        for (int i = 0; i < sLesson.length; i++) {
            List<Map<String, String>> f = new ArrayList<>(), s = new ArrayList<>();
            for (int j = 0; j < sLesson[0].length; j++)
                if (sLesson[j][i][0] != null) {
                    //Log.i("AA", j + " " + sLesson[j][i][0]);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("lesson", sLesson[j][i][0]);
                    hm.put("type", sLesson[j][i][1]);
                    hm.put("cabinet", sLesson[j][i][2]);
                    if (i % 2 == 0) f.add(hm);
                    else s.add(hm);
                }
            if (f.size() > 0) firstGroup.add(f);
            if (s.size() > 0) secondGroup.add(s);
            //Log.i("AA", i + "----");
        }
        MainActivity.firstGroup = firstGroup;
        MainActivity.secondGroup = secondGroup;


    }


}
