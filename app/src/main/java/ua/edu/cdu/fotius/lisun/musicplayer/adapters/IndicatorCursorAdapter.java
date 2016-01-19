package ua.edu.cdu.fotius.lisun.musicplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.views.BaseNameTextView;

/**
 *An easy adapter to map columns from a cursor to TextViews or ImageViews defined in an XML file.
 * Template method pattern is used to define fragment specific behaviour, such as getting unknown
 * string({@link ua.edu.cdu.fotius.lisun.musicplayer.fragments
 * .BaseSimpleCursorAdapter#getUnknownText(android.content.Context, android.database.Cursor, int)})
 */
public class IndicatorCursorAdapter extends SimpleCursorAdapter {

    private long mCurrentId = AudioStorage.WRONG_ID;
    private String mIdColumn = null;
    private int mIndicatorColor = -1;

    public IndicatorCursorAdapter(Context context, int rowLayout, String[] from, int[] to) {
        super(context, rowLayout, /*cursor*/null, from, to, /*don't register content observer*/0);
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

        tryToSetIndicator(rowLayout, cursor);
    }

    @Override
    public void setViewText(TextView v, String text) {
        if(v instanceof BaseNameTextView) {
            ((BaseNameTextView)v).setName(text);
        } else {
            v.setText(text);
        }
    }

    private void tryToSetIndicator(View rowLayout, Cursor cursor) {
        ImageView indicator = (ImageView)rowLayout.findViewById(R.id.play_indicator);
        /*if resource doesn't contain indicator.*/
        if(indicator == null) return;

        if(mCurrentId == AudioStorage.WRONG_ID) {
            indicator.setVisibility(View.GONE);
            return;
        }

        int idIdx = cursor.getColumnIndexOrThrow(mIdColumn);
        long id = cursor.getLong(idIdx);

        int visibility;
        if (id == mCurrentId) {
            AnimationDrawable animation = (AnimationDrawable)
                    mContext.getDrawable(R.drawable.ic_equalizer_white_24dp);
            indicator.setImageDrawable(animation);
            //holder.mImageView.setImageTintList(sColorStatePlaying);
            indicator.setVisibility(View.VISIBLE);
            if (animation != null) animation.start();
            //visibility = View.VISIBLE;
        } else {
            //visibility = View.GONE;
            indicator.setVisibility(View.GONE);
        }
        //indicator.setVisibility(visibility);
    }

    public void setIndicatorFor(String idColumn, long currentId) {
        mCurrentId = currentId;
        mIdColumn = idColumn;
        //mIndicatorColor = indicatorColor;
        notifyDataSetChanged();
    }
}
