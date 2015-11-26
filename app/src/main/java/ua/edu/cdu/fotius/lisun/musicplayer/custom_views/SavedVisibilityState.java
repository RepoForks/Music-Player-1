package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

public class SavedVisibilityState extends View.BaseSavedState{

    private final String TAG = getClass().getSimpleName();

    int visibility;

    public SavedVisibilityState(Parcelable superState) {
        super(superState);
    }

    public SavedVisibilityState(Parcel in) {
        super(in);
        visibility = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(visibility);
    }

    public static final Parcelable.Creator<SavedVisibilityState> CREATOR = new Parcelable.Creator<SavedVisibilityState>() {
        @Override
        public SavedVisibilityState createFromParcel(Parcel in) {
            return new SavedVisibilityState(in);
        }

        @Override
        public SavedVisibilityState[] newArray(int size) {
            return new SavedVisibilityState[0];
        }
    };
}
