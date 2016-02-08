package ua.edu.cdu.fotius.lisun.musicplayer.sliding_panel;

import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.SlidingPanelActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.DockPlaybackFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaybackHeaderFragment;


public class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {

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
           showFragmentToolbar();
        }

        if ((slideOffset < 0.6) && !mIsConcealableViewsVisible) {
            showDockPanel();
        }
    }

    private void showDockPanel() {
        mFragmentManager.beginTransaction()
                .replace(R.id.drag_handler_fragment_container, new DockPlaybackFragment(),
                        SlidingPanelActivity.FRAGMENT_TAG).commit();

        mIsConcealableViewsVisible = true;
    }

    private void showFragmentToolbar() {
        mFragmentManager.beginTransaction()
                .replace(R.id.drag_handler_fragment_container, new PlaybackHeaderFragment(),
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
