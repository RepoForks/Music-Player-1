package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.content.Context;

import java.util.ArrayList;

public abstract class BaseValidatorsSetCreator {

    protected Context mContext;

    public BaseValidatorsSetCreator(Context context) {
        mContext = context;
    }

    public abstract ArrayList<BaseValidator> create();
}
