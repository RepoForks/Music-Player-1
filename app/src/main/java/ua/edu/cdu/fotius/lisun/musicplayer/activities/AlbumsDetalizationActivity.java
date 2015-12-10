package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingPanelListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseLoaderFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;

public class AlbumsDetalizationActivity extends BaseActivity implements ToolbarStateListener{

    private final String TAG = getClass().getSimpleName();

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton);


        String toolbarTitle = getToolbarTitle(
                        getIntent().getExtras(),
                        getResources().getString(R.string.default_albums_detalization_activity_title));

        setUpToolbar(R.id.toolbar,
                toolbarTitle,
                R.drawable.ic_arrow_back_black_24dp,
                new OnUpClickListener(this));
        setUpSlidingPanel(R.id.sliding_up_panel_layout, null);

        if(savedInstanceState != null) {
            String currentFragmentTag = savedInstanceState.getString(EXTRA_FRAGMENT_TAG);
            mCurrentFragment =
                    getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
        }

        if(mCurrentFragment == null) {
            BaseLoaderFragment fragment = new AlbumsBrowserFragment();
            fragment.setFragmentTag(AlbumsBrowserFragment.TAG);
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, fragment.getFragmentTag()).commit();
            mCurrentFragment = fragment;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FRAGMENT_TAG, mCurrentFragment.getTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }
}
