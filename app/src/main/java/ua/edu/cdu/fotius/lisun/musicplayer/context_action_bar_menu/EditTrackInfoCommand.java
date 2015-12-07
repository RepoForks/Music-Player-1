package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditInfoActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditTrackInfoFragment;

public class EditTrackInfoCommand extends Command{

    public EditTrackInfoCommand(Context context) {
        super(context, null);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {

        //TODO: make this in all Command subclasses
        if((idsOverWhichToExecute == null) || (idsOverWhichToExecute.length != 1)) {
            throw new IllegalStateException("idsOverWhichToExecute == null " +
                    "or idsOverWhichToExecute.length != 1");
        }

        Intent intent = new Intent(mContext, EditInfoActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(EditTrackInfoFragment.TRACK_ID_KEY, idsOverWhichToExecute[0]);
        intent.putExtras(extras);
        //TODO: need to set trackId;
        mContext.startActivity(intent);
    }
}
