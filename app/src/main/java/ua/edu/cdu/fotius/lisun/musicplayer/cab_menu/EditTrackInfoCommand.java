package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.InfoEditorActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.InfoEditorFragment;

public class EditTrackInfoCommand extends Command {

    public EditTrackInfoCommand(Fragment fragment) {
        super(fragment, null);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {

        //TODO: make this in all Command subclasses
        if((idsOverWhichToExecute == null) || (idsOverWhichToExecute.length != 1)) {
            throw new IllegalStateException("idsOverWhichToExecute == null " +
                    "or idsOverWhichToExecute.length != 1");
        }

        Intent intent = new Intent(mFragment.getActivity(), InfoEditorActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(InfoEditorFragment.TRACK_ID_KEY, idsOverWhichToExecute[0]);
        intent.putExtras(extras);
        //TODO: need to set trackId;
        mFragment.startActivity(intent);
    }
}
