package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;


import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public abstract class AbstractCursorLoaderCreator {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    protected AbstractCursorLoaderCreator(Context context) {
        mContext = context;
    }

    public CursorLoader createCursorLoader() {
        //DatabaseUtils.queryParamsInLog(getUri(), getProjection(), getSelection(), getSelectionArgs());

        return new CursorLoader(mContext, getUri(), getProjection(),
                getSelection(), getSelectionArgs(), getSortOrder());
    }

    public abstract Uri getUri();
    public abstract String[] getProjection();
    public abstract String getSelection();
    public abstract String[] getSelectionArgs();
    public abstract String getSortOrder();
}
