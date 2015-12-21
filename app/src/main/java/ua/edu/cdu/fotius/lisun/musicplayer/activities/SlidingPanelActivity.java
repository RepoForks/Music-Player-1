package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.support.v7.widget.Toolbar;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;

public abstract class SlidingPanelActivity extends ToolbarActivity{

    protected SlidingUpPanelLayout mSlidingPanel;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
         mSlidingPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel);

        if(mSlidingPanel == null) {
            throw new RuntimeException(
                    "Your content must have a SlidingUpPanelLayout" +
                            "whose id attribute is " +
                            "'R.id.sliding_panel'");
        }
    }

    public void setPanelSlideListener(SlidingUpPanelLayout.PanelSlideListener listener) {
        mSlidingPanel.setPanelSlideListener(listener);
    }

    public void setDragView(int dragViewResourceID) {
        mSlidingPanel.setDragView(dragViewResourceID);
    }
}
