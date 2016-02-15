package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncRetriever;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncUpdater;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistNameValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogNegativeClick;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnRenameDialogPositiveClick;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class RenamePlaylistDialogFragment extends SingleInputDialogFragment
        implements PlaylistNameAsyncRetriever.Callback, PlaylistNameAsyncUpdater.Callback{

    private final String TAG = getClass().getSimpleName();

    private static String PLAYLIST_ID_ARG_KEY = "playlist_id_key";

    public static RenamePlaylistDialogFragment newInstance(long playlistId) {
        Bundle bundle = new Bundle();
        bundle.putLong(PLAYLIST_ID_ARG_KEY, playlistId);
        RenamePlaylistDialogFragment fragment = new RenamePlaylistDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private long mPlaylistId;

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
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if(savedInstanceState == null) {
            mPositiveButton.setEnabled(false);
            mNegativeButton.setEnabled(false);
            mInputView.setEnabled(false);
            new PlaylistNameAsyncRetriever(this, mPlaylistId, this).execute();
        }
        return v;
    }

    @Override
    public String getTitle() {
        return getString(R.string.rename_dialog_title);
    }

    @Override
    public View.OnClickListener getOnPositiveClickListener() {
        PlaylistNameAsyncUpdater asyncUpdater =
                new PlaylistNameAsyncUpdater(this, mPlaylistId, this);
        return new OnRenameDialogPositiveClick(getActivity(),
                asyncUpdater, new PlaylistNameValidatorsSetCreator(getActivity()));
    }

    @Override
    public View.OnClickListener getOnNegativeClickListener() {
        return new OnDialogNegativeClick(this);
    }

    @Override
    public void queryCompleted(String playlistName) {
        mInputView.setInitialText(playlistName);
        mInputView.setSelection(playlistName.length());
        mInputView.setEnabled(true);

        mPositiveButton.setEnabled(true);
        mNegativeButton.setEnabled(true);
    }

    @Override
    public void updated(String oldName, String newName) {
        dismiss();
        String message =
                getActivity().getResources().getString(R.string.rename_dialog_result_message,
                        oldName, newName);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
