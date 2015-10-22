package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.AddToPlaylistCommand;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import ua.edu.cdu.fotius.lisun.musicplayer.DatabaseUtils;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class CreateNewPlaylistDialog extends BaseDialog {

    private final String TAG = getClass().getSimpleName();
    private EditText mEditText;

    public CreateNewPlaylistDialog(Context context, long[] trackIds, AddToPlaylistResultListener listener) {
        super(context, trackIds, listener);
    }

    public void show() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        View v = createLayout();
        if(v != null) {
            dialogBuilder.setView(v);
            dialogBuilder.setTitle(mContext.getResources().getString(R.string.create_new_playlist_title));
            dialogBuilder.setPositiveButton(R.string.create_new_playlist_positive_button, mOnPositiveClick)
                    .setNegativeButton(R.string.dialog_negative_button, mOnNegativeClick);
            dialogBuilder.create().show();
        }
        //TODO: else
    }

    private View createLayout() {
        mEditText = new EditText(mContext);
        mEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mEditText.setHint(mContext.getResources().getString(R.string.new_playlist_name_hint));

        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(mEditText);
        return layout;
    }

    private DialogInterface.OnClickListener mOnPositiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String name = mEditText.getText().toString();
            long newlyCreatedId = DatabaseUtils.createPlaylist(mContext, name);
            int addedQuantity = DatabaseUtils.addToPlaylist(mContext, newlyCreatedId, mTrackIds);
            mListener.addedToPlaylistUserNotification(addedQuantity, name);
        }
    };

    private DialogInterface.OnClickListener mOnNegativeClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
}
