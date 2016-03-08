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

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.RecommendationsAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.FavoriteGenresRetrieverAsyncTask;

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
        //TODO: debug only
        new FavoriteGenresRetrieverAsyncTask(null).execute(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommendations, container, false);
        RecyclerView recyclerView = findById(v, R.id.list);
        recyclerView.setAdapter(new RecommendationsAdapter(mDataset));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        return v;
    }
}
