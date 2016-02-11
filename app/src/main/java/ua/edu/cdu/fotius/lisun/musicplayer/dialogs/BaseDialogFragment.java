package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

public class BaseDialogFragment extends DialogFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }
}
