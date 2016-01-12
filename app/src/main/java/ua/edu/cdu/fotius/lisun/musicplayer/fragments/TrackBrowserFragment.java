package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AlbumTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AllTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.ArtistAlbumTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.PlaylistTracksCursorLoaderCreator;


public class TrackBrowserFragment extends BaseLoaderFragment {

    public static final String TAG = "tracks";

    protected ToolbarStateListener mToolbarStateListener;

    private Bundle mPassedArguments;

    public TrackBrowserFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarStateListener = (ToolbarStateListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPassedArguments = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected IndicatorCursorAdapter createCursorAdapter() {
        AbstractTracksCursorLoaderCreator loaderFactory = (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderFactory.getTrackColumnName(),
                loaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new AlbumArtCursorAdapter(getActivity(), R.layout.row_tracks_list, from, to, R.id.album_art, loaderFactory.getAlbumIdColumnName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        AbstractTracksCursorLoaderCreator loaderCreator = (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, new TrackMenuCommandSet(this, mServiceWrapper),
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
        AbstractTracksCursorLoaderCreator loaderCreator =
                (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        return new OnTrackClickListener(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderCreator.getTrackIdColumnName());
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public void onMetadataChanged() {
        Log.d(TAG, "onMetadataChanged");
        mCursorAdapter.setIndicatorFor(mServiceWrapper.getTrackID());
    }

    @Override
    public void onPlaybackStateChanged() {
        if(!mServiceWrapper.isPlaying()) {
            mCursorAdapter.setIndicatorFor(AudioStorage.WRONG_ID);
        } else {
            mCursorAdapter.setIndicatorFor(mServiceWrapper.getTrackID());
        }
    }
}
