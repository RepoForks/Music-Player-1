package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceStateChangesObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceStateReceiver;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;

/*Methods from ServiceConnectionObserver do anything. Interface is needed to distinguish elements connected to service.
TODO: maybe should distinguish by fragment*/
public abstract class BaseFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnectionObserver, ServiceStateChangesObserver {

    private final String TAG = getClass().getSimpleName();

    protected BaseCursorAdapter mCursorAdapter;
    protected BaseLoaderCreator mLoaderCreator;
    private ToolbarActivity mToolbarActivity;
    protected MediaPlaybackServiceWrapper mServiceWrapper;

    private ServiceStateReceiver mServiceStateReceiver;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToolbarActivity = (ToolbarActivity) activity;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setIndicator(mServiceWrapper, mCursorAdapter, mLoaderCreator);
        return super.onCreateView(inflater, container, savedInstanceState);
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
        if (mToolbarActivity != null) {
            mToolbarActivity.showProgress();
        }
        getLoaderManager().initLoader(mLoaderCreator.getLoaderId(), null, this);
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(mLoaderCreator.getLoaderId(), null, this);
    }

    protected abstract BaseCursorAdapter createCursorAdapter();

    protected abstract BaseLoaderCreator createCursorLoaderCreator();

    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        if (mToolbarActivity == null) return;
        mToolbarActivity.hideProgress();

        View v = mToolbarActivity.findViewById(R.id.empty);
        if (v == null) {
            throw new RuntimeException(
                    "Your content must have a View " +
                            "whose id attribute is " +
                            "'R.id.empty'");
        }

        if (mCursorAdapter.getCount() <= 0) {
            TextView emptyListMessage = (TextView) v;
            emptyListMessage.setText(mToolbarActivity
                    .getString(R.string.empty_list_message, defineEmptyListMessage()));
            v.setVisibility(View.VISIBLE);
        }
    }

    public abstract String defineEmptyListMessage();

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
        if (!mServiceWrapper.isPlaying()) {
            mCursorAdapter.setIndicatorFor(null, AudioStorage.WRONG_ID, false);
        } else {
            setIndicator(mServiceWrapper, mCursorAdapter, mLoaderCreator);
        }
    }

    protected abstract void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                         BaseCursorAdapter adapter,
                                         BaseLoaderCreator loaderCreator);
}
