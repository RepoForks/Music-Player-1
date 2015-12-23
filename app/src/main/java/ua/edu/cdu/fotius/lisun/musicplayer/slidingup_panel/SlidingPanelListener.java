package ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.SlidingPanelActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment.DockPlaybackFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment.PlaybackFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment.PlaybackToolbarFragment;


public class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {

    private final String TAG = getClass().getSimpleName();

    private boolean mIsConcealableViewsVisible;

    private DrawerLayout mDrawerLayout = null;
    private FragmentManager mFragmentManager;

    //drawerLayout can be null, if there no drawer for this activity
    public SlidingPanelListener(DrawerLayout drawerLayout, FragmentManager fragmentManager) {
        mDrawerLayout = drawerLayout;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if ((slideOffset > 0.6) && mIsConcealableViewsVisible) {
           showFragmentToolbar(panel);
        }

        if ((slideOffset < 0.6) && !mIsConcealableViewsVisible) {
            showDockPanel(panel);
        }
    }

    private void showDockPanel(View panel) {
        Log.d(TAG, "showDockPanel");

        List<Fragment> list = mFragmentManager.getFragments();

        for(Fragment f : list) {
            Log.d(TAG, "TAG: " + f.getTag());
        }

        mFragmentManager.beginTransaction()
                .replace(R.id.drag_handler_fragment_container, new DockPlaybackFragment(),
                        SlidingPanelActivity.FRAGMENT_TAG).commit();

        mIsConcealableViewsVisible = true;
    }

    private void showFragmentToolbar(View panel) {
        Log.d(TAG, "showFragmentToolbar");
        mFragmentManager.beginTransaction()
                .replace(R.id.drag_handler_fragment_container, new PlaybackToolbarFragment(),
                        SlidingPanelActivity.FRAGMENT_TAG).commit();
        mIsConcealableViewsVisible = false;
    }

    @Override
    public void onPanelCollapsed(View panel) {
        //enable ability of opening navigation menu
        if(mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void onPanelExpanded(View panel) {
        //disable ability of opening navigation menu
        if(mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onPanelAnchored(View panel) {
    }

    @Override
    public void onPanelHidden(View panel) {
    }
}
