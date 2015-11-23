package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractAlbumCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AlbumsCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.ArtistAlbumsCursorLoaderCreator;

public class AlbumsBrowserFragment extends BaseLoaderFragment {

    public static final String TAG = "albums";
    public static final  String ALBUM_ID_KEY = "album_id_key";
    private Bundle mExtras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mExtras = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseSimpleCursorAdapter createCursorAdapter() {
        String[] from = new String[] {AudioStorage.Album.ALBUM, AudioStorage.Album.ARTIST};
        int[] to = new int[] { R.id.album_title, R.id.artist_name };

        AbstractAlbumCursorLoaderCreator loaderFactory = (AbstractAlbumCursorLoaderCreator) mLoaderFactory;
        return new AlbumArtCursorAdapter(getActivity(),
                R.layout.grid_item_albums, from, to, R.id.album_art, loaderFactory.getAlbumIdColumnName());
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        Bundle extras = mExtras;
        if(extras == null) {
            return new AlbumsCursorLoaderCreator(getActivity());
        }

        long artistId = mExtras.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY, WRONG_ID);
        if(artistId != WRONG_ID) {
            return new ArtistAlbumsCursorLoaderCreator(getActivity(), artistId);
        } else {
            /*this won't be executed, but keep this as "default value"*/
            return new AlbumsCursorLoaderCreator(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums_browser, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.grid_container);
        gridView.setAdapter(mCursorAdapter);
        AbstractAlbumCursorLoaderCreator loaderFactory = (AbstractAlbumCursorLoaderCreator) mLoaderFactory;
        gridView.setOnItemClickListener(new OnAlbumClickListener(getActivity(), mCursorAdapter, mExtras,loaderFactory.getAlbumIdColumnName()));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderFactory.createCursorLoader();
    }
}
