package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static butterknife.ButterKnife.findById;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.RecommendationsAdapter;

public class RecommendationsFragment extends Fragment {

    public static String TAG = "recommendations_fragment";

    private String[] mDataset = new String[] {
            "One", "Two", "Three", "Four", "Five"
    };

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
