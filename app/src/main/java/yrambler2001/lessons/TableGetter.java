package yrambler2001.lessons;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class TableGetter {


    // Extract video URL

    public static void init() {
        try {
            Document doc;
            String url = "http://tntu.edu.ua/?p=uk/schedule&s=fis-sp11";
            doc = Jsoup.connect(url).get();
            Element script = doc.getElementById("ScheduleWeek")
                    .getElementsByTag("table").last();
            script.getElementsByTag("tbody");
            Elements linesInTable = script.getElementsByTag("tbody").first().getElementsByTag("tr");
            int linesIn1Line = Integer.parseInt(linesInTable.get(0).getElementsByClass("LessonNumber").first().attr("rowspan"));
            String period1 = linesInTable.get(0).getElementsByClass("LessonPeriod").first().text();
            List<Dictionary> a = new ArrayList<Dictionary>();
            String[][] aa = new String[11][20];
            int q=0;
            for (int v = 0; v < linesInTable.size(); v++) {
                Elements fline = linesInTable.get(v).getElementsByTag("td");
                for (int h = 0; h < fline.size(); h++) {
                    q=h;
                    Element e = fline.get(h);
                    Element ee = e.getElementsByTag("div").first();
                    String s = "--";
                    if (ee != null) {
                        if (ee.hasText())
                            s = ee.text();

                    }

                    if (aa[v][h] == null)
                        aa[v][h] = s;
                    else {
                        for (q = 0; q < aa[v].length; q++) {
                            if (aa[v][q] == null) {
                                aa[v][q] = s;
                                break;
                            }
                        }
                    }
                    if (e.hasAttr("rowspan"))
                        aa[v + 1][q] = s;
                    if (e.hasAttr("colspan"))
                        aa[v][q + 1] = s;
                    if (e.hasAttr("colspan") && e.hasAttr("rowspan"))
                        aa[v + 1][q + 1] = s;

                    //String s=
//if (e.hasAttr())
                }
            }



            int aaa = 0;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
