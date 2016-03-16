package ua.edu.cdu.fotius.lisun.musicplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.TrackInfoRealm;

public class RecommendationsAdapter extends RealmBasedRecyclerViewAdapter<TrackInfoRealm,
        RecommendationsAdapter.ViewHolder> {

    public static class ViewHolder extends RealmViewHolder {

        @Bind(R.id.track_name) public TextView mTrackName;
        @Bind(R.id.artist_name) public TextView mArtistName;
        @Bind(R.id.album_name) public TextView mAlbumName;

        public ViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

    private final String TAG = getClass().getSimpleName();

    public RecommendationsAdapter(
            Context context,
            RealmResults<TrackInfoRealm> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recommendations_single_item, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        TrackInfoRealm trackInfo = realmResults.get(i);
        Log.d(TAG, "OnBindViewHolder: " + trackInfo.isValid());
        viewHolder.mTrackName.setText(trackInfo.getTrack_name());
        viewHolder.mArtistName.setText(trackInfo.getArtist());
        viewHolder.mAlbumName.setText(trackInfo.getAlbum());
    }
}
