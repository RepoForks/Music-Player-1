package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseTracksLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.PlaylistLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnPlaylistClickListener;

public class PlaylistsFragment extends BaseFragment {

    public static final String TAG = "playlists";
    public static final String PLAYLIST_ID_KEY = "playlist_id_key";

    public PlaylistsFragment() {
    }

    @Override
    protected BaseCursorAdapter createCursorAdapter() {
        PlaylistLoaderCreator loaderFactory = (PlaylistLoaderCreator) mLoaderCreator;
        String[] from = new String[] { loaderFactory.getPlaylistColumn()};
        int[] to = new int[] { R.id.playlist_name};

        return new BaseCursorAdapter(getActivity(),
                R.layout.row_user_playlists_list, from, to);
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        return new PlaylistLoaderCreator(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_playlists_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        PlaylistLoaderCreator loaderCreator = (PlaylistLoaderCreator) mLoaderCreator;
        listView.setOnItemClickListener(new OnPlaylistClickListener(getActivity(), mCursorAdapter,
                loaderCreator.getPlaylistIdColumn(), loaderCreator.getPlaylistColumn()));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), listView,
                new PlaylistMenuCommandSet(this, mServiceWrapper),
                loaderCreator.getPlaylistIdColumn()));
        return v;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public String defineEmptyListMessage() {
        return getActivity().getResources().getString(R.string.playlists);
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                BaseCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator) {}
}
