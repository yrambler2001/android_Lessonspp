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
    int sum(int[] a, int cnt) {
        int s = 0;
        for (int i = 0; i < cnt; i++)
            s += a[i];
        return s;
    }

    public static void init() {
        try {
            Document doc;
            String url = "http://tntu.edu.ua/?p=uk/schedule&s=fis-sp11";
            doc = Jsoup.connect(url).get();
            Element script = doc.getElementById("ScheduleWeek")
                    .getElementsByTag("table").last();
            script.getElementsByTag("tbody");
            Elements linesInTable = script.getElementsByTag("table").first().getElementsByTag("tr");
            //int linesIn1Line = Integer.parseInt(linesInTable.get(0).getElementsByClass("LessonNumber").first().attr("rowspan"));
            //String period1 = linesInTable.get(0).getElementsByClass("LessonPeriod").first().text();
            List<Dictionary> a = new ArrayList<>();
            String[] sHeader = new String[20];
            int[] iHeader = new int[20];
            String[][] aa = new String[20][20];
            int q;
            Elements headr = linesInTable.get(0).getElementsByTag("th");
            int k = 0;

            for (int h = 0; h < headr.size(); h++) {
                Element curel = headr.get(h);
                if (curel.hasAttr("colspan")) {
                    iHeader[k] = 2;
                    sHeader[k] = curel.text();
                    k++;
                    iHeader[k] = 2;
                    sHeader[k] = curel.text();
                } else {
                    iHeader[k] = 1;
                    sHeader[k] = curel.text();
                    k++;
                    iHeader[k] = 1;
                    sHeader[k] = curel.text();
                }
                k++;
            }
            for (int v = 0; v < linesInTable.size(); v++) {
                //if (linesInTable.get(v).getElementsByTag())
                Elements fline = linesInTable.get(v).getElementsByTag("td");

                int iSpHor=0;

                for (int h = 0; h < fline.size(); h++) {
                    q = h;
                    Element e = fline.get(h);
                    Element ee = e.getElementsByTag("div").first();
                    String s = "--";
                    if (ee != null) s = ee.text();
                    for (q = 0; q < aa.length; q++)
                        if (aa[v][q] == null) {
                            aa[v][q] = s;

                            if (iHeader[q] == 2) {
                                if (e.hasAttr("rowspan"))//вниз
                                    aa[v + 1][q] = s;
                                if (e.hasAttr("colspan"))//вбік
                                    aa[v][q + 1] = s;
                                if (e.hasAttr("colspan") && e.hasAttr("rowspan"))
                                    aa[v + 1][q + 1] = s;
                                iSpHor+=2;
                            }
                            else if (iHeader[q] == 1) {
                                aa[v][q + 1] = s;
                                if (e.hasAttr("rowspan")) {
                                    aa[v + 1][q] = s;
                                    aa[v + 1][q + 1] = s;
                                }

                                aa[v][q + 1] = s;
                                iSpHor+=1;

                            }
                            break;
                        }
                    Log.i("tbl", "OK h " + h);
                }
                Log.i("tbl", "OK v " + v);

            }


            int aaa = 0;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
