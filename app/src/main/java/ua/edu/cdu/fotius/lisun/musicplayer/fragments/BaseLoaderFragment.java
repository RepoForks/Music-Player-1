package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceStateChangesObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceStateReceiver;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;

/*Methods from ServiceConnectionObserver do anything. Interface is needed to distinguish elements connected to service.
TODO: maybe should distinguish by fragment*/
public abstract class BaseLoaderFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnectionObserver, ServiceStateChangesObserver {

    protected IndicatorCursorAdapter mCursorAdapter;
    protected AbstractCursorLoaderCreator mLoaderCreator;
    private ToolbarActivity mToolbarActivity;
    protected MediaPlaybackServiceWrapper mServiceWrapper;

    private ServiceStateReceiver mServiceStateReceiver;

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
        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
        mLoaderCreator = createCursorLoaderCreator();
        mCursorAdapter = createCursorAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindFromService(getActivity(), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        actionFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        mServiceStateReceiver = new ServiceStateReceiver(this);
        getActivity().registerReceiver(mServiceStateReceiver, actionFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mServiceStateReceiver);
    }

    @Override
    public void onResume() {
        super.onPause();
        if(mToolbarActivity != null) {
            mToolbarActivity.showProgress();
        }
        getLoaderManager().initLoader(mLoaderCreator.getLoaderId(), null, this);
    }

    protected abstract IndicatorCursorAdapter createCursorAdapter();

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

    @Override
    public void ServiceConnected() {
    }

    @Override
    public void ServiceDisconnected() {
    }
}
