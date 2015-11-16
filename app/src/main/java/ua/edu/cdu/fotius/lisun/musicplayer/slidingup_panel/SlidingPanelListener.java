package ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel;

import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;

public class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {

    private final String TAG = getClass().getSimpleName();

    private boolean mIsAdditionalControlsVisible;

    private DrawerLayout mDrawerLayout = null;

    //drawerLayout can be null, if there no drawer for this activity
    public SlidingPanelListener(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if ((slideOffset > 0.6) && mIsAdditionalControlsVisible) {
           hideAdditionalControlPanel(panel);
        }

        if ((slideOffset < 0.6) && !mIsAdditionalControlsVisible) {
            showAdditionalControlPanel(panel);
        }
    }

    private void showAdditionalControlPanel(View panel) {
        LinearLayout additionalControl = getAdditionalControlPanel(panel);
        additionalControl.setVisibility(View.VISIBLE);
        mIsAdditionalControlsVisible = true;
    }

    private void hideAdditionalControlPanel(View panel) {
        LinearLayout additionalControl = getAdditionalControlPanel(panel);
        additionalControl.setVisibility(View.GONE);
        mIsAdditionalControlsVisible = false;
    }

    private LinearLayout getAdditionalControlPanel(View panel) {
        return (LinearLayout) panel.findViewById(R.id.additional_control_panel);
    }
    
    @Override
    public void onPanelCollapsed(View panel) {
        //enable ability of opening navigation menu
        if(mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        Log.d(TAG, "OnPanelCollapsed");
    }

    @Override
    public void onPanelExpanded(View panel) {
        //disable ability of opening navigation menu
        if(mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        Log.d(TAG, "OnPanelExpanded");
    }

    @Override
    public void onPanelAnchored(View panel) {
        Log.d(TAG, "OnPanelAnchored");
    }

    @Override
    public void onPanelHidden(View panel) {
        Log.d(TAG, "OnPanelHidden");
    }
}
