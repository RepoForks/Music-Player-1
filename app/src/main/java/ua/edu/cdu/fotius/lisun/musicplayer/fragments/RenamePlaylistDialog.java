package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncRetriever;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncUpdater;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.OnDialogNegativeClick;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.OnRenameDialogPositiveClick;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class RenamePlaylistDialog extends DialogFragment implements PlaylistNameAsyncRetriever.Callback, PlaylistNameAsyncUpdater.Callback{

    private static String PLAYLIST_ID_ARG_KEY = "playlist_id_key";

    public static RenamePlaylistDialog newInstance(long playlistId) {
        Bundle bundle = new Bundle();
        bundle.putLong(PLAYLIST_ID_ARG_KEY, playlistId);
        RenamePlaylistDialog fragment = new RenamePlaylistDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    private long mPlaylistId;
    private EditTextWithValidation mInputView;
    private Button mRenameButton;
    private Button mCancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null) {
            mPlaylistId = arguments.getLong(PLAYLIST_ID_ARG_KEY);
        }
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container);
        mInputView = (EditTextWithValidation)v.findViewById(R.id.dialog_input);
        mRenameButton = (Button)v.findViewById(R.id.rename_button);
        mCancelButton = (Button)v.findViewById(R.id.cancel_button);
        new PlaylistNameAsyncRetriever(this, mPlaylistId, this).execute();
        return v;
    }

    @Override
    public void queryCompleted(String playlistName) {
        mInputView.setInitialText(playlistName);
        mInputView.setSelection(playlistName.length());
        mInputView.setEnabled(true);

        mRenameButton.setEnabled(true);
        PlaylistNameAsyncUpdater asyncUpdater =
                new PlaylistNameAsyncUpdater(this, mPlaylistId, this);
        mRenameButton.setOnClickListener(
                new OnRenameDialogPositiveClick(this.getActivity(), asyncUpdater));

        mCancelButton.setEnabled(true);
    }

    @Override
    public void updated() {
        dismiss();
    }
}
