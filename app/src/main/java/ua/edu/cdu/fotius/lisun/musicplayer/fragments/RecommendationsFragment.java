package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static butterknife.ButterKnife.findById;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.RecommendationsAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.FavoriteGenresRetrieverAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.TrackInfoRealm;

public class RecommendationsFragment extends Fragment {

    public static String TAG = "recommendations_fragment";

    private String[] mDataset = new String[] {
            "One", "Two", "Three", "Four", "Five"
    };

    private Context mContext;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommendations, container, false);
        RealmRecyclerView recyclerView = findById(v, R.id.list);

        Realm realm = Realm.getInstance(mContext);
        RealmResults<TrackInfoRealm> results = realm.allObjects(TrackInfoRealm.class);
        Log.d(TAG, "RESULTS: " + results.size());
        recyclerView.setAdapter(new RecommendationsAdapter(mContext, results, true, true));

        new FavoriteGenresRetrieverAsyncTask(getActivity().getApplicationContext(), null).execute(mContext);
        return v;
    }
}
