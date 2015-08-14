package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.OnFragmentReplaceListener;

public class AlbumsBrowserFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = "albums_browser_fragment";

    public static final  String ALBUM_ID_KEY = "album_id_key";
    private final int ALBUMS_LOADER_ID = 1;

    private OnFragmentReplaceListener mReplaceFragmentCallback;
    private BaseSimpleCursorAdapter mCursorAdapter = null;
    private long mArtistId = -1;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mReplaceFragmentCallback = (OnFragmentReplaceListener) activity;
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

         /*if called with artist id
        * then need to list albums of
        * this concrete album*/
        Bundle args = getArguments();
        if(args != null) {
           mArtistId = args.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY);
        }

        getLoaderManager().initLoader(ALBUMS_LOADER_ID, null, this);
        mCursorAdapter = getAdapter();
    }

    private BaseSimpleCursorAdapter getAdapter() {
        String[] from = new String[] {AudioStorage.AlbumBrowser.ALBUM, AudioStorage.AlbumBrowser.ARTIST};
        int[] to = new int[] { R.id.album_title, R.id.artist_name };

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.grid_item_albums, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_albums_browser, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.grid_container);
        gridView.setAdapter(mCursorAdapter);
        gridView.setOnItemClickListener(mGridItemClickListener);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = new String[] {
                AudioStorage.AlbumBrowser.ALBUM_ID,
                AudioStorage.AlbumBrowser.ALBUM,
                AudioStorage.AlbumBrowser.ARTIST
        };

        Log.d(TAG, "mArtistId: " + mArtistId);

        //TODO: find out about MediaStore.Audio.Albums.ARTIST_ID
        /*There is no ARTIST_ID in MediaStore.Audio.Albums
        * although in database "album_info" view this attribute is present
        * Since MediaStore.Audio.Media.ARTIST_ID also named "artist_id"
        * just use it here*/
        return new CursorLoader(getActivity(), MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                (mArtistId == -1) ? null : MediaStore.Audio.Media.ARTIST_ID + " = ?",
                (mArtistId == -1) ? null : new String[] {Long.toString(mArtistId)}, AudioStorage.AlbumBrowser.SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    /* On GridView item click show fragment with tracks of
    * this concrete album */
    private GridView.OnItemClickListener mGridItemClickListener = new GridView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = mCursorAdapter.getCursor();
            if((cursor != null) && (cursor.moveToPosition(position))) {
                int idColumnIndex = cursor.getColumnIndexOrThrow(AudioStorage.AlbumBrowser.ALBUM_ID);
                long albumId = cursor.getLong(idColumnIndex);
                TrackBrowserFragment fragment = new TrackBrowserFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(ALBUM_ID_KEY, albumId);
                fragment.setArguments(bundle);
                mReplaceFragmentCallback.replaceFragment(fragment, TrackBrowserFragment.TAG);
            }
        }
    };
}
