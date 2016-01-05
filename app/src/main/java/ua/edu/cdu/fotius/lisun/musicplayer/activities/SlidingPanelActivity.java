package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment.DockPlaybackFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment.PlaybackToolbarFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;

public abstract class SlidingPanelActivity extends ToolbarActivity {

    private final String TAG = getClass().getSimpleName();

    public static final String PANEL_STATE_KEY = "panel_state_key";

    public static enum PanelState {
        EXPANDED(SlidingUpPanelLayout.PanelState.EXPANDED),
        COLLAPSED(SlidingUpPanelLayout.PanelState.COLLAPSED);

        private SlidingUpPanelLayout.PanelState mState;

        PanelState(SlidingUpPanelLayout.PanelState state) {
            mState = state;
        }

        public SlidingUpPanelLayout.PanelState getPanelState() {
            return mState;
        }
    }

    private PanelState mPanelState;
    public static final String FRAGMENT_TAG = "panel_control_fragment";
    protected SlidingUpPanelLayout mSlidingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SlidingPanelActivity.OnCreate()");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mPanelState = (PanelState)extras.getSerializable(PANEL_STATE_KEY);
        }

        if(mPanelState == null) {
            mPanelState = PanelState.COLLAPSED;
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        Log.d(TAG, "SlidingPanelActivity.OnContentChange");
        mSlidingPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel);

        if (mSlidingPanel == null) {
            throw new RuntimeException(
                    "Your content must have a SlidingUpPanelLayout " +
                            "whose id attribute is " +
                            "'R.id.sliding_panel'");
        }

        View panelDragHandler = findViewById(R.id.drag_handler_fragment_container);
        if (panelDragHandler == null) {
            throw new RuntimeException(
                    "Your content must have a View " +
                            "whose id attribute is " +
                            "'R.id.drag_handler_fragment_container'");
        }
        mSlidingPanel.setDragView(panelDragHandler);
        mSlidingPanel.setPanelState(mPanelState.getPanelState());

        if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null) {
            Log.e(TAG, "onContentChanged.settingInitial");
            Fragment fragment = null;

            if(mPanelState == PanelState.COLLAPSED) {
                fragment = new DockPlaybackFragment();
            } else {
                fragment = new PlaybackToolbarFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.drag_handler_fragment_container,
                            fragment, FRAGMENT_TAG).commit();
        }
    }

    public void setPanelSlideListener(SlidingUpPanelLayout.PanelSlideListener listener) {
        mSlidingPanel.setPanelSlideListener(listener);
    }
}
