package ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;


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
           hideConcealableViews(panel);
        }

        if ((slideOffset < 0.6) && !mIsConcealableViewsVisible) {
            showConcealableViews(panel);
        }
    }

    private void showConcealableViews(View panel) {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.dock_playback_fragment);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(fragment.isHidden()) {
            ft.show(fragment);
        }
        ft.commit();
        mIsConcealableViewsVisible = true;
    }

    private void hideConcealableViews(View panel) {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.dock_playback_fragment);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(fragment.isVisible()) {
            ft.hide(fragment);
        }
        ft.commit();
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
