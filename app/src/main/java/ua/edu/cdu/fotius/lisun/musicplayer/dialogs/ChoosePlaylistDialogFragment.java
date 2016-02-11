package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.AddToPlaylistAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistsQueryAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogPlaylistClick;

public class ChoosePlaylistDialogFragment extends BaseDialogFragment
        implements PlaylistsQueryAsyncTask.Callback, AddToPlaylistAsyncTask.Callback {

    private static String TRACKS_IDS_KEY = "tracks_ids_key";

    public static ChoosePlaylistDialogFragment newInstance(long[] trackIDs) {
        Bundle arguments = new Bundle();
        arguments.putLongArray(TRACKS_IDS_KEY, trackIDs);
        ChoosePlaylistDialogFragment fragment = new ChoosePlaylistDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private ListView mListView;
    private long[] mTracksIds = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTracksIds = arguments.getLongArray(TRACKS_IDS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_choose_playlist, null);
        mListView = (ListView) v.findViewById(R.id.list);
        new PlaylistsQueryAsyncTask(this, mTracksIds, this).execute();
        return v;
    }

    @Override
    public void queryCompleted(Map<String, Long> namesToIds, List<String> names) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
        mListView.setAdapter(adapter);
        AddToPlaylistAsyncTask addTask = new AddToPlaylistAsyncTask(this, mTracksIds, this);
        mListView.setOnItemClickListener(new OnDialogPlaylistClick(addTask, namesToIds));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void insertCompleted(String playlistName, int quantityInserted) {
        dismiss();
        notifyUser(playlistName, quantityInserted);
    }

    //conscious duplicate in CreateNewPlaylistDialog
    private void notifyUser(String playlistName, int addedQuantity) {
        String ending = ((addedQuantity == 1) ? "" : "s");
        String message = getString(R.string.tracks_added_to_playlist,
                addedQuantity, ending, playlistName);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
