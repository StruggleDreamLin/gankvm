package com.dreamlin.gankvm.utils;

import android.os.AsyncTask;
import android.os.Build;

import java.lang.ref.WeakReference;

public class TaskUtils {

    public static <T, Params, Progress, Result> void executeAsyncTask(
            WeakAsyncTask<T, Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    public static abstract class WeakAsyncTask<T, Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

        public WeakReference<T> tWeak;

        public WeakAsyncTask(WeakReference<T> weakT) {
            tWeak = weakT;
        }

    }

}
