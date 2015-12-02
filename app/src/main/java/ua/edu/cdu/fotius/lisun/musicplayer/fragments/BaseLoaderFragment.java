package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;

public abstract class BaseLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String TAG = getClass().getSimpleName();

    protected CursorAdapter mCursorAdapter;
    protected AbstractCursorLoaderCreator mLoaderCreator;

    private String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mLoaderCreator = createCursorLoaderCreator();
        mCursorAdapter = createCursorAdapter();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onPause();
        getLoaderManager().initLoader(mLoaderCreator.getLoaderId(), null, this);
    }

    public String getFragmentTag() {
        return tag;
    }

    public void setFragmentTag(String tag) {
        this.tag = tag;
    }

    protected abstract CursorAdapter createCursorAdapter();
    protected abstract AbstractCursorLoaderCreator createCursorLoaderCreator();

    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
        mCursorAdapter.swapCursor(null);
    }
}
