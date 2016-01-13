package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public class OnTrackClickListener extends BaseFragmentItemClickListener {

    private PlaybackServiceWrapper mServiceWrapper;
    private String mTrackIdColumnName;

    public OnTrackClickListener(Context context, CursorAdapter cursorAdapter, PlaybackServiceWrapper serviceWrapper,
                                String trackIdColumnName) {
        super(context, cursorAdapter);
        mServiceWrapper = serviceWrapper;
        mTrackIdColumnName = trackIdColumnName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mServiceWrapper.playAll(mCursorAdapter.getCursor(), position, mTrackIdColumnName);
    }
}
