package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.DragNDropListView;

public class OnDropPlaylistTrackListener implements DragNDropListView.DropListener {

    private Context mContext;
    private long mPlaylistId;

    public OnDropPlaylistTrackListener(Context context, long playlistId) {
        mContext = context;
        mPlaylistId = playlistId;
    }

    @Override
    public void onDrop(HashMap<Long, Integer> initialIdToPositionMap,
                       long[] newQueue, int from, int to) {
        ChangeOrderInPlaylistAsyncTask task =
                new ChangeOrderInPlaylistAsyncTask(mContext,
                        mPlaylistId, initialIdToPositionMap, newQueue);
        task.execute();
    }
}
