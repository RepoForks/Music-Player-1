package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class TrackMenuCommandSet extends BaseTrackMenuCommandSet {

    public TrackMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        super.initMinimalGroup(minimalGroup);
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        minimalGroup.add(ADD_TO_PLAY_QUEUE_ID, resources.getString(R.string.cab_menu_add_to_queue),
                R.mipmap.ic_launcher, new AddToPlayQueue(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }
}
