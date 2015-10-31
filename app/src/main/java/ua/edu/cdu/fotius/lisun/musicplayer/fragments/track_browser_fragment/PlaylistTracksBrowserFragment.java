package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.BaseMenu;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaylistsBrowserFragment;

public class PlaylistTracksBrowserFragment extends TracksDragNDropBrowserFragment{

    public static final String TAG = "playlist_tracks";
    private long mPlaylistID = PARENT_ID_IS_NOT_SET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null) {
            mPlaylistID = arguments.getLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY,
                    PARENT_ID_IS_NOT_SET);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CursorAdapter createAdapter() {
        PlaylistTracksCursorLoaderFactory loaderFactory = (PlaylistTracksCursorLoaderFactory) mLoaderFactory;
        String[] from = new String[]{loaderFactory.getTrackColumnName(),
                loaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new DragNDropCursorAdapter(getActivity(), getRowLayoutID(), from, to, R.id.handler,
                loaderFactory.getId(), loaderFactory.getPlayOrder());
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        return new PlaylistTracksCursorLoaderFactory(getActivity(), mPlaylistID);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_playlist_tracks;
    }

    @Override
    protected int getRowLayoutID() {
        return R.layout.row_playlist_tracks_list;
    }

    //TODO:
    @Override
    protected BaseMenu createActionBarMenuContent() {
        return super.createActionBarMenuContent();
    }
}
