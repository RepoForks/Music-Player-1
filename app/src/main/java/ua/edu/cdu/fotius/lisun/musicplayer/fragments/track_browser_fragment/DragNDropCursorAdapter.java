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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;

public class DragNDropCursorAdapter extends BaseSimpleCursorAdapter implements DragNDropListView.OnDragNDropEventListener{

    private final String TAG = getClass().getSimpleName();

    final int INVALID_ID = -1;
    private int mHandlerResourceID;

    private ArrayList<Integer> mPositionInPlaylist;
    /*mIds serves as unique identifier for views in the
     *listview that is why used in getItemId*/
    private ArrayList<Long> mIds;
    private ArrayList<ArrayList<String>> mEachLineData;

    private String mIdColumnName;
    private String mPositionInPlaylistColumnName;

    public DragNDropCursorAdapter(Context context, int layout, String[] from, int[] to,
                                  int handlerResourceId, String idColumnName, String posInPlaylistColumnName) {
        super(context, layout, from, to);
        mIdColumnName = idColumnName;
        mPositionInPlaylistColumnName = posInPlaylistColumnName;

        mHandlerResourceID = handlerResourceId;
        mEachLineData = new ArrayList<ArrayList<String>>();
        mIds = new ArrayList<>();
        mPositionInPlaylist = new ArrayList<Integer>();
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIds.size()) {
            return INVALID_ID;
        }
        return mIds.get(position);
    }

    @Override
    public void bindView(View rowLayout, Context context, Cursor cursor) {
        Log.d(TAG, "EachLineDAta in bindView: " + mEachLineData.size());
        int position = cursor.getPosition();
        ArrayList<String> oneLineData = mEachLineData.get(position);
        for(int i = 0; i < mTo.length; i++) {
            View v = rowLayout.findViewById(mTo[i]);

            if(v == null) continue;

            if(v instanceof TextView) {
                ((TextView) v).setText(oneLineData.get(i));
            }
        }
    }

    public void swapElements(int from, int to) {
        swapEachLineData(from, to);
        swapIds(from, to);
        swapPlayOrder(from, to);
    }

    private void swapEachLineData(int from, int to) {
        Log.d(TAG, "From: " + from + " to: " + to);
        ArrayList<String> tmp = mEachLineData.get(from);
        mEachLineData.set(from, mEachLineData.get(to));
        mEachLineData.set(to, tmp);
    }

    private void swapIds(int from, int to) {

        Log.d(TAG, "id(from) : " + mIds.get(from) + " id(to) : " + mIds.get(to));

        long tmp = mIds.get(from);
        mIds.set(from, mIds.get(to));
        mIds.set(to, tmp);

        Log.d(TAG, "id(from) : " + mIds.get(from) + " id(to) : " + mIds.get(to));
    }

    private void swapPlayOrder(int from, int to) {
        int tmp = mPositionInPlaylist.get(from);
        mPositionInPlaylist.set(from, mPositionInPlaylist.get(to));
        mPositionInPlaylist.set(to, tmp);
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        Log.e(TAG, "Swap Cursor");
        Cursor previouslySetCursor = super.swapCursor(c);
        Cursor cursor = c;
        if(cursor != null && cursor.moveToFirst()) {
            mEachLineData.clear();
            mIds.clear();
            mPositionInPlaylist.clear();
            int trackIdColumn = cursor.getColumnIndexOrThrow(mIdColumnName);
            int playlistPosIdColumn = cursor.getColumnIndexOrThrow(mPositionInPlaylistColumnName);
            while(!cursor.isAfterLast()) {
                mIds.add(cursor.getLong(trackIdColumn));
                mPositionInPlaylist.add(cursor.getInt(playlistPosIdColumn));
                ArrayList<String> oneLineData = new ArrayList<>();
                for(int i = 0; i < mTo.length; i++) {
                    oneLineData.add(cursor.getString(mFrom[i]));
                }
                mEachLineData.add(oneLineData);
                cursor.moveToNext();
            }
        }
        return previouslySetCursor;
    }

    public int getHandlerResourceID() {
        return mHandlerResourceID;
    }

    @Override
    public void onDrop(int from, int to) {
        Log.d(TAG, "Drooooooooop");
    }
}
