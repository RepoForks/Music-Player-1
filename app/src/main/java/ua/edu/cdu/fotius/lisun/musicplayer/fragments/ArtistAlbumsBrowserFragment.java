package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AbstractCursorLoaderFactory;

public class ArtistAlbumsBrowserFragment extends AlbumsBrowserFragment{

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
