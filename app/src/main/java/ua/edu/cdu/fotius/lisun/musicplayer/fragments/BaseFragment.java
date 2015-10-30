package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AbstractCursorLoaderFactory;

public abstract class BaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int PARENT_ID_IS_NOT_SET = -1;
    /*we have one and only one loader per fragment
     so don't need some specific loader ids.*/
    private final int ARBITRARY_LOADER_ID = 1;

    protected CursorAdapter mCursorAdapter;
    protected AbstractCursorLoaderFactory mLoaderFactory;

    private String tag;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mLoaderFactory = createLoaderFactory();
        mCursorAdapter = createAdapter();
        getLoaderManager().initLoader(ARBITRARY_LOADER_ID, null, this);
    }

    public String getFragmentTag() {
        return tag;
    }

    public void setFragmentTag(String tag) {
        this.tag = tag;
    }

    protected abstract CursorAdapter createAdapter();
    protected abstract AbstractCursorLoaderFactory createLoaderFactory();

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
