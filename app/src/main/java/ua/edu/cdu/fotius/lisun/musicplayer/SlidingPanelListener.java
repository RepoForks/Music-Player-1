package ua.edu.cdu.fotius.lisun.musicplayer;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;

/**
 * Created by andrei on 13.10.2015.
 */
public class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {
    private LinearLayout mAdditionalControlPanel;
    private boolean mIsAdditionalControlsVisible;

    private DrawerLayout mDrawerLayout = null;

    //drawerLayout can be null, if there no drawer for this activity
    public SlidingPanelListener(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if ((slideOffset > 0.6) && mIsAdditionalControlsVisible) {
            mAdditionalControlPanel =
                    (LinearLayout) panel.findViewById(R.id.additional_control_panel);
            mAdditionalControlPanel.setVisibility(View.GONE);
            mIsAdditionalControlsVisible = false;
        }

        if ((slideOffset < 0.6) && !mIsAdditionalControlsVisible) {
            mAdditionalControlPanel =
                    (LinearLayout) panel.findViewById(R.id.additional_control_panel);

            mAdditionalControlPanel.setVisibility(View.VISIBLE);
            mIsAdditionalControlsVisible = true;
        }
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
    public void onPanelAnchored(View panel) {}
    @Override
    public void onPanelHidden(View panel) {}
}
