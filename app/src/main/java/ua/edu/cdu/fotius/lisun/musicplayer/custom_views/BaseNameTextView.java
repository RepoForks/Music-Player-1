package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.widget.TextView;

public abstract class BaseNameTextView extends TextView{

    public BaseNameTextView(Context context) {
        super(context);
    }

    public BaseNameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseNameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setName(String name){
        if(name.equals(MediaStore.UNKNOWN_STRING)) {
            name = getUnknownName();
        }
        super.setText(name);
    }

    /*Template method pattern*/
    public abstract String getUnknownName();
}
