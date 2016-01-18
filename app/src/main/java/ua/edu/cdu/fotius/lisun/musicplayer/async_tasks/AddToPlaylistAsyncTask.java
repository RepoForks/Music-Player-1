package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistNameIdTuple;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class AddToPlaylistAsyncTask extends AsyncTaskWithProgressBar {

    private PlaylistNameIdTuple mPlaylistInfo;
    private long[] mTrackIds;

    public AddToPlaylistAsyncTask(Fragment fragment, PlaylistNameIdTuple playlistInfo, long[] tracksIds) {
        super(fragment);
        mPlaylistInfo = playlistInfo;
        mTrackIds = tracksIds;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground();

        long playlistId = mPlaylistInfo.getId();

        Context context = mFragmentWrapper.getActivity();
        if (context == null) {
            return null;
        }

        if (playlistId == AudioStorage.WRONG_ID) {
                playlistId =
                        DatabaseUtils.createPlaylist(context, mPlaylistInfo.getName());
        }

        int addedQuantity = DatabaseUtils.addToPlaylist(context,
                playlistId, mTrackIds);

        return addedQuantity;
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        if (obj != null) {
            int addedQuantity = (Integer) obj;
            notifyUser(addedQuantity, mPlaylistInfo.getName());
        }
    }

    //conscious duplicate in CreateNewPlaylistDialog
    private void notifyUser(int addedQuantity, String playlistName) {
        Context context = mFragmentWrapper.getActivity();
        if(context == null) {
            return;
        }

        String ending = ((addedQuantity == 1) ? "" : "s");
        String message = context.getResources().getString(R.string.tracks_added_to_playlist,
                addedQuantity, ending, playlistName);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
