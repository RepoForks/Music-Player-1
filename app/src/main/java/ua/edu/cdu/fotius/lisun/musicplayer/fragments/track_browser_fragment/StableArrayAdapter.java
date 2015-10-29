/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;

public class StableArrayAdapter extends BaseSimpleCursorAdapter {

    private final String TAG = getClass().getSimpleName();

    final int INVALID_ID = -1;
    private int mHandlerResourceID;

    private ArrayList<Integer> mPositionInPlaylist;
    private ArrayList<Long> mTrackIds;
    private ArrayList<ArrayList<String>> mEachLineData;
//    in super class
//    private String[] mFrom;
//    private int[] mTo;

    public StableArrayAdapter(Context context, int layout, String[] from, int[] to, int handlerResourceId) {
        super(context, layout, from, to);
        mHandlerResourceID = handlerResourceId;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mTrackIds.size()) {
            return INVALID_ID;
        }
        return mTrackIds.get(position);
    }

    @Override
    public boolean hasStableIds() {
        return android.os.Build.VERSION.SDK_INT < 20;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int position = cursor.getPosition();
        ArrayList<String> oneLineData = mEachLineData.get(position);
        for(int i = 0; i < mTo.length; i++) {
            View v = view.findViewById(mTo[i]);

            if(v == null) continue;

            if(v instanceof TextView) {
                ((TextView) v).setText(oneLineData.get(i));
            }
        }
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        if(c != null && c.moveToFirst()) {
            mEachLineData = new ArrayList<>(c.getCount());
            while(!c.isAfterLast()) {
                ArrayList<String> oneLineData = new ArrayList<>();
                for(int i = 0; i < mTo.length; i++) {
                    oneLineData.add(c.getString(mFrom[i]));
                }
                mEachLineData.add(oneLineData);
            }
        }

        return super.swapCursor(c);
    }

    public int getHandlerResourceID() {
        return mHandlerResourceID;
    }
}
