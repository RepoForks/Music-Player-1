package ua.edu.cdu.fotius.lisun.musicplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnRecommendationClick;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.TrackInfoRealm;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ViewHolder> {

    private List<TrackInfoRealm> mTracks;

    public RecommendationsAdapter() {
        mTracks = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recommendations_single_item, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TrackInfoRealm trackInfo = mTracks.get(i);
        viewHolder.mTrackName.setText(trackInfo.getTrack_name());
        viewHolder.mArtistName.setText(trackInfo.getArtist());
        viewHolder.mRootView.setOnClickListener(new OnRecommendationClick(trackInfo));
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    public void setTracks(List<TrackInfoRealm> tracks) {
        if (tracks != null) {
            mTracks = tracks;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mRootView;
        @BindView(R.id.track_name) public TextView mTrackName;
        @BindView(R.id.artist_name) public TextView mArtistName;

        public ViewHolder(View rootView) {
            super(rootView);
            mRootView = rootView;
            ButterKnife.bind(this, rootView);
        }
    }
}
