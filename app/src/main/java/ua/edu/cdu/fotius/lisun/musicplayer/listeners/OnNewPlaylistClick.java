package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.dialogs.ChoosePlaylistDialogFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.dialogs.NewPlaylistDialogFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.StringConstants;

public class OnNewPlaylistClick implements View.OnClickListener {

    private ChoosePlaylistDialogFragment mFragment;
    private long[] mTracksIds;

    public OnNewPlaylistClick(ChoosePlaylistDialogFragment fragment, long[] tracksIds) {
        mTracksIds = tracksIds;
        mFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        NewPlaylistDialogFragment fragment = NewPlaylistDialogFragment.newInstance(mTracksIds);
        fragment.show(mFragment.getFragmentManager(), StringConstants.DIALOG_FRAGMENT_TAG);
        mFragment.dismiss();
    }
}
