package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.DragNDropListView;

public class OnDropPlaylistTrackListener implements DragNDropListView.DropListener {

    private Fragment mFragment;
    private long mPlaylistId;

    public OnDropPlaylistTrackListener(Fragment fragment, long playlistId) {
        mFragment = fragment;
        mPlaylistId = playlistId;
    }

    @Override
    public void onDrop(HashMap<Long, Integer> initialIdToPositionMap,
                       long[] newQueue, int from, int to) {
        ChangeOrderInPlaylistAsyncTask task =
                new ChangeOrderInPlaylistAsyncTask(mFragment,
                        mPlaylistId, initialIdToPositionMap, newQueue);
        task.execute();
    }
}
