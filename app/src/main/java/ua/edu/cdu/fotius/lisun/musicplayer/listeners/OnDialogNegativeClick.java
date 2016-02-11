package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.DialogFragment;
import android.view.View;

public class OnDialogNegativeClick implements View.OnClickListener{

    private DialogFragment mDialogFragment;

    public OnDialogNegativeClick(DialogFragment fragment) {
        mDialogFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        mDialogFragment.dismiss();
    }
}
