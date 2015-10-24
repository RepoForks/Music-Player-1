package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

public abstract class BaseFragmentItemClickListener implements AdapterView.OnItemClickListener{

    protected Context mContext;
    protected CursorAdapter mCursorAdapter;

    public BaseFragmentItemClickListener(Context context, CursorAdapter cursorAdapter) {
        mContext = context;
        mCursorAdapter = cursorAdapter;
    }

    @Override
    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
