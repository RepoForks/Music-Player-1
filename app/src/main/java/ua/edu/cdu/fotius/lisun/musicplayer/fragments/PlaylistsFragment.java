package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.IndicatorCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.PlaylistLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnPlaylistClickListener;

public class PlaylistsFragment extends BaseFragment {

    public static final String TAG = "playlists";
    public static final String PLAYLIST_ID_KEY = "playlist_id_key";

    public PlaylistsFragment() {
    }

    @Override
    protected IndicatorCursorAdapter createCursorAdapter() {
        PlaylistLoaderCreator loaderFactory = (PlaylistLoaderCreator) mLoaderCreator;
        String[] from = new String[] { loaderFactory.getPlaylistColumn()};
        int[] to = new int[] { R.id.playlist_name};

        return new IndicatorCursorAdapter(getActivity(),
                R.layout.row_user_playlists_list, from, to);
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        return new PlaylistLoaderCreator(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_playlists_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        PlaylistLoaderCreator loaderCreator = (PlaylistLoaderCreator) mLoaderCreator;
        listView.setOnItemClickListener(new OnPlaylistClickListener(getActivity(), mCursorAdapter,
                loaderCreator.getPlaylistIdColumn(), loaderCreator.getPlaylistColumn()));
        return v;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                IndicatorCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator) {}
}
