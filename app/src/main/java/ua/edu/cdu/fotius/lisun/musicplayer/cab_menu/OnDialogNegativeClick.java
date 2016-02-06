package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.DialogInterface;

public class OnDialogNegativeClick implements DialogInterface.OnClickListener{
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
