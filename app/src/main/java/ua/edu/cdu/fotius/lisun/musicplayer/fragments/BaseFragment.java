package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

public abstract class BaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int PARENT_ID_IS_NOT_SET = -1;
    /*we have one and only one loader per fragment
     so don't need some specific loader ids.*/
    private final int ARBITRARY_LOADER_ID = 1;
    protected CursorAdapter mCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCursorAdapter = initAdapter();
        getLoaderManager().initLoader(ARBITRARY_LOADER_ID, null, this);
    }

    protected abstract CursorAdapter initAdapter();

    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
