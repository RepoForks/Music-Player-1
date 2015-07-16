package ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import ua.edu.cdu.fotius.lisun.musicplayer.R;

/**
 * A simple {@link ListFragment} subclass.
 *
 */
public class TrackBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks{

    private final String TRACK_TITLE_COLUMN = MediaStore.Audio.Media.TITLE;
    private final String ARTIST_TITLE_COLUMN = MediaStore.Audio.Media.ARTIST;
    private final String CURSOR_SORT_ORDER = TRACK_TITLE_COLUMN + " ASC";
    private final int TRACK_LOADER_ID = 1;
    private final String TAG = getClass().getSimpleName();

    private SimpleCursorAdapter mCursorAdapter;

    public TrackBrowserFragment() {
        // Required empty public constructor
        Log.d(TAG, "Constructor");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        //do we actually need this if we use Loader?
        //definitely, because don't need to create
        //adapter and call onLoadFinished()
        //everytime on config changes
        setRetainInstance(true);

        mCursorAdapter = (SimpleCursorAdapter)getCursorAdapter();
        setListAdapter(mCursorAdapter);

        getLoaderManager().initLoader(TRACK_LOADER_ID, null, this);
    }

    private CursorAdapter getCursorAdapter() {

        String[] from = new String[] {
                TRACK_TITLE_COLUMN,
                ARTIST_TITLE_COLUMN
        };

        int[] to = new int[] {
                R.id.song_title,
                R.id.artist_title
        };

        return new SimpleCursorAdapter(getActivity(), R.layout.row_songs_list, null, from, to, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_songs_browser, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    //Loader Manager Callbacks
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");

        String[] mProjection = new String[] {
                MediaStore.Audio.Media._ID,
                TRACK_TITLE_COLUMN,
                ARTIST_TITLE_COLUMN
        };
        return new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                mProjection, null, null, CURSOR_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.d(TAG, "onLoaderFinished");
        switch (loader.getId()){
            case TRACK_LOADER_ID:
                mCursorAdapter.swapCursor((Cursor)data);
            break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mCursorAdapter.swapCursor(null);
    }
}
