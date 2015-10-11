package com.yuyifei;

import java.io.File;

/**
 * Created by yuyifei on 10/8/15.
 */
public class Main {
    public static void main(String[] args) {
        String url = "http://www.lz13.cn/";

        // get the html.
        String html = Html.getHtmlFromUrl(url);

        // parser html
        new Parser().execute(html);

        System.out.print("Game over !!!");
    }

}

