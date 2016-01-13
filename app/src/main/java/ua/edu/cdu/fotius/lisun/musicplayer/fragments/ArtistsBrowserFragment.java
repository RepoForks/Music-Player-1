package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.ArtistMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.ArtistCursorLoaderCreator;

public class ArtistsBrowserFragment extends BaseListFragment {

    public static final String TAG = "artists";
    public static final String ARTIST_ID_KEY = "artist_id_key";

    protected ToolbarStateListener mToolbarStateListener;

    public ArtistsBrowserFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarStateListener = (ToolbarStateListener) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected IndicatorCursorAdapter createCursorAdapter() {
        ArtistCursorLoaderCreator loaderFactory = (ArtistCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderFactory.getArtistColumnName(),
                loaderFactory.getAlbumsQuantityColumnName(),
                loaderFactory.getTracksQuantityColumnName()};
        int[] to = new int[]{R.id.artist_name, R.id.albums_quantity, R.id.tracks_quantity};

        return new IndicatorCursorAdapter(getActivity(),
                R.layout.row_artist_list, from, to);
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        return new ArtistCursorLoaderCreator(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artists_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        ArtistCursorLoaderCreator loaderCreator =
                (ArtistCursorLoaderCreator) mLoaderCreator;
        listView.setOnItemClickListener(new OnArtistClickListener(getActivity(),
                mCursorAdapter, loaderCreator.getArtistIdColumnName(),
                loaderCreator.getArtistColumnName()));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, new ArtistMenuCommandSet(this, mServiceWrapper),
                loaderCreator.getArtistIdColumnName()));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    protected void setIndicator(PlaybackServiceWrapper serviceWrapper,
                                IndicatorCursorAdapter adapter,
                                AbstractCursorLoaderCreator loaderCreator) {
        ArtistCursorLoaderCreator creator =
                (ArtistCursorLoaderCreator) loaderCreator;
        adapter.setIndicatorFor(creator.getArtistIdColumnName(), serviceWrapper.getArtistID());
    }
}
