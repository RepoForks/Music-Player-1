package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.Fragment;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.ChangeOrderInPlaylistAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.views.DragNDropListView;

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
