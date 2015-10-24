package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class PlaylistsBrowserFragment extends BaseFragment {

    public static final String TAG = "playlists";
    public static final String PLAYLIST_ID_KEY = "playlist_id_key";

    public PlaylistsBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CursorAdapter initAdapter() {
        String[] from = new String[] { AudioStorage.Playlist.PLAYLIST };
        int[] to = new int[] { R.id.playlist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_user_playlists_list, from, to);
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
        String[] projection = new String[] {
                AudioStorage.Playlist.PLAYLIST_ID,
                AudioStorage.Playlist.PLAYLIST
        };
        return new CursorLoader(getActivity(), MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                projection, null, null, AudioStorage.Playlist.SORT_ORDER);
    }
}
