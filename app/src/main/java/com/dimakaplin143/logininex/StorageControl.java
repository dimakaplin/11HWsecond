package com.dimakaplin143.logininex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class StorageControl {


    private SharedPreferences bgSettings;

    private Context ctx;
    private FileOutputStream outputStream;

    final String LOG_TAG = "myLogs";
    private static final String PREFERENCES = "bgImg";

    public StorageControl(Context ctx) {
        this.ctx = ctx;
        bgSettings = this.ctx.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }

    private File getPrivateStorageDir(String filename) {

        File file = new File(ctx.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), filename);
        if (!ctx.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS).mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    private void saveIn(String filename, String fileContent) {
        try
        {
            outputStream = ctx.openFileOutput(filename, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            bw.write(fileContent);
            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveEx(String filename, String fileContent) {
        File file = getPrivateStorageDir(filename);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(fileContent);
            bw.close();
            Log.d(LOG_TAG, "Файл записан " + fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSharedBool(String varName, boolean var) {
        SharedPreferences.Editor editor = bgSettings.edit();
        editor.putBoolean(varName, var);
        editor.apply();
    }

    public boolean getSharedBool(String varName) {
        return bgSettings.getBoolean(varName, false);
    }

    public void saveFile(String filename, String fileContent, boolean exSave) {

        if(exSave) {
            saveEx(filename, fileContent);
        } else {
            saveIn(filename, fileContent);
        }

    }

    private String readEx(String filename) {
        File file = getPrivateStorageDir(filename);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "READ " + e.getMessage());
        }
        Log.e(LOG_TAG, "READED " + text.toString());
        return text.toString();
    }

    private String readIn(String filename) {
        StringBuilder text = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(ctx.openFileInput(filename));

            BufferedReader br = new BufferedReader(inputStreamReader);

            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public String readFile(String filename, boolean exSave) {
        String result;
        if(exSave) {
            result = readEx(filename);
        } else {
            result = readIn(filename);
        }
        return result;
    }

    public boolean isExist(String filename, boolean exSave) {
        if(exSave) {
            return getPrivateStorageDir(filename).exists();
        } else {
            return new File(ctx.getFilesDir(), filename).exists();
        }

    }


}
