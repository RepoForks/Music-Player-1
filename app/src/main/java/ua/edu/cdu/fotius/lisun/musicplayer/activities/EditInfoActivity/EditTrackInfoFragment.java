package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.EditTextWithValidation;

//ToDO: maybe extends BaseFragment
public class EditTrackInfoFragment extends Fragment implements EditInfoAsyncQueryHandler.QueryCallbacks{

    public static final String TAG = "edit_info";

    //TODO: possibly move to TrackInfo class
    public static final String TRACK_ID_KEY = "track_id";

    private long mTrackId;

    private EditInfoQueryCreator mEditInfoQueryCreator;

    private EditTextWithValidation mTitleEditText;
    private EditTextWithValidation mAlbumEditText;
    private EditTextWithValidation mArtistEditText;
    private EditTextWithValidation mYearEditText;

    private EditInfoAsyncQueryHandler mQueryHandler;

    public EditTrackInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle arguments = getArguments();
        mTrackId = arguments.getLong(TRACK_ID_KEY);
        mEditInfoQueryCreator = new EditInfoQueryCreator(mTrackId);
        mQueryHandler = new EditInfoAsyncQueryHandler(getActivity().getContentResolver(), mEditInfoQueryCreator, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_track_info, container, false);

        mTitleEditText = (EditTextWithValidation)v.findViewById(R.id.title_input);
        mTitleEditText.setValidators(new TitleValidatorsSetCreator(getActivity()).create());

        mAlbumEditText = (EditTextWithValidation)v.findViewById(R.id.album_input);
        mAlbumEditText.setValidators(new AlbumValidatorsSetCreator(getActivity()).create());

        mArtistEditText = (EditTextWithValidation)v.findViewById(R.id.artist_input);
        mArtistEditText.setValidators(new ArtistValidatorSetCreator(getActivity()).create());

        mYearEditText = (EditTextWithValidation)v.findViewById(R.id.year_input);
        mYearEditText.setValidators(new YearValidatorSetCreator(getActivity()).create());

        /*Should be called after initializing EditText views*/
        if (savedInstanceState == null) {
            mQueryHandler.queryTrackInfo();
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
        mTitleEditText.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getAlbumColumnName());
        mAlbumEditText.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getArtistColumnName());
        mArtistEditText.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mEditInfoQueryCreator.getYearColumnName());
        mYearEditText.setInitialText(Integer.toString(c.getInt(idx)));

        c.close();
    }

    @Override
    public void onUpdateTrackInfoStarted() {

    }

    @Override
    public void onUpdateTrackInfoCompleted() {
        Log.d(TAG, "Update completed");
    }

    public void doneEditing() {
        if(isAllEnteredTextValid()) {
            ContentValues contentValues = new ContentValues();
            if(mTitleEditText.isChanged()) {
                contentValues.put(mEditInfoQueryCreator.getTitleColumnName(),
                        mTitleEditText.getText().toString());
            }

            if(mAlbumEditText.isChanged()) {
                contentValues.put(mEditInfoQueryCreator.getAlbumColumnName(),
                        mAlbumEditText.getText().toString());
            }

            if(mArtistEditText.isChanged()) {
                contentValues.put(mEditInfoQueryCreator.getArtistColumnName(),
                        mArtistEditText.getText().toString());
            }

            if(mYearEditText.isChanged()) {
                contentValues.put(mEditInfoQueryCreator.getYearColumnName(),
                        mYearEditText.getText().toString());
            }

            mQueryHandler.updateTrackInfo(contentValues);
            //TODO: make update query
        }
    }

    private boolean isAllEnteredTextValid() {
        if(!isSpecificEnteredTextValid(mTitleEditText)) {
            return false;
        }

        if(!isSpecificEnteredTextValid(mAlbumEditText)) {
            return false;
        }

        if(!isSpecificEnteredTextValid(mArtistEditText)) {
            return false;
        }

        if(!isSpecificEnteredTextValid(mYearEditText)) {
            return false;
        }

        return true;
    }

    private boolean isSpecificEnteredTextValid(EditTextWithValidation editText) {
        BaseValidator.ValidationResult validationResult = new BaseValidator.ValidationResult();
        editText.validateInput(validationResult);
        if(!validationResult.mIsSuccessful) {
            notifyUserAboutInvalidInput(editText.getContentDescription().toString(),
                    validationResult.mInvalidityMessage);
            return false;
        }
        return true;
    }

    private void notifyUserAboutInvalidInput(String invalidFieldName, String invalidityMessage) {
        String message = getActivity().getResources().getString(R.string.invalid_input,
                invalidFieldName, invalidityMessage);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
