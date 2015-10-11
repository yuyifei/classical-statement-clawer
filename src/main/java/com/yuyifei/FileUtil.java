package com.yuyifei;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by yuyifei on 10/9/15.
 */
public class FileUtil {
    public static void mkdir(File outPath) {
        try {
            FileUtils.forceMkdir(outPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
