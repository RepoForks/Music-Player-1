package ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ConcealableImageView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.HideableLayout;

public class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {

    private final String TAG = getClass().getSimpleName();

    private boolean mIsConcealableViewsVisible;

    private DrawerLayout mDrawerLayout = null;

    //drawerLayout can be null, if there no drawer for this activity
    public SlidingPanelListener(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
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
        HideableLayout concealableMediaButtons = getConcealableMediaButtonsLayout(panel);
        concealableMediaButtons.show();
        ConcealableImageView concealableAlbumArt = getConcealableAlbumArt(panel);
        concealableAlbumArt.show();
        mIsConcealableViewsVisible = true;
    }

    private void hideConcealableViews(View panel) {
        HideableLayout concealableMediaButtons = getConcealableMediaButtonsLayout(panel);
        concealableMediaButtons.hide();
        ConcealableImageView concealableAlbumArt = getConcealableAlbumArt(panel);
        concealableAlbumArt.hide();
        mIsConcealableViewsVisible = false;
    }

    private HideableLayout getConcealableMediaButtonsLayout(View panel) {
        return (HideableLayout) panel.findViewById(R.id.concealable_control_panel);
    }

    private ConcealableImageView getConcealableAlbumArt(View panel) {
        return (ConcealableImageView) panel.findViewById(R.id.album_art);
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
