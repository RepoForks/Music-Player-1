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
    protected BaseSimpleCursorAdapter createAdapter() {
        String[] from = new String[] {AudioStorage.Album.ALBUM, AudioStorage.Album.ARTIST};
        int[] to = new int[] { R.id.album_title, R.id.artist_name };

        AbstractAlbumCursorLoaderFactory loaderFactory = (AbstractAlbumCursorLoaderFactory) mLoaderFactory;
        return new AlbumArtCursorAdapter(getActivity(),
                R.layout.grid_item_albums, from, to, R.id.album_art, loaderFactory.getAlbumIdColumnName());
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        Bundle extras = mExtras;
        if(extras == null) {
            return new AlbumsCursorLoaderFactory(getActivity());
        }

        long artistId = mExtras.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY, PARENT_ID_IS_NOT_SET);
        if(artistId != PARENT_ID_IS_NOT_SET) {
            return new ArtistAlbumsCursorLoaderFactory(getActivity(), artistId);
        } else {
            /*this won't be executed, but keep this as "default value"*/
            return new AlbumsCursorLoaderFactory(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums_browser, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.grid_container);
        gridView.setAdapter(mCursorAdapter);
        AbstractAlbumCursorLoaderFactory loaderFactory = (AbstractAlbumCursorLoaderFactory) mLoaderFactory;
        gridView.setOnItemClickListener(new OnAlbumClick(getActivity(), mCursorAdapter, mExtras,loaderFactory.getAlbumIdColumnName()));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderFactory.getCursorLoader();
    }
}
