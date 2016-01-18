package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.views.DragNDropListView;

public class OnDropNowPlayingListener implements DragNDropListView.DropListener{

    private MediaPlaybackServiceWrapper mServiceWrapper;

    public OnDropNowPlayingListener(MediaPlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onDrop(HashMap<Long, Integer> initialIdToPositionMap,
                       long[] newQueue, int from, int to) {
        mServiceWrapper.moveQueueItem(from, to);
    }
}
