package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.NowPlayingFragment;


public abstract class ToolbarActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    public static final String TOOLBAR_TITLE_KEY = "title_key";

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";
    private Fragment mCurrentFragment;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            String savedFragmentTag = savedInstanceState.getString(EXTRA_FRAGMENT_TAG);
            mCurrentFragment = getSupportFragmentManager()
                    .findFragmentByTag(savedFragmentTag);
        }

        Log.d(TAG, "Current fragment == null: " + (mCurrentFragment == null));
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if(mToolbar == null) {
            throw new RuntimeException(
                    "Your content must have a android.support.v7.widget.Toolbar " +
                            "whose id attribute is " +
                            "'R.id.toolbar'");
        }
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FRAGMENT_TAG, mCurrentFragment.getTag());
        super.onSaveInstanceState(outState);
    }

    public boolean isFragmentSet() {
        return mCurrentFragment != null;
    }

    public void setDefaultFragment(Fragment fragment, String tag, Bundle arguments) {
        if(mCurrentFragment == null) {
            Log.d(TAG, "Setting initial fragment");
            checkFragmentContainerResourceID();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, tag)
                    .commit();
            mCurrentFragment = fragment;
        }
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        checkFragmentContainerResourceID();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, fragmentTag)
                .commit();
        mCurrentFragment = fragment;
    }

    private void checkFragmentContainerResourceID() {
        if(findViewById(R.id.fragment_container) == null) {
            throw new RuntimeException(
                    "Your content must have a container for fragment " +
                            "whose id attribute is " +
                            "'R.id.fragment_container'");
        }
    }

    public void setTitle(String title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setTitle(Bundle extras, String defaultString) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getToolbarTitle(extras, defaultString));
        }
    }

    public void setNavigationClickListener(View.OnClickListener listener) {
        if(mToolbar != null) {
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    public void setNavigationIconResourceID(int resourceID) {
        if(mToolbar != null) {
            mToolbar.setNavigationIcon(resourceID);
        }
    }

    private String getToolbarTitle(Bundle extras, String defaultString) {
        if(defaultString == null) {
            return getResources().getString(R.string.app_name);
        }

        if(extras == null) {
            return defaultString;
        }

        String title = extras.getString(TOOLBAR_TITLE_KEY);
        if(title == null) {
            title = defaultString;
        }
        return title;
    }
}
