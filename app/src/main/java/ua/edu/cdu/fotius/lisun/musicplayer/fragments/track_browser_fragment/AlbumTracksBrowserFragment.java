package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;

public class AlbumTracksBrowserFragment extends TrackBrowserFragment{

    public static final String TAG = "album_tracks";

    private long mAlbumID = PARENT_ID_IS_NOT_SET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null) {
            mAlbumID = arguments.getLong(AlbumsBrowserFragment.ALBUM_ID_KEY,
                    PARENT_ID_IS_NOT_SET);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        return new AlbumTracksCursorLoaderFactory(getActivity(), mAlbumID);
    }
}
