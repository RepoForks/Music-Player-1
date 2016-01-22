package ua.edu.cdu.fotius.lisun.musicplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.views.BaseNameTextView;

public class BaseCursorAdapter extends SimpleCursorAdapter {
    private long mCurrentId = AudioStorage.WRONG_ID;
    private String mIdColumn = null;
    private boolean mIsPlaying = false;
    private int mPlayIndicatorColor;
    private AbsListView mAbsListView = null;
    //private int mCurrentPositionInList = -1;

    public BaseCursorAdapter(Context context, int rowLayout, String[] from, int[] to) {
        super(context, rowLayout, /*cursor*/null, from, to, /*don't register content observer*/0);
        mPlayIndicatorColor = context.getResources().getColor(R.color.accent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mAbsListView = (AbsListView) parent;
        //mCurrentPositionInList = cursor.getPosition();
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View rowLayout, Context context, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = rowLayout.findViewById(to[i]);
            if (v == null) {
                continue;
            }

            String text = cursor.getString(from[i]);
            if (text == null) {
                text = "";
            }

            if (v instanceof TextView) {
                setViewText((TextView) v, text);
            } else if (v instanceof ImageView) {
                setViewImage((ImageView) v, text);
            } else {
                throw new IllegalStateException(v.getClass().getName() + " is not a " +
                        " view that can be bounds by this BaseSimpleCursorAdapter");
            }
        }

        tryToSetCheckedIndicator(rowLayout, cursor);
        tryToSetPlayIndicator(rowLayout, cursor);
    }

    @Override
    public void setViewText(TextView v, String text) {
        if(v instanceof BaseNameTextView) {
            ((BaseNameTextView)v).setName(text);
        } else {
            v.setText(text);
        }
    }

    private void tryToSetCheckedIndicator(View rowLayout, Cursor cursor) {
        if(mAbsListView.getChoiceMode() == AbsListView.CHOICE_MODE_NONE) return;

        View checkedIndicator = rowLayout.findViewById(R.id.checked_indicator);
        if(checkedIndicator == null) {
            throw new RuntimeException(
                    "Your content must have a view " +
                            "whose id attribute is " +
                            "'R.id.checked_indicator' or you should " +
                            "set AbsListView.CHOICE_MODE_NONE for AbsListView");
        }

        SparseBooleanArray checkedPositions = mAbsListView.getCheckedItemPositions();
        boolean checked = checkedPositions.get(cursor.getPosition(), false);

        if(checked) {
            checkedIndicator.setVisibility(View.VISIBLE);
        } else {
            checkedIndicator.setVisibility(View.GONE);
        }
    }

    private void tryToSetPlayIndicator(View rowLayout, Cursor cursor) {
        ImageView indicator = (ImageView)rowLayout.findViewById(R.id.play_indicator);
        /*if resource doesn't contain indicator.*/
        if(indicator == null) return;

        if(mCurrentId == AudioStorage.WRONG_ID) {
            indicator.setVisibility(View.GONE);
            return;
        }

        int idIdx = cursor.getColumnIndexOrThrow(mIdColumn);
        long id = cursor.getLong(idIdx);

        if ((id == mCurrentId) && mIsPlaying) {
            AnimationDrawable animation = (AnimationDrawable)
                    mContext.getResources().getDrawable(R.drawable.ic_equalizer_white_18dp);
            indicator.setImageDrawable(animation);
            indicator.setColorFilter(mPlayIndicatorColor);
            indicator.setVisibility(View.VISIBLE);
            if (animation != null) animation.start();
        } else {
            indicator.setVisibility(View.GONE);
        }
    }

    public void setIndicatorFor(String idColumn, long currentId, boolean isPlaying) {
        mCurrentId = currentId;
        mIdColumn = idColumn;
        mIsPlaying = isPlaying;
        //mPlayIndicatorColor = indicatorColor;
        notifyDataSetChanged();
    }
}
