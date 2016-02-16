package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.GenresLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnGenreClickListener;

public class GenresFragment extends BaseFragment {

    public static final String GENRE_ID_KEY = "genre_id_key";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_genres, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        GenresLoaderCreator loaderCreator = (GenresLoaderCreator) mLoaderCreator;
        listView.setOnItemClickListener(new OnGenreClickListener(getActivity(), mCursorAdapter,
                loaderCreator.getGenreIdColumn(), loaderCreator.getGenreColumn()));
        return v;
    }

    @Override
    protected BaseCursorAdapter createCursorAdapter() {
        GenresLoaderCreator loaderFactory = (GenresLoaderCreator) mLoaderCreator;
        String[] from = new String[] { loaderFactory.getGenreColumn()};
        int[] to = new int[] { R.id.genre };

        return new BaseCursorAdapter(getActivity(),
                R.layout.row_genres_list, from, to);
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        return new GenresLoaderCreator(getActivity());
    }

    //TODO: move to super
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public String defineEmptyListMessage() {
        return getActivity().getResources().getString(R.string.genres);
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper,
                                BaseCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator) {}
}
