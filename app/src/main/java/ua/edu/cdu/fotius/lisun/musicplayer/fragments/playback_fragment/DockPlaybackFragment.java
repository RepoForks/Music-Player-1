package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.HideableLayout;

public class DockPlaybackFragment extends AbstractPlaybackFragment{

    public static final String TAG = "dock_playback_fragment";

    private HideableLayout mMainLayout;

    public DockPlaybackFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DockPlaybackFragment.onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mMainLayout = (HideableLayout) v.findViewById(R.id.dock_playback_layout);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DockPlaybackFragment.onDestroy()");
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_dock_playback;
    }

    public void show() {
        mMainLayout.show();
    }

    public void hide() {
        mMainLayout.hide();
    }

    public boolean isContentVisible() {
        return (mMainLayout.getVisibility() == View.VISIBLE);
    }
}
