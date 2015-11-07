package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.os.Bundle;

public class ArtistAlbumsBrowserFragment extends AlbumsBrowserFragment{

    public static final String TAG = "artist_albums";
    private long mAlbumId = PARENT_ID_IS_NOT_SET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null) {
            mAlbumId = arguments.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY, PARENT_ID_IS_NOT_SET);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        return new ArtistAlbumsCursorLoaderFactory(getActivity(), mAlbumId);
    }
}
