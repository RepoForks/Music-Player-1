package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.DragNDropListView;

public class OnDropNowPlayingListener implements DragNDropListView.DropListener{

    private PlaybackServiceWrapper mServiceWrapper;

    public OnDropNowPlayingListener(PlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onDrop(HashMap<Long, Integer> initialIdToPositionMap,
                       long[] newQueue, int from, int to) {
        mServiceWrapper.moveQueueItem(from, to);
    }
}
