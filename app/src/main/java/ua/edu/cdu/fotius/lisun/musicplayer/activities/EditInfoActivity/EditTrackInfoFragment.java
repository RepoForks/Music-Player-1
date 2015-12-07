package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;


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

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.AlbumValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.ArtistValidatorSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.BaseValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.InfoElement;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditInfoAsyncQueryHandler;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditInfoQueryCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.TitleValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.TrackInfoHolder;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.YearValidatorSetCreator;

//ToDO: maybe extends BaseFragment
public class EditTrackInfoFragment extends Fragment implements EditInfoAsyncQueryHandler.QueryCallbacks{

    public static final String TAG = "edit_info";

    //TODO: possibly move to TrackInfo class
    public static final String TRACK_ID_KEY = "track_id";

    private long mTrackId;

    private EditInfoQueryCreator mEditInfoQueryCreator;

    private TextView mTitleLabel;
    private TextView mAlbumLabel;
    private TextView mArtistLabel;
    private TextView mYearLabel;

    private TrackInfoHolder mTrackInfoHolder;

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

        mTrackInfoHolder = new TrackInfoHolder();

        int idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getTitleColumnName());
        String title = c.getString(idx);
        InfoElement info = new InfoElement(mTitleLabel.getText().toString(), title, mEditInfoQueryCreator.getTitleColumnName());
        info.setValidators(new TitleValidatorsSetCreator(getActivity()).create());
        mTrackInfoHolder.put(mEditInfoQueryCreator.getTitleColumnName(), info);
        mTitleEditText.setText(title);

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getAlbumColumnName());
        String album = c.getString(idx);
        info = new InfoElement(mAlbumLabel.getText().toString(), album, mEditInfoQueryCreator.getAlbumColumnName());
        info.setValidators(new AlbumValidatorsSetCreator(getActivity()).create());
        mTrackInfoHolder.put(mEditInfoQueryCreator.getAlbumColumnName(), info);
        mAlbumEditText.setText(album);

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getArtistColumnName());
        String artist = c.getString(idx);
        info = new InfoElement(mArtistLabel.getText().toString(), artist, mEditInfoQueryCreator.getArtistColumnName());
        info.setValidators(new ArtistValidatorSetCreator(getActivity()).create());
        mTrackInfoHolder.put(mEditInfoQueryCreator.getArtistColumnName(), info);
        mArtistEditText.setText(artist);

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getYearColumnName());
        int year = c.getInt(idx);
        info = new InfoElement(mYearLabel.getText().toString(), Integer.toString(year),
                mEditInfoQueryCreator.getYearColumnName());
        info.setValidators(new YearValidatorSetCreator(getActivity()).create());
        mTrackInfoHolder.put(mEditInfoQueryCreator.getYearColumnName(), info);
        mYearEditText.setText(Integer.toString(year));

        c.close();
    }

    public void doneEditing() {
        Log.d(TAG, "doneEditingFunction()");
        //retreive values
        //check for validity
        boolean allValid = retreiveAndValidateValuesFromEditTexts();
        if(allValid) {
            Log.d(TAG, "SAVE TO DB");
            //save to DB
        }
    }

    private boolean retreiveAndValidateValuesFromEditTexts() {

        boolean result = validateAndSetValues(mEditInfoQueryCreator.getTitleColumnName(),
                mTitleEditText.getText().toString());
        if(!result) {
            return false;
        }

        result = validateAndSetValues(mEditInfoQueryCreator.getAlbumColumnName(),
                mAlbumEditText.getText().toString());
        if(!result) {
            return false;
        }

        result = validateAndSetValues(mEditInfoQueryCreator.getArtistColumnName(),
                mArtistEditText.getText().toString());
        if(!result) {
            return false;
        }

        result = validateAndSetValues(mEditInfoQueryCreator.getYearColumnName(),
                mYearEditText.getText().toString());
        if(!result) {
            return false;
        }

        return true;
    }

    private boolean validateAndSetValues(String key, String newData) {
        BaseValidator.ValidationResult validationResult = new BaseValidator.ValidationResult();
        mTrackInfoHolder.setDataAt(key, newData, validationResult);
        if(!validationResult.mIsSuccessful) {
            notifyUserAboutInvalidInput(validationResult.mFieldTitle,
                    validationResult.mInvalidityMessage);
            return false;
        }
        return true;
    }

    private void notifyUserAboutInvalidInput(String invalidFieldName, String invalidityMessage) {
        Log.d(TAG, "notification");
        String message = getActivity().getResources().getString(R.string.invalid_input, invalidFieldName, invalidityMessage);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
