package com.dreamlin.gankvm.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {

    public static File getExternal() {
        return Environment.getExternalStorageDirectory();
    }

    public static String getExternalPath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

}
