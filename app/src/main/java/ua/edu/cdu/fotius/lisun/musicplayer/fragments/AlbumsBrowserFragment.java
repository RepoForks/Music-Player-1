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
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AbstractCursorLoaderFactory;

public class AlbumsBrowserFragment extends BaseLoaderFragment {

    public static final String TAG = "albums";
    public static final  String ALBUM_ID_KEY = "album_id_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseSimpleCursorAdapter createAdapter() {
        String[] from = new String[] {AudioStorage.Album.ALBUM, AudioStorage.Album.ARTIST};
        int[] to = new int[] { R.id.album_title, R.id.artist_name };

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.grid_item_albums, from, to);
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        return new AlbumsCursorLoaderFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums_browser, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.grid_container);
        gridView.setAdapter(mCursorAdapter);
        gridView.setOnItemClickListener(new OnAlbumClick(getActivity(), mCursorAdapter));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderFactory.getCursorLoader();
    }
}
