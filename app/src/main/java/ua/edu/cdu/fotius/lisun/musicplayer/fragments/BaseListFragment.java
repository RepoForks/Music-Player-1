package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceStateChangesObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceStateReceiver;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;

/*Methods from ServiceConnectionObserver do anything. Interface is needed to distinguish elements connected to service.
TODO: maybe should distinguish by fragment*/
public abstract class BaseListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnectionObserver, ServiceStateChangesObserver {

    protected IndicatorCursorAdapter mCursorAdapter;
    protected AbstractCursorLoaderCreator mLoaderCreator;
    private ToolbarActivity mToolbarActivity;
    protected PlaybackServiceWrapper mServiceWrapper;

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
        mServiceWrapper = PlaybackServiceWrapper.getInstance();
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

        View v = getActivity().findViewById(R.id.empty);
        if(v == null) {
            throw new RuntimeException(
                    "Your content must have a View " +
                            "whose id attribute is " +
                            "'R.id.empty'");
        }

        if(mCursorAdapter.getCount() > 0) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
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

    @Override
    public void onMetadataChanged() {
        setIndicator(mServiceWrapper, mCursorAdapter, mLoaderCreator);
    }

    @Override
    public void onPlaybackStateChanged() {
        if(!mServiceWrapper.isPlaying()) {
            mCursorAdapter.setIndicatorFor(null, AudioStorage.WRONG_ID);
        } else {
            setIndicator(mServiceWrapper, mCursorAdapter, mLoaderCreator);
        }
    }

    protected abstract void setIndicator(PlaybackServiceWrapper serviceWrapper,
                                         IndicatorCursorAdapter adapter,
                                         AbstractCursorLoaderCreator loaderCreator);
}
