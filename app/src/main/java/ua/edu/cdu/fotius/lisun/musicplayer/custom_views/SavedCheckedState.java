package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by andrei on 24.11.2015.
 */
public class SavedCheckedState extends View.BaseSavedState{

    int checkedMenuItemId;

    public SavedCheckedState(Parcelable superState) {
        super(superState);
    }

    public SavedCheckedState(Parcel in) {
        super(in);
        checkedMenuItemId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(checkedMenuItemId);
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
