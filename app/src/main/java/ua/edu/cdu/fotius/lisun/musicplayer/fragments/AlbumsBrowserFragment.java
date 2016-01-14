package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.AlbumMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractAlbumCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AlbumsCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.ArtistAlbumsCursorLoaderCreator;

public class AlbumsBrowserFragment extends BaseListFragment {

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
    protected IndicatorCursorAdapter createCursorAdapter() {
        String[] from = new String[]{AudioStorage.Album.ALBUM, AudioStorage.Album.ARTIST};
        int[] to = new int[]{R.id.album_title, R.id.artist_name};

        AbstractAlbumCursorLoaderCreator loaderFactory = (AbstractAlbumCursorLoaderCreator) mLoaderCreator;
        return new AlbumArtCursorAdapter(getActivity(),
                R.layout.grid_item_albums, from, to, R.id.album_art,
                loaderFactory.getAlbumIdColumnName());
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        Bundle extras = mExtras;
        if (extras == null) {
            return new AlbumsCursorLoaderCreator(getActivity());
        }

        long artistId = mArtistID = mExtras.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY, AudioStorage.WRONG_ID);
        if (artistId != AudioStorage.WRONG_ID) {
            return new ArtistAlbumsCursorLoaderCreator(getActivity(), artistId);
        } else {
            /*this won't be executed, but keep this as "default value"*/
            return new AlbumsCursorLoaderCreator(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums_browser, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.list);
        gridView.setAdapter(mCursorAdapter);
        AbstractAlbumCursorLoaderCreator loaderCreator = (AbstractAlbumCursorLoaderCreator) mLoaderCreator;
        gridView.setOnItemClickListener(new OnAlbumClickListener(getActivity(),
                mCursorAdapter, mExtras,
                loaderCreator.getAlbumIdColumnName(), loaderCreator.getAlbumColumnName()));
        gridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), gridView,
                new AlbumMenuCommandSet(this, mServiceWrapper, mArtistID),
                loaderCreator.getAlbumIdColumnName()));
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    protected void setIndicator(PlaybackServiceWrapper serviceWrapper,
                                IndicatorCursorAdapter adapter,
                                AbstractCursorLoaderCreator loaderCreator) {
        AbstractAlbumCursorLoaderCreator creator =
                (AbstractAlbumCursorLoaderCreator) loaderCreator;
        adapter.setIndicatorFor(creator.getAlbumIdColumnName(), mServiceWrapper.getAlbumID());
    }
}
