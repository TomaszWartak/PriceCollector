package com.dev4lazy.pricecollector.csv2pojo;

import android.os.Environment;

import com.dev4lazy.pricecollector.utils.AppHandle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {

    private BufferedReader reader;

    public void openReader(String csvFileName) {
        try {
            File directory = getDocumentDirectory();
            // todo start test
            String path = directory + File.separator + csvFileName;
            /* todo
            coś mi się wydaje, że albo:
            - spróbować jednak w publicznym katalogu to zapisać
            - lub skopiować w aplikacji z publicznego
             */
            // todo end test
            FileReader fileReader = new FileReader(directory + File.separator + csvFileName);
            reader = new BufferedReader(fileReader);
        } catch (FileNotFoundException ex0) {
            ex0.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    private File getPrivateDirectory() {
        return AppHandle.getHandle().getFilesDir();
    }

    private File getDocumentDirectory() {
        if (isExternalStorageReadable()) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        } else {
            return null;
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public String readCsvLine() {
        String csvLine = null;
        try {
            csvLine = reader.readLine();
            // jeżeli część dziesiętną oddziela ",", to zamiana na "."
            // żeby konwersja ze String na Double przeszła
            if (csvLine!=null) {
                csvLine = csvLine.replace(",", ".");
            }
        } catch(IOException ex1) {
            ex1.printStackTrace();
            try {
                if(reader!= null) {
                    reader.close();
                }
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
        return csvLine;
    }

    public void closeReader() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
    }

}