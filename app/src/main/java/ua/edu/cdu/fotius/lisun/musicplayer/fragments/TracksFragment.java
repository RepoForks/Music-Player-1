package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.AlbumArtCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.AlbumTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.AllTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.ArtistAlbumTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.GenreTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.PlaylistTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnTrackClickListener;


public class TracksFragment extends BaseFragment {
    public static final String TAG = "tracks";

    private Bundle mPassedArguments;

    public TracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPassedArguments = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseCursorAdapter createCursorAdapter() {
        BaseTracksLoaderCreator loaderFactory = (BaseTracksLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderFactory.getTrackColumn(),
                loaderFactory.getArtistColumn()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new AlbumArtCursorAdapter(getActivity(), R.layout.row_tracks_list, from, to, R.id.album_art, loaderFactory.getAlbumIdColumn());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        BaseTracksLoaderCreator loaderCreator = (BaseTracksLoaderCreator) mLoaderCreator;
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), listView,
                new TrackMenuCommandSet(this, mServiceWrapper),
                loaderCreator.getTrackIdColumn()));
        return v;
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        Bundle extras = mPassedArguments;
        if (extras == null) {
            return new AllTracksLoaderCreator(getActivity());
        }

        long artistId = extras.getLong(ArtistsFragment.ARTIST_ID_KEY, AudioStorage.WRONG_ID);
        long albumId = extras.getLong(AlbumsFragment.ALBUM_ID_KEY, AudioStorage.WRONG_ID);
        long playlistId = extras.getLong(PlaylistsFragment.PLAYLIST_ID_KEY, AudioStorage.WRONG_ID);
        long genreId = extras.getLong(GenresFragment.GENRE_ID_KEY, AudioStorage.WRONG_ID);

        if ((artistId != AudioStorage.WRONG_ID) && (albumId != AudioStorage.WRONG_ID)) {
            return new ArtistAlbumTracksLoaderCreator(getActivity(), artistId, albumId);
        } else if (albumId != AudioStorage.WRONG_ID) {
            return new AlbumTracksLoaderCreator(getActivity(), albumId);
        } else if(playlistId != AudioStorage.WRONG_ID) {
            return new PlaylistTracksLoaderCreator(getActivity(), playlistId);
        } else if(genreId != AudioStorage.WRONG_ID) {
            return new GenreTracksLoaderCreator(getActivity(), genreId);
        } else {
            /*this won't be executed, but keep this as "default value"*/
            return new AllTracksLoaderCreator(getActivity());
        }

    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        BaseTracksLoaderCreator loaderCreator =
                (BaseTracksLoaderCreator) mLoaderCreator;
        return new OnTrackClickListener(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderCreator.getTrackIdColumn());
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                BaseCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator) {
        BaseTracksLoaderCreator creator =
                (BaseTracksLoaderCreator) loaderCreator;
        adapter.setIndicatorFor(creator.getTrackIdColumn(), serviceWrapper.getTrackID(),
                serviceWrapper.isPlaying());
    }
}
