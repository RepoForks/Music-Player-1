package ua.edu.cdu.fotius.lisun.musicplayer.activities;


import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingPanelListener;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;

public abstract class SlidingPanelActivity extends ToolbarActivity{

    private SlidingUpPanelLayout mSlidingPanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpSlidingPanel();
    }

    private void setUpSlidingPanel() {
        mSlidingPanel = (SlidingUpPanelLayout)findViewById(R.id.sliding_up_panel_layout);
    }

    public void setPanelSlideListener(SlidingPanelListener listener) {
        mSlidingPanel.setPanelSlideListener(listener);
    }
}
