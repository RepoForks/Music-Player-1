package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;

public class NowPlayingFragment extends BaseLoaderFragment implements ServiceConnectionObserver{

    private MediaPlaybackServiceWrapper mServiceWrapper;

    public NowPlayingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    protected CursorAdapter createCursorAdapter() {
        return null;
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        return null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }


    @Override
    public void ServiceConnected() {
        //TODO:
    }

    @Override
    public void ServiceDisconnected() {

    }
}
