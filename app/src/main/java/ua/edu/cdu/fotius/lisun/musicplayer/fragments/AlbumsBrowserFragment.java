package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class AlbumsBrowserFragment extends BaseFragment {

    public static final String TAG = "albums";
    public static final  String ALBUM_ID_KEY = "album_id_key";
    private long mArtistId = PARENT_ID_IS_NOT_SET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
         /*if called with artist id
        * then need to list albums of
        * this concrete artist*/
        Bundle args = getArguments();
        if(args != null) {
            mArtistId = args.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY, PARENT_ID_IS_NOT_SET);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseSimpleCursorAdapter initAdapter() {
        String[] from = new String[] {AudioStorage.Album.ALBUM, AudioStorage.Album.ARTIST};
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
        gridView.setOnItemClickListener(new OnAlbumClick(getActivity(), mCursorAdapter));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                AudioStorage.Album.ALBUM_ID,
                AudioStorage.Album.ALBUM,
                AudioStorage.Album.ARTIST
        };
        return new CursorLoader(getActivity(), MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                (mArtistId == -1) ? null : AudioStorage.Album.ARTIST_ID + " = ?",
                (mArtistId == -1) ? null : new String[] {Long.toString(mArtistId)}, AudioStorage.Album.SORT_ORDER);
    }
}
