package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class OnTrackClickListener extends BaseFragmentItemClickListener {

    private MediaPlaybackServiceWrapper mServiceWrapper;
    private String mTrackIdColumn;

    public OnTrackClickListener(Context context, CursorAdapter cursorAdapter, MediaPlaybackServiceWrapper serviceWrapper,
                                String trackIdColumn) {
        super(context, cursorAdapter);
        mServiceWrapper = serviceWrapper;
        mTrackIdColumn = trackIdColumn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mServiceWrapper.playAll(mCursorAdapter.getCursor(), position, mTrackIdColumn);
    }
}
