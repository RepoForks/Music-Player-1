package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class RemoveFromPlaylistAsyncTask extends AsyncTaskWithProgressBar {

    private long mPlaylistId;
    private long[] mTracksIds;

    public RemoveFromPlaylistAsyncTask(Fragment fragment, long playlistId, long[] tracksIds) {
        super(fragment);
        mPlaylistId = playlistId;
        mTracksIds = tracksIds;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);

        Activity activity = mFragmentWrapper.getActivity();
        if (activity != null) {
            ContentResolver resolver = activity.getContentResolver();
            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", mPlaylistId);
            String where =
                    AudioStorage.PlaylistMember.TRACK_ID + " IN (" + DatabaseUtils.makeInBody(mTracksIds) + ")";

            resolver.delete(uri, where, null);
        }
        return null;
    }
}
