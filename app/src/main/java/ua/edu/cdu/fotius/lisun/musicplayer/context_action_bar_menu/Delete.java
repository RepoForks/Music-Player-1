package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.ConfirmationAndDeletionDialog;

public class Delete extends Command{

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    //TODO: maybe move to super
    public Delete(Context context) {
        mContext = context;
    }

    @Override
    public void execute() {
        new ConfirmationAndDeletionDialog(mContext).show();
    }
}
