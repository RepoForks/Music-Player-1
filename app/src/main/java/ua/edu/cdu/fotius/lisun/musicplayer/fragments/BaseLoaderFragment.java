package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;

public abstract class BaseLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    protected CursorAdapter mCursorAdapter;
    protected AbstractCursorLoaderCreator mLoaderCreator;
    private ToolbarActivity mToolbarActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (ToolbarActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mLoaderCreator = createCursorLoaderCreator();
        mCursorAdapter = createCursorAdapter();
    }

    @Override
    public void onResume() {
        super.onPause();
        if(mToolbarActivity != null) {
            mToolbarActivity.showProgress();
        }
        getLoaderManager().initLoader(mLoaderCreator.getLoaderId(), null, this);
    }

    protected abstract CursorAdapter createCursorAdapter();

    protected abstract AbstractCursorLoaderCreator createCursorLoaderCreator();

    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        if(mToolbarActivity != null) {
            mToolbarActivity.hideProgress();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
