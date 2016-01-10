package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditInfoActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditTrackInfoFragment;

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

        Intent intent = new Intent(mFragment.getActivity(), EditInfoActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(EditTrackInfoFragment.TRACK_ID_KEY, idsOverWhichToExecute[0]);
        intent.putExtras(extras);
        //TODO: need to set trackId;
        mFragment.startActivity(intent);
    }
}
