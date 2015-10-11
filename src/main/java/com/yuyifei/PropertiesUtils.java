package com.yuyifei;

import java.io.*;
import java.util.Properties;

/**
 * function:Get properties file attributes value
 */
public class PropertiesUtils {

    private static Properties pros = null;//Properties Object
    private static String fileName = null;//properties file

    /**
     * Loading files to Properties object
     * @param fileName
     */
    public static void load(String fileName){
        // record propertirs file.
        PropertiesUtils.fileName = fileName;

        pros = new Properties();//Create a Properties object
        /**
         * Get properties file attributes for the current value
         * of the inter-class compiled bytecode
         * in file list of files of the documents
         * called fileName input stream
         */
        FileInputStream fis = null;
        try {
            // create a file stream.
            fis = new FileInputStream(fileName);

            // load properties.
            pros.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Through the attribute name to get attribute values
     * @param key
     * @return
     */
    public static String get(String key){
        if (pros == null) {
            System.out.println("read properties failed!");
            return null;
        }
        return pros.getProperty(key);//Through the key specific get attribute value
    }


    public static void set(String key, String value) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(PropertiesUtils.fileName);

            pros.setProperty(key, value);

            pros.store(out, "Update" + key + "value");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
