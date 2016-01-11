package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.ContentUris;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class AsRingtone extends Command {

    public AsRingtone(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        if (idsOverWhichToExecute.length == 1) {
            long trackId = idsOverWhichToExecute[0];
            Uri uri = setAsRingtoneAndReturnUri(trackId);
            new UpdateIsRingtoneAndNotifyUserAsyncTask(mFragment, uri, trackId).execute();
        }
    }

    private Uri setAsRingtoneAndReturnUri(long id) {
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        RingtoneManager.setActualDefaultRingtoneUri(mFragment.getActivity(), RingtoneManager.TYPE_RINGTONE, uri);
        return uri;
    }
}
