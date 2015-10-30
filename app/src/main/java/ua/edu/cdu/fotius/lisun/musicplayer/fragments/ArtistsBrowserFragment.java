package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.AlbumsDetalizationActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AbstractCursorLoaderFactory;

public class ArtistsBrowserFragment extends BaseFragment {

    public static final String TAG = "artists";
    public static final String ARTIST_ID_KEY = "artist_id_key";

    public ArtistsBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CursorAdapter createAdapter() {
        ArtistCursorLoaderFactory loaderFactory = (ArtistCursorLoaderFactory) mLoaderFactory;
        String[] from = new String[]{loaderFactory.getArtistColumnName(),
                loaderFactory.getAlbumsQuantityColumnName(),
                loaderFactory.getTracksQuantityColumnName()};
        int[] to = new int[]{R.id.artist_name, R.id.albums_quantity, R.id.tracks_quantity};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_artist_list, from, to);
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        return new ArtistCursorLoaderFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artists_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new OnArtistClick(getActivity(), mCursorAdapter));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderFactory.getCursorLoader();
    }
}
