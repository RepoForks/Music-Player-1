package ua.edu.cdu.fotius.lisun.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.Objects;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public abstract class AsyncTaskWithProgressBar extends AsyncTask<Object, Object, Object> {

    /*note: this fragment should call setRetainInstance(true)*/
    //TODO: maybe create super class which calls setRetainInstance(true)
    protected AsyncFragmentWrapper mFragmentWrapper;

    public AsyncTaskWithProgressBar(Fragment fragment) {
        mFragmentWrapper = new AsyncFragmentWrapper(fragment);
    }

    @Override
    protected void onPreExecute() {
        ToolbarActivity activity = mFragmentWrapper.getActivity();
        if (activity != null) {
            activity.showProgress();
        }
    }

    @Override
    protected Object doInBackground(Object... params) {
        //TODO: debug
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        ToolbarActivity activity = mFragmentWrapper.getActivity();
        if (activity != null) {
            activity.hideProgress();
        }
    }
}
