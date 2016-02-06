package ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity;

import android.content.Context;

import java.util.ArrayList;

public class TitleValidatorsSetCreator extends BaseValidatorsSetCreator{

    public TitleValidatorsSetCreator(Context context) {
        super(context);
    }

    @Override
    public ArrayList<BaseValidator> create() {
        ArrayList<BaseValidator> validators = new ArrayList<BaseValidator>();
        validators.add(new EmptyStringValidator(mContext));
        return validators;
    }
}
