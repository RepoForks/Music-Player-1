package ua.edu.cdu.fotius.lisun.musicplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ViewHolder>{

    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text) public TextView mText;

        public ViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

    public RecommendationsAdapter(String[] dataset) {
        mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommendations_single_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mText.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
