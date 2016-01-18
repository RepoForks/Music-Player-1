package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class PlaylistTrackMenuCommandSet extends TrackMenuCommandSet{

    private long mPlaylistId;

    public PlaylistTrackMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper, long playlistId) {
        super(fragment, serviceWrapper);
        mPlaylistId = playlistId;
    }

    @Override
    public void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        super.initMinimalGroup(minimalGroup);
        minimalGroup.add(REMOVE_FROM_PLAYLIST, resources.getString(R.string.cab_remove_from_playlist),
                R.mipmap.ic_launcher, new RemoveFromPlaylist(mFragment, mServiceWrapper, mPlaylistId),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }
}
