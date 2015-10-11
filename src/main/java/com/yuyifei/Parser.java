package com.yuyifei;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yuyifei on 10/9/15.
 */
public class Parser {

    public void execute(String html) {
        // read properties file.
        PropertiesUtils.load("properties/download_sort_flag.pro");
        String sortFlag = PropertiesUtils.get("sort_number");

        String basePath = "名人名言";
        File data = new File(basePath);
        if (!data.exists()) {
            FileUtil.mkdir(data);
        }

        Document doc = Jsoup.parse(html);

        String titleId = "MainMenu";
        Element div = doc.getElementById(titleId);
        Elements lis = div.select("> ul > li");

        int sortNumber = Integer.parseInt(sortFlag);
        for (int index=sortNumber; index < lis.size(); ++index) {
            Element li = lis.get(index);
            String sort = li.select("> a").first().attr("title");

            File file = new File(basePath + "/" + sort);
            if(!file.exists()) {
                FileUtil.mkdir(file);
            }

            String nextUrl = li.select("> a").first().attr("href");

             // get the real url we need.
            HashMap<String, String> realUrlList = null;
            realUrlList = new HashMap<String, String>();
            parserNextPage(nextUrl, realUrlList);

            // download the content we need.
            downloadContent(realUrlList, file);

            // record sort number.
            PropertiesUtils.set("sort_number", Integer.toString(index));
        }
    }

    private void downloadContent(HashMap<String, String> realUrlList, File baseDir) {
        int count = 0;
        for (Map.Entry<String, String> entry : realUrlList.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            System.out.println("Download:" + value);
            String html = Html.getHtmlFromUrl(value);
            Document doc = Jsoup.parse(html);
            Elements ps = doc.select("p");
            ArrayList<String> content = new ArrayList<String>();
            for (Element p : ps) {
                content.add(p.text());
                content.add("\n");
            }

            // write content to file.
            File file = new File (baseDir.getAbsolutePath() + "/" + key + ".txt");
            try {
                FileUtils.writeLines(file, content);

                ++count;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //break;
        }

        System.out.println(count);
    }


    private void parserNextPage(String url, HashMap<String, String> realUrlList) {
        UrlQueue.addElem(url);

        while (!UrlQueue.isEmpty()) {
            // get a new url.
            String currentUrl = UrlQueue.outElem();

            System.out.println("nextUrl:" + currentUrl);

            String html = Html.getHtmlFromUrl(currentUrl);

            Document doc = Jsoup.parse(html);
            Elements divs = doc.select("div.PostHead");
            for (Element div : divs) {
                Element a = div.select("a").first();
                realUrlList.put(a.text(), a.attr("href"));
            }

            // when the the page is last page , we should stop to parser the nextPage url.
            if (currentUrl.endsWith("-1.html")) {
                continue;
            }
            // get the next page url if it exist.
            Element paperSpan = doc.select("span.now-page").first();
            String nextPageUrl = paperSpan.nextElementSibling().attr("href");
            UrlQueue.addElem(nextPageUrl);

            //System.out.println(paperSpan.text());
            //System.out.println(paperSpan.nextElementSibling().attr("href"));
        }


    }
}
