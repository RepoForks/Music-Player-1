package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncRetriever;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncUpdater;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogNegativeClick;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnRenameDialogPositiveClick;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class RenamePlaylistDialogFragment extends BaseDialogFragment implements PlaylistNameAsyncRetriever.Callback, PlaylistNameAsyncUpdater.Callback{

    private static String PLAYLIST_ID_ARG_KEY = "playlist_id_key";

    public static RenamePlaylistDialogFragment newInstance(long playlistId) {
        Bundle bundle = new Bundle();
        bundle.putLong(PLAYLIST_ID_ARG_KEY, playlistId);
        RenamePlaylistDialogFragment fragment = new RenamePlaylistDialogFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rename_dialog, container);
        mInputView = (EditTextWithValidation)v.findViewById(R.id.dialog_input);
        mRenameButton = (Button)v.findViewById(R.id.dialog_positive_button);
        mRenameButton.setEnabled(false);
        mCancelButton = (Button)v.findViewById(R.id.dialog_negative_button);
        mCancelButton.setEnabled(false);
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
        mCancelButton.setOnClickListener(new OnDialogNegativeClick(this));
    }

    @Override
    public void updated() {
        dismiss();
    }
}
