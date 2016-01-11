package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.EditTextWithValidation;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.QueryTrackInfoAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.UpdateTrackInfoAsyncTask;

//ToDO: maybe extends BaseFragment
public class EditTrackInfoFragment extends Fragment implements QueryTrackInfoAsyncTask.Callback, UpdateTrackInfoAsyncTask.Callback{

    public static final String ACTION_TRACK_INFO_CHANGED =
            "ua.edu.cdu.fotius.lisun.musicplayer.track_info_changed";

    public static final String TAG = "edit_info";
    //TODO: possibly move to AudioStorage class
    public static final String TRACK_ID_KEY = "track_id";

    private long mTrackId;

    private EditInfoQueryCreator mQueryCreator;

    private EditTextWithValidation mTitle;
    private EditTextWithValidation mAlbum;
    private EditTextWithValidation mArtist;
    private EditTextWithValidation mYear;

    public EditTrackInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle arguments = getArguments();
        mTrackId = arguments.getLong(TRACK_ID_KEY);
        mQueryCreator = new EditInfoQueryCreator(mTrackId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_track_info, container, false);

        ImageButton doneEditingButton = (ImageButton) v.findViewById(R.id.done_editing);
        doneEditingButton.setOnClickListener(mOnDoneEditingClick);

        mTitle = (EditTextWithValidation)v.findViewById(R.id.title_input);
        mTitle.setValidators(new TitleValidatorsSetCreator(getActivity()).create());

        mAlbum = (EditTextWithValidation)v.findViewById(R.id.album_input);
        mAlbum.setValidators(new AlbumValidatorsSetCreator(getActivity()).create());

        mArtist = (EditTextWithValidation)v.findViewById(R.id.artist_input);
        mArtist.setValidators(new ArtistValidatorSetCreator(getActivity()).create());

        mYear = (EditTextWithValidation)v.findViewById(R.id.year_input);
        mYear.setValidators(new YearValidatorSetCreator(getActivity()).create());

        /*Should be called after initializing EditText views*/
        if (savedInstanceState == null) {
            new QueryTrackInfoAsyncTask(this, mQueryCreator, this).execute();
        }
        return v;
    }

    public void doneEditing() {
        if(isAllEnteredTextValid()) {
            ContentValues contentValues = new ContentValues();
            if(mTitle.isChanged()) {
                contentValues.put(mQueryCreator.getTitleColumnName(),
                        mTitle.getText().toString());
            }

            if(mAlbum.isChanged()) {
                contentValues.put(mQueryCreator.getAlbumColumnName(),
                        mAlbum.getText().toString());
            }

            if(mArtist.isChanged()) {
                contentValues.put(mQueryCreator.getArtistColumnName(),
                        mArtist.getText().toString());
            }

            if(mYear.isChanged()) {
                contentValues.put(mQueryCreator.getYearColumnName(),
                        mYear.getText().toString());
            }

            new UpdateTrackInfoAsyncTask(this, contentValues, mQueryCreator, this).execute();
        }
    }

    private boolean isAllEnteredTextValid() {
        if(!isSpecificEnteredTextValid(mTitle)) {
            return false;
        }

        if(!isSpecificEnteredTextValid(mAlbum)) {
            return false;
        }

        if(!isSpecificEnteredTextValid(mArtist)) {
            return false;
        }

        if(!isSpecificEnteredTextValid(mYear)) {
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

    private View.OnClickListener mOnDoneEditingClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doneEditing();
        }
    };

    @Override
    public void updateCompleted() {
        Intent intent = new Intent();
        intent.setAction(ACTION_TRACK_INFO_CHANGED);
        intent.putExtra(TRACK_ID_KEY, mTrackId);
        getActivity().sendBroadcast(intent);
        getActivity().finish();
    }

    @Override
    public void queryCompleted(Cursor c) {
        /*if couldn't retrieve track info*/
        //TODO: maybe return result to activity and notify user
        if((c == null) || (!c.moveToFirst())) {
            getActivity().finish();
        }

        int idx = c.getColumnIndexOrThrow(mQueryCreator.getTitleColumnName());
        mTitle.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mQueryCreator.getAlbumColumnName());
        mAlbum.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mQueryCreator.getArtistColumnName());
        mArtist.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mQueryCreator.getYearColumnName());
        mYear.setInitialText(Integer.toString(c.getInt(idx)));

        c.close();
    }
}
