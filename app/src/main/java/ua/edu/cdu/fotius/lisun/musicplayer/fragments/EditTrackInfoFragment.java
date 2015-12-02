package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoAsyncQueryHandler;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoQueryCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.TrackInfo;

public class EditTrackInfoFragment extends Fragment implements EditInfoAsyncQueryHandler.QueryCallbacks{

    public static final String TAG = "edit_info";

    //TODO: possibly move to TrackInfo class
    public static final String TRACK_ID_KEY = "track_id";
    private EditInfoQueryCreator mEditInfoQueryCreator;
    private TrackInfo mTrackInfo;
    private EditText mTitleEditText;
    private EditText mAlbumEditText;
    private EditText mArtistEditText;
    private EditText mYearEditText;

    public EditTrackInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(TAG, "onCreate()");
        Bundle arguments = getArguments();
        long trackId = arguments.getLong(TRACK_ID_KEY);
        mTrackInfo = new TrackInfo(trackId);
        mEditInfoQueryCreator = new EditInfoQueryCreator(trackId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_track_info, container, false);
        mTitleEditText = (EditText)v.findViewById(R.id.title_input);
        mAlbumEditText = (EditText)v.findViewById(R.id.album_input);
        mArtistEditText = (EditText)v.findViewById(R.id.artist_input);
        mYearEditText = (EditText)v.findViewById(R.id.year_input);

        /*Should be called after initializing EditText views*/
        if (savedInstanceState == null) {
            EditInfoAsyncQueryHandler ed =
                    new EditInfoAsyncQueryHandler(getActivity().getContentResolver(), mEditInfoQueryCreator, this);
            ed.queryTrackInfo();
        }

        Log.d(TAG, "trackId: " + mTrackInfo.getTrackId());

        return v;
    }

    @Override
    public void onQueryTrackInfoStarted() {
    }

    @Override
    public void onQueryTrackInfoCompleted(Cursor c) {

        c.moveToFirst();

        int idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getTitleColumnName());
        mTrackInfo.setTitle(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getAlbumColumnName());
        mTrackInfo.setAlbum(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getArtistColumnName());
        mTrackInfo.setArtist(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getYearColumnName());
        mTrackInfo.setYear(c.getInt(idx));

        mTitleEditText.setText(mTrackInfo.getTitle());
        mAlbumEditText.setText(mTrackInfo.getAlbum());
        mArtistEditText.setText(mTrackInfo.getArtist());
        mYearEditText.setText(Integer.toString(mTrackInfo.getYear()));
    }
}
