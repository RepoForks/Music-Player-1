package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.QueueCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistTrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDropPlaylistTrackListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnTrackClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.views.DragNDropListView;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.PlaylistTracksLoaderCreator;

public class PlaylistTracksFragment extends BaseFragment {

    public static final String TAG = "playlist_tracklist";

    private long mPlaylistID;

    public PlaylistTracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPlaylistID = fetchPlaylistID(getArguments());

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
        return arguments.getLong(PlaylistsFragment.PLAYLIST_ID_KEY,
                AudioStorage.WRONG_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist_tracks, container, false);
        DragNDropListView listView = (DragNDropListView) v.findViewById(R.id.list);
        listView.setDragHandlerResourceID(R.id.handler);
        listView.setDropListener(new OnDropPlaylistTrackListener(this, mPlaylistID));
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        PlaylistTracksLoaderCreator loaderCreator =
                (PlaylistTracksLoaderCreator) mLoaderCreator;
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), listView,
                new PlaylistTrackMenuCommandSet(this, mServiceWrapper, mPlaylistID),
                loaderCreator.getTrackIdColumn()));
        return v;
    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        BaseTracksLoaderCreator loaderFactory =
                (BaseTracksLoaderCreator) mLoaderCreator;
        return new OnTrackClickListener(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderFactory.getTrackIdColumn());
    }

    @Override
    protected BaseCursorAdapter createCursorAdapter() {
        PlaylistTracksLoaderCreator loaderCreator =
                (PlaylistTracksLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderCreator.getTrackColumn(),
                loaderCreator.getArtistColumn()};
        int[] to = new int[]{R.id.track_title, R.id.track_details};

        return new QueueCursorAdapter(getActivity(), R.layout.row_playlist_tracks, from, to,
                loaderCreator.getTrackIdColumn());
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        return new PlaylistTracksLoaderCreator(getActivity(), mPlaylistID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public String defineEmptyListMessage() {
        return getActivity().getResources().getString(R.string.songs);
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                BaseCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator){}
}
