package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;


public abstract class ToolbarActivity extends AppCompatActivity {

    public static final String TOOLBAR_TITLE_KEY = "title_key";

    protected Toolbar mToolbar;

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
