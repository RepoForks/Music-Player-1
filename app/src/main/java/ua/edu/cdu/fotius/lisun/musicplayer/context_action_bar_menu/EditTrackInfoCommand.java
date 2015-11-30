package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;
import android.content.Intent;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

public class EditTrackInfoCommand extends Command{

    public EditTrackInfoCommand(Context context) {
        super(context, null);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        Intent intent = new Intent(mContext, EditInfoActivity.class);
        //TODO: need to set trackId;
        mContext.startActivity(intent);
    }
}
