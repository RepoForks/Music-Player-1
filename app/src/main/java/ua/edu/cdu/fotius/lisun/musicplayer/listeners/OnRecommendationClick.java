package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.TrackInfoRealm;

public class OnRecommendationClick implements View.OnClickListener {

    private TrackInfoRealm mTrackInfo;

    public OnRecommendationClick(TrackInfoRealm trackInfo) {
        mTrackInfo = trackInfo;
    }

    @Override
    public void onClick(View view) {
        String searchRequest = mTrackInfo.getArtist() + " " + mTrackInfo.getTrack_name();
        String urlString="http://www.google.com/search?q=" + searchRequest;
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(intent);
    }
}
