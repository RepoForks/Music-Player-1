package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.Context;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class AddToPlaylistAsyncTask extends AsyncTaskWithProgressBar {

    public interface Callback {
        public void insertCompleted(String playlistName, int quantityInserted);
    }

    private Callback mCallback;
    private String mPlaylistName;
    private long[] mTracksIds;

    public AddToPlaylistAsyncTask(Fragment fragment, long[] tracksIds, Callback callback) {
        super(fragment);
        mTracksIds = tracksIds;
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground();

        long playlistId = (long) params[0];
        mPlaylistName = (String) params[1];

        Context context = mFragmentWrapper.getActivity();
        if (context == null) {
            return null;
        }

        if (playlistId == AudioStorage.WRONG_ID) {
            playlistId =
                    DatabaseUtils.createPlaylist(context, mPlaylistName);
        }

        int addedQuantity = DatabaseUtils.addToPlaylist(context,
                playlistId, mTracksIds);

        return addedQuantity;
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        if (obj != null) {
            mCallback.insertCompleted(mPlaylistName, (Integer) obj);
        }
    }
}
