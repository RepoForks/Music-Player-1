package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AlbumTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AllTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.ArtistAlbumTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.PlaylistCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.PlaylistTracksCursorLoaderCreator;

public class TrackBrowserFragment extends BaseLoaderFragment implements ServiceConnectionObserver {

    //TODO: refactor this to tracklist
    public static final String TAG = "tracks";

    protected MediaPlaybackServiceWrapper mServiceWrapper;
    protected ToolbarStateListener mToolbarStateListener;

    private Bundle mPassedArguments;

    public TrackBrowserFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToolbarStateListener = (ToolbarStateListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPassedArguments = getArguments();

        super.onCreate(savedInstanceState);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    protected CursorAdapter createCursorAdapter() {
        AbstractTracksCursorLoaderCreator loaderFactory = (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderFactory.getTrackColumnName(),
                loaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new AlbumArtCursorAdapter(getActivity(), R.layout.row_tracks_list, from, to, R.id.album_art, loaderFactory.getAlbumIdColumnName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "R.layout.fragment_tracks_browser id: " + R.layout.fragment_tracks_browser);
        View v = inflater.inflate(R.layout.fragment_tracks_browser, container, false);
        Log.d(TAG, "v id: " + v.getId());
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        AbstractTracksCursorLoaderCreator loaderCreator = (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, new TrackMenuCommandSet(getActivity(), mServiceWrapper),
                loaderCreator.getTrackIdColumnName()));
        return v;
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        Bundle extras = mPassedArguments;
        if (extras == null) {
            return new AllTracksCursorLoaderCreator(getActivity());
        }
        long artistId = extras.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY, AudioStorage.WRONG_ID);
        long albumId = extras.getLong(AlbumsBrowserFragment.ALBUM_ID_KEY, AudioStorage.WRONG_ID);
        long playlistId = extras.getLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY, AudioStorage.WRONG_ID);

        if ((artistId != AudioStorage.WRONG_ID) && (albumId != AudioStorage.WRONG_ID)) {
            return new ArtistAlbumTracksCursorLoaderCreator(getActivity(), artistId, albumId);
        } else if (albumId != AudioStorage.WRONG_ID) {
            return new AlbumTracksCursorLoaderCreator(getActivity(), albumId);
        } else if(playlistId != AudioStorage.WRONG_ID) {
            return new PlaylistTracksCursorLoaderCreator(getActivity(), playlistId);
        } else {
            /*this won't be executed, but keep this as "default value"*/
            return new AllTracksCursorLoaderCreator(getActivity());
        }

    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        AbstractTracksCursorLoaderCreator loaderFactory =
                (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        return new OnTrackClickListener(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderFactory.getTrackIdColumnName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindFromService(getActivity(), this);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public void ServiceConnected() {
        //TODO
    }

    @Override
    public void ServiceDisconnected() {
        //TODO
    }
}
