package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.IndicatorCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.ArtistMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.ArtistLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnArtistClickListener;

public class ArtistsFragment extends BaseFragment {

    public static final String TAG = "artists";
    public static final String ARTIST_ID_KEY = "artist_id_key";

    public ArtistsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected IndicatorCursorAdapter createCursorAdapter() {
        ArtistLoaderCreator loaderFactory = (ArtistLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderFactory.getArtistColumn(),
                loaderFactory.getAlbumsQuantityColumn(),
                loaderFactory.getTracksQuantityColumn()};
        int[] to = new int[]{R.id.artist_name, R.id.albums_quantity, R.id.tracks_quantity};

        return new IndicatorCursorAdapter(getActivity(),
                R.layout.row_artist_list, from, to);
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        return new ArtistLoaderCreator(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artists_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        ArtistLoaderCreator loaderCreator =
                (ArtistLoaderCreator) mLoaderCreator;
        listView.setOnItemClickListener(new OnArtistClickListener(getActivity(),
                mCursorAdapter, loaderCreator.getArtistIdColumn(),
                loaderCreator.getArtistColumn()));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), listView, new ArtistMenuCommandSet(this, mServiceWrapper),
                loaderCreator.getArtistIdColumn()));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                IndicatorCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator) {
        ArtistLoaderCreator creator =
                (ArtistLoaderCreator) loaderCreator;
        adapter.setIndicatorFor(creator.getArtistIdColumn(),
                serviceWrapper.getArtistID(), serviceWrapper.isPlaying());
    }
}
