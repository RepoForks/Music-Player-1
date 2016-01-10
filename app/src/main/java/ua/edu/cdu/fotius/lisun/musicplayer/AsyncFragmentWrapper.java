package ua.edu.cdu.fotius.lisun.musicplayer;

import android.graphics.RadialGradient;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public class AsyncFragmentWrapper {

    private final String TAG = getClass().getSimpleName();

    private WeakReference<Fragment> mFragmentReference;

    public AsyncFragmentWrapper(Fragment fragment) {
        mFragmentReference = new WeakReference<Fragment>(fragment);
    }

    public ToolbarActivity getActivity() {
        Fragment fragment = mFragmentReference.get();
        if(fragment == null) {
            Log.d(TAG, "fragment == null");
            return null;
        }

        ToolbarActivity activity = (ToolbarActivity)fragment.getActivity();
        if(activity == null) {
            Log.d(TAG, "activity == null");
            return null;
        }

        return activity;
    }
}
