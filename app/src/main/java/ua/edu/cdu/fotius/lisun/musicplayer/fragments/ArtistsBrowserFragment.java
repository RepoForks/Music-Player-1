package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.ArtistMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.ArtistAlbumsCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.ArtistCursorLoaderCreator;

public class ArtistsBrowserFragment extends BaseLoaderFragment implements ServiceConnectionObserver {

    public static final String TAG = "artists";
    public static final String ARTIST_ID_KEY = "artist_id_key";

    //TODO: move to super
    private MediaPlaybackServiceWrapper mServiceWrapper;
    protected ToolbarStateListener mToolbarStateListener;

    public ArtistsBrowserFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToolbarStateListener = (ToolbarStateListener) activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    protected CursorAdapter createCursorAdapter() {
        ArtistCursorLoaderCreator loaderFactory = (ArtistCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderFactory.getArtistColumnName(),
                loaderFactory.getAlbumsQuantityColumnName(),
                loaderFactory.getTracksQuantityColumnName()};
        int[] to = new int[]{R.id.artist_name, R.id.albums_quantity, R.id.tracks_quantity};

        return new BaseSimpleCursorAdapter(getActivity(),
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
                mToolbarStateListener, listView, new ArtistMenuCommandSet(getActivity(), mServiceWrapper),
                loaderCreator.getArtistIdColumnName()));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public void ServiceConnected() {

    }

    @Override
    public void ServiceDisconnected() {

    }
}
