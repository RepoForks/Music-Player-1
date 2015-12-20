package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
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
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.DragNDropListView;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.PlaylistTracksCursorLoaderCreator;

public class PlaylistTracklistFragment extends BaseLoaderFragment implements ServiceConnectionObserver{

    public static final String TAG = "playlist_tracklist";

    private MediaPlaybackServiceWrapper mServiceWrapper;
    private ToolbarStateListener mToolbarStateListener;

    private long mPlaylistID;

    public PlaylistTracklistFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToolbarStateListener = (ToolbarStateListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);

        mPlaylistID = fetchPlaylistID(getArguments());

        Log.d(TAG, "mPlaylistID: " + mPlaylistID);

        if(mPlaylistID == AudioStorage.WRONG_ID) {
            throw new RuntimeException("Playlist id should be passed " +
                    "to PlaylistTracksBrowserFragment as arguments item");
        }

        super.onCreate(savedInstanceState);
    }

    private long fetchPlaylistID(Bundle arguments) {
        if(arguments == null) {
            return AudioStorage.WRONG_ID;
        }
        return arguments.getLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY,
                AudioStorage.WRONG_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dragndrop_list, container, false);
        DragNDropListView listView = (DragNDropListView) v.findViewById(R.id.list);
        listView.setDragHandlerResourceID(R.id.handler);
        listView.setDropListener(new OnDropPlaylistTrackListener(getActivity(), mPlaylistID));
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        PlaylistTracksCursorLoaderCreator loaderCreator =
                (PlaylistTracksCursorLoaderCreator) mLoaderCreator;
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, new TrackMenuCommandSet(getActivity(), mServiceWrapper),
                loaderCreator.getTrackIdColumnName()));
        return v;
    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        AbstractTracksCursorLoaderCreator loaderFactory =
                (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        return new OnTrackClickListener(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderFactory.getTrackIdColumnName());
    }

    @Override
    protected CursorAdapter createCursorAdapter() {
        PlaylistTracksCursorLoaderCreator loaderCreator =
                (PlaylistTracksCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderCreator.getTrackColumnName(),
                loaderCreator.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.track_details};

        return new QueueCursorAdapter(getActivity(), R.layout.drag_n_drop_row_layout, from, to,
                loaderCreator.getTrackIdColumnName());
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        return new PlaylistTracksCursorLoaderCreator(getActivity(), mPlaylistID);
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
