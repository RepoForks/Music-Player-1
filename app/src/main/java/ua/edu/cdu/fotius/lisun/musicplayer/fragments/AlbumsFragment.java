package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.AlbumArtCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.AlbumMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseAlbumsLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.AlbumsLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.ArtistAlbumsLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnAlbumClickListener;

public class AlbumsFragment extends BaseFragment {

    public static final String TAG = "albums";
    public static final String ALBUM_ID_KEY = "album_id_key";
    private Bundle mExtras;

    private long mArtistID = AudioStorage.WRONG_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mExtras = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseCursorAdapter createCursorAdapter() {
        String[] from = new String[]{AudioStorage.Album.ALBUM, AudioStorage.Album.ARTIST};
        int[] to = new int[]{R.id.album_title, R.id.artist_name};

        BaseAlbumsLoaderCreator loaderFactory = (BaseAlbumsLoaderCreator) mLoaderCreator;
        return new AlbumArtCursorAdapter(getActivity(),
                R.layout.grid_item_albums, from, to, R.id.album_art,
                loaderFactory.getAlbumIdColumn());
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        Bundle extras = mExtras;
        if (extras == null) {
            return new AlbumsLoaderCreator(getActivity());
        }

        long artistId = mArtistID = mExtras.getLong(ArtistsFragment.ARTIST_ID_KEY, AudioStorage.WRONG_ID);
        if (artistId != AudioStorage.WRONG_ID) {
            return new ArtistAlbumsLoaderCreator(getActivity(), artistId);
        } else {
            /*this won't be executed, but keep this as "default value"*/
            return new AlbumsLoaderCreator(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_albums, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.list);
        gridView.setAdapter(mCursorAdapter);
        BaseAlbumsLoaderCreator loaderCreator = (BaseAlbumsLoaderCreator) mLoaderCreator;
        gridView.setOnItemClickListener(new OnAlbumClickListener(getActivity(),
                mCursorAdapter, mExtras,
                loaderCreator.getAlbumIdColumn(), loaderCreator.getAlbumColumn()));
        gridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), gridView,
                new AlbumMenuCommandSet(this, mServiceWrapper, mArtistID),
                loaderCreator.getAlbumIdColumn()));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public String defineEmptyListMessage() {
        return getActivity().getResources().getString(R.string.albums);
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                BaseCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator) {
        BaseAlbumsLoaderCreator creator =
                (BaseAlbumsLoaderCreator) loaderCreator;
        Log.e(TAG, "Album ID: " + mServiceWrapper.getAlbumID());
        adapter.setIndicatorFor(creator.getAlbumIdColumn(), mServiceWrapper.getAlbumID(),
                mServiceWrapper.isPlaying());
    }
}
