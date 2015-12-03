package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.BaseActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoAsyncQueryHandler;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoQueryCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.TrackInfoValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.TrackInfo;


//ToDO: maybe extends BaseFragment
public class EditTrackInfoFragment extends Fragment implements EditInfoAsyncQueryHandler.QueryCallbacks{

    public static final String TAG = "edit_info";

    //TODO: possibly move to TrackInfo class
    public static final String TRACK_ID_KEY = "track_id";

    private long mTrackId;

    private EditInfoQueryCreator mEditInfoQueryCreator;
    private TrackInfo mPersistentTrackInfo;

    private TextView mTitleLabel;
    private TextView mAlbumLabel;
    private TextView mArtistLabel;
    private TextView mYearLabel;
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
        mTrackId = arguments.getLong(TRACK_ID_KEY);
        mEditInfoQueryCreator = new EditInfoQueryCreator(mTrackId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_track_info, container, false);

        mTitleLabel = (TextView)v.findViewById(R.id.title_label);
        Log.d(TAG, "Title_label text: " + mTitleLabel.getText().toString());
        mAlbumLabel = (TextView)v.findViewById(R.id.album_label);
        mArtistLabel = (TextView)v.findViewById(R.id.artist_label);
        mYearLabel = (TextView)v.findViewById(R.id.year_label);

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
        return v;
    }

    @Override
    public void onQueryTrackInfoStarted() {
    }

    @Override
    public void onQueryTrackInfoCompleted(Cursor c) {
//        if((c == null) && (!c.moveToFirst())) {
//            getActivity().finish();
//            return;
//        }

        c.moveToFirst();

        int idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getTitleColumnName());
        String title = c.getString(idx);

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getAlbumColumnName());
        String album = c.getString(idx);

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getArtistColumnName());
        String artist = c.getString(idx);

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getYearColumnName());
        int year = c.getInt(idx);

        mPersistentTrackInfo = new TrackInfo(mTrackId, title, album, artist, Integer.toString(year));

        mTitleEditText.setText(mPersistentTrackInfo.getTitle());
        mAlbumEditText.setText(mPersistentTrackInfo.getAlbum());
        mArtistEditText.setText(mPersistentTrackInfo.getArtist());
        mYearEditText.setText(mPersistentTrackInfo.getYear());
    }

    public void doneEditing() {
        Log.d(TAG, "doneEditingFunction()");
        //retreive values
        TrackInfo currentTrackInfo = getValuesFromEditTexts();
        //check for validity
        if(isInputValid(currentTrackInfo)) {
            //check for changes
            String[]
            //save to DB
        }
    }

    public Map getChangedFilds() {
        Map<String, String>
    }

    private TrackInfo getValuesFromEditTexts() {
        String title = mTitleEditText.getText().toString();
        String album = mAlbumEditText.getText().toString();
        String artist = mArtistEditText.getText().toString();
        //TODO: But only latter in ValidityChacker parse and if not valid notify user
        String year = mYearEditText.getText().toString();

        return new TrackInfo(mTrackId, title, album, artist, year);
    }

    private boolean isInputValid(TrackInfo trackInfo) {
        TrackInfoValidator validator = new TrackInfoValidator(trackInfo);
        if(!validator.isTitleValid()) {
            notifyUserAboutInvalidInput(mTitleLabel.getText().toString());
            return false;
        }

        if(!validator.isAlbumValid()) {
            notifyUserAboutInvalidInput(mAlbumLabel.getText().toString());
            return false;
        }

        if(!validator.isArtistValid()) {
            notifyUserAboutInvalidInput(mArtistLabel.getText().toString());
            return false;
        }

        if(!validator.isYearValid()) {
            notifyUserAboutInvalidInput(mYearLabel.getText().toString());
            return false;
        }

        return true;
    }

    private void notifyUserAboutInvalidInput(String invalidFieldName) {
        Log.d(TAG, "notification");
        String message = getActivity().getResources().getString(R.string.invalid_input, invalidFieldName);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
