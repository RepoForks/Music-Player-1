package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.Map;

import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.AddToPlaylistAsyncTask;

public class OnDialogPlaylistClick implements AdapterView.OnItemClickListener {
    private AddToPlaylistAsyncTask mAsyncTask;
    private Map<String, Long> mNamesToIds;

    public OnDialogPlaylistClick(AddToPlaylistAsyncTask asyncTask, Map<String, Long> namesToIds) {
        mAsyncTask = asyncTask;
        mNamesToIds = namesToIds;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String playlistName = (String) parent.getAdapter().getItem(position);
        long playlistId = mNamesToIds.get(playlistName);
        mAsyncTask.execute(playlistId, playlistName);
    }
}
