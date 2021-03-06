package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;
import android.content.res.Resources;
import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class QueueTrackMenuCommandSet extends BaseTrackMenuCommandSet {
    public QueueTrackMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    protected void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        super.initMinimalGroup(minimalGroup);
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        minimalGroup.add(REMOVE_FROM_PLAY_QUEUE_ID, resources.getString(R.string.cab_remove_from_queue),
                MenuItem.NO_ICON_ID, new RemoveFromPlayQueue(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }
}
