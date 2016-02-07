package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public abstract class BaseDialog {
    protected Fragment mFragment;
    protected long[] mIds;

    public BaseDialog(Fragment fragment, long[] ids) {
        mFragment = fragment;
        mIds = ids;
    }

    public abstract void show();
}
