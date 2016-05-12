package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public abstract class ToolbarActivity extends AppCompatActivity {

    public static final String TOOLBAR_TITLE_KEY = "title_key";

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";
    private Fragment mCurrentFragment;
    private ProgressBar mProgressBar;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            String savedFragmentTag = savedInstanceState.getString(EXTRA_FRAGMENT_TAG);
            mCurrentFragment = getFragmentManager()
                    .findFragmentByTag(savedFragmentTag);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar == null) {
            throw new RuntimeException(
                    "Your content must have a android.support.v7.widget.Toolbar " +
                            "whose id attribute is " +
                            "'R.id.toolbar'");
        }
        setSupportActionBar(mToolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (mProgressBar == null) {
            throw new RuntimeException(
                    "Your content must have a ProgressBar" +
                            "whose id attribute is " +
                            "'R.id.progress_bar'");
        }
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
        if (mCurrentFragment == null) {
            checkFragmentContainerResourceID();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.root_view, fragment, tag)
                    .commit();
            mCurrentFragment = fragment;
        }
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        checkFragmentContainerResourceID();
        getFragmentManager().beginTransaction()
                .replace(R.id.root_view, fragment, fragmentTag)
                .commit();
        mCurrentFragment = fragment;
    }

    private void checkFragmentContainerResourceID() {
        if (findViewById(R.id.root_view) == null) {
            throw new RuntimeException(
                    "Your content must have a container for fragment " +
                            "whose id attribute is " +
                            "'R.id.fragment_container'");
        }
    }

    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setTitle(Bundle extras, String defaultString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getToolbarTitle(extras, defaultString));
        }
    }

    public void setNavigationClickListener(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    public void setNavigationIconResourceID(int resourceID) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(resourceID);
        }
    }

    private String getToolbarTitle(Bundle extras, String defaultString) {
        if (defaultString == null) {
            return getResources().getString(R.string.app_name);
        }

        if (extras == null) {
            return defaultString;
        }

        String title = extras.getString(TOOLBAR_TITLE_KEY);
        if (title == null) {
            title = defaultString;
        }
        return title;
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
