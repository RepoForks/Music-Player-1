package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

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
    protected CursorAdapter createAdapter() {
        PlaylistCursorLoaderFactory loaderFactory = (PlaylistCursorLoaderFactory) mLoaderFactory;
        String[] from = new String[] { loaderFactory.getPlaylistName()};
        int[] to = new int[] { R.id.playlist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_user_playlists_list, from, to);
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        return new PlaylistCursorLoaderFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_playlists_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new OnPlaylistClick(getActivity(), mCursorAdapter));
        return v;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return mLoaderFactory.getCursorLoader();
    }
}
