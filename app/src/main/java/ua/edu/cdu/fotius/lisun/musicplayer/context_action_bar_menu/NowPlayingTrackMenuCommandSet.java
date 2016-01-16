package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class NowPlayingTrackMenuCommandSet extends BaseTrackMenuCommandSet {
    public NowPlayingTrackMenuCommandSet(Fragment fragment, PlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    protected void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        super.initMinimalGroup(minimalGroup);
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        minimalGroup.add(REMOVE_FROM_PLAY_QUEUE_ID, resources.getString(R.string.cab_remove_from_queue),
                R.mipmap.ic_launcher, new RemoveFromPlayQueue(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }
}
