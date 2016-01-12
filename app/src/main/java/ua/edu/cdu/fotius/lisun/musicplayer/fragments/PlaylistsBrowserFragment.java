package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.PlaylistCursorLoaderCreator;

public class PlaylistsBrowserFragment extends BaseLoaderFragment {

    public static final String TAG = "playlists";
    public static final String PLAYLIST_ID_KEY = "playlist_id_key";

    public PlaylistsBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected IndicatorCursorAdapter createCursorAdapter() {
        PlaylistCursorLoaderCreator loaderFactory = (PlaylistCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[] { loaderFactory.getPlaylistColumnName()};
        int[] to = new int[] { R.id.playlist_name};

        return new IndicatorCursorAdapter(getActivity(),
                R.layout.row_user_playlists_list, from, to);
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        return new PlaylistCursorLoaderCreator(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_playlists_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        PlaylistCursorLoaderCreator loaderCreator = (PlaylistCursorLoaderCreator) mLoaderCreator;
        listView.setOnItemClickListener(new OnPlaylistClickListener(getActivity(), mCursorAdapter,
                loaderCreator.getPlaylistIdColumnName(), loaderCreator.getPlaylistColumnName()));
        return v;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public void onMetadataChanged() {

    }

    @Override
    public void onPlaybackStateChanged() {

    }
}
