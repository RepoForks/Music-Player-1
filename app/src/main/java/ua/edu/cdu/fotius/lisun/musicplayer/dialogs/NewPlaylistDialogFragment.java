package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.AddToPlaylistAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistNameValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogNegativeClick;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogDeleteClick;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnNewPlaylistDialogPositiveClick;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class NewPlaylistDialogFragment extends SingleInputDialogFragment implements AddToPlaylistAsyncTask.Callback{
    private static String TRACKS_IDS_KEY = "tracks_ids_key";

    public static NewPlaylistDialogFragment newInstance(long[] trackIDs) {
        Bundle arguments = new Bundle();
        arguments.putLongArray(TRACKS_IDS_KEY, trackIDs);
        NewPlaylistDialogFragment fragment = new NewPlaylistDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private long[] mTracksIds = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTracksIds = arguments.getLongArray(TRACKS_IDS_KEY);
    }

    @Override
    public View.OnClickListener getOnPositiveClickListener() {
        AddToPlaylistAsyncTask asyncTask = new AddToPlaylistAsyncTask(this, mTracksIds, this);
        return new OnNewPlaylistDialogPositiveClick(getActivity(), asyncTask,
                new PlaylistNameValidatorsSetCreator(getActivity()));
    }

    @Override
    public View.OnClickListener getOnNegativeClickListener() {
        return new OnDialogNegativeClick(this);
    }

    @Override
    public void insertCompleted(String playlistName, int quantityInserted) {
        dismiss();
    }
}
