package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.Fragment;
import android.content.Context;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.dialogs.NewPlaylistDialogFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.StringConstants;

public class OnNewPlaylistClick implements View.OnClickListener {

    private Fragment mFragment;
    private long[] mTracksIds;

    public OnNewPlaylistClick(Fragment fragment, long[] tracksIds) {
        mTracksIds = tracksIds;
        mFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        NewPlaylistDialogFragment fragment = NewPlaylistDialogFragment.newInstance(mTracksIds);
        fragment.show(mFragment.getActivity().getFragmentManager(),
                StringConstants.DIALOG_FRAGMENT_TAG);
    }
}
