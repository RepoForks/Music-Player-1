package ua.edu.cdu.fotius.lisun.musicplayer.utils;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class CheckedIndicatorController {
    //FIXME: this is quick fix, in future think up all "checking" logic
    public static void setCheckedIndicator(View parent, boolean value) {
        View checkedIndicator = parent.findViewById(R.id.checked_indicator);
        if(checkedIndicator == null) return;
        if(value) {
            checkedIndicator.setVisibility(View.VISIBLE);
        } else {
            checkedIndicator.setVisibility(View.GONE);
        }
    }
}
