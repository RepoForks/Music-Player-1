package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.GenresCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.PlaylistCursorLoaderCreator;

public class GenresFragment extends BaseListFragment{

    public static final String GENRE_ID_KEY = "genre_id_key";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_genres, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        GenresCursorLoaderCreator loaderCreator = (GenresCursorLoaderCreator) mLoaderCreator;
        listView.setOnItemClickListener(new OnGenreClickListener(getActivity(), mCursorAdapter,
                loaderCreator.getGenreIdColumnName(), loaderCreator.getGenreColumnName()));
        return v;
    }

    @Override
    protected IndicatorCursorAdapter createCursorAdapter() {
        GenresCursorLoaderCreator loaderFactory = (GenresCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[] { loaderFactory.getGenreColumnName()};
        int[] to = new int[] { R.id.genre};

        return new IndicatorCursorAdapter(getActivity(),
                R.layout.row_genres_list, from, to);
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        return new GenresCursorLoaderCreator(getActivity());
    }

    //TODO: move to super
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    protected void setIndicator(PlaybackServiceWrapper serviceWrapper,
                                IndicatorCursorAdapter adapter,
                                AbstractCursorLoaderCreator loaderCreator) {}
}
