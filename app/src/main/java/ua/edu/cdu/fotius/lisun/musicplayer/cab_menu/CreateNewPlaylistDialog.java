package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.AddToPlaylistAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class CreateNewPlaylistDialog extends BaseDialog {


    private EditText mEditText;

    public CreateNewPlaylistDialog(Fragment fragment, long[] trackIds) {
        super(fragment, trackIds);
    }

    public void show() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mFragment.getActivity());
        View v = createLayout();
        if(v != null) {
            dialogBuilder.setView(v);
            dialogBuilder.setTitle(mFragment.getResources().getString(R.string.create_new_playlist_title));
            dialogBuilder.setPositiveButton(R.string.create_new_playlist_positive_button, mOnPositiveClick)
                    .setNegativeButton(R.string.dialog_negative_button, mOnNegativeClick);
            dialogBuilder.create().show();
        }
        //TODO: else
    }

    private View createLayout() {
        mEditText = new EditText(mFragment.getActivity());
        mEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mEditText.setHint(mFragment.getResources().getString(R.string.new_playlist_name_hint));

        LinearLayout layout = new LinearLayout(mFragment.getActivity());
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
            AddToPlaylistAsyncTask addTask =
                    new AddToPlaylistAsyncTask(mFragment, new PlaylistNameIdTuple(AudioStorage.WRONG_ID, name), mTrackIds);
            addTask.execute();
        }
    };

    private DialogInterface.OnClickListener mOnNegativeClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
}
