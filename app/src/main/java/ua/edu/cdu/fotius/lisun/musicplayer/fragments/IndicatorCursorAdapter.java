package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.BaseNameTextView;

/**
 *An easy adapter to map columns from a cursor to TextViews or ImageViews defined in an XML file.
 * Template method pattern is used to define fragment specific behaviour, such as getting unknown
 * string({@link ua.edu.cdu.fotius.lisun.musicplayer.fragments
 * .BaseSimpleCursorAdapter#getUnknownText(android.content.Context, android.database.Cursor, int)})
 */
public class IndicatorCursorAdapter extends SimpleCursorAdapter {

    private final String TAG = getClass().getSimpleName();

    private long mCurrentId;

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
        View indicator = rowLayout.findViewById(R.id.play_indicator);
        /*if resource doesn't contain indicator.*/
        if(indicator == null) return;

        //TODO: maybe make named constant
        int idIdx = cursor.getColumnIndexOrThrow("_id");
        int visibility;
        long id = cursor.getLong(idIdx);
        Log.d(TAG, "tryToSetIndicator..._id == " + id);

        if (id == mCurrentId) {
            Log.d(TAG, "VISIBLE");
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
        }
        indicator.setVisibility(visibility);
    }

    public void setIndicatorFor(long currentId) {
        mCurrentId = currentId;
        notifyDataSetChanged();
    }
}
