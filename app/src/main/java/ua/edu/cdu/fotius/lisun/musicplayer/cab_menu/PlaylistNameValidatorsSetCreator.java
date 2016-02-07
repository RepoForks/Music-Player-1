package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.EmptyStringValidator;

public class PlaylistNameValidatorsSetCreator extends BaseValidatorsSetCreator {
    public PlaylistNameValidatorsSetCreator(Context context) {
        super(context);
    }

    @Override
    public ArrayList<BaseValidator> create() {
        ArrayList<BaseValidator> validators = new ArrayList<BaseValidator>();
        validators.add(new EmptyStringValidator(mContext));
        return validators;
    }
}
