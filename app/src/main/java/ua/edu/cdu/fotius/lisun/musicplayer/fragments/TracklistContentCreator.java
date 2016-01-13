package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.BaseMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;

/**
 * Created by andrei on 16.12.2015.
 */
public class TracklistContentCreator extends BaseTracklistContentCreator{

    @Override
    public CursorAdapter createCursorAdapter(AbstractTracksCursorLoaderCreator loaderCreator, Context context) {
        String[] from = new String[]{loaderCreator.getTrackColumnName(),
                loaderCreator.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new AlbumArtCursorAdapter(context, R.layout.row_tracks_list, from, to, R.id.album_art,
                loaderCreator.getAlbumIdColumnName());
    }

    @Override
    public ListView createListView(View v, LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.fragment_tracks_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        return listView;
    }

    @Override
    public int getMultichoiceMode() {
        return ListView.CHOICE_MODE_MULTIPLE_MODAL;
    }

    @Override
    public BaseMenuCommandSet getMenuCommandSet(Fragment fragment, PlaybackServiceWrapper serviceWrapper) {
        return new TrackMenuCommandSet(fragment, serviceWrapper);
    }


}
