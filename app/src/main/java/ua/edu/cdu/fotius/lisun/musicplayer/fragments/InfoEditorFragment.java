package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.AlbumValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.ArtistValidatorSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.EditInfoQueryCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.TitleValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.YearValidatorSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.QueryTrackInfoAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.UpdateTrackInfoAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

//ToDO: maybe extends BaseFragment
public class InfoEditorFragment extends Fragment implements QueryTrackInfoAsyncTask.Callback,
        UpdateTrackInfoAsyncTask.Callback, ServiceConnectionObserver {

    public static final String TAG = "edit_info";
    public static final String TRACK_ID_KEY = "track_id";

    private long mTrackId;

    private EditInfoQueryCreator mQueryCreator;

    private EditTextWithValidation mTitle;
    private EditTextWithValidation mAlbum;
    private EditTextWithValidation mArtist;
    private EditTextWithValidation mYear;

    private MediaPlaybackServiceWrapper mServiceWrapper;

    public InfoEditorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle arguments = getArguments();
        mTrackId = arguments.getLong(TRACK_ID_KEY);
        mQueryCreator = new EditInfoQueryCreator(mTrackId);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindFromService(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_track_info, container, false);

        ImageButton doneEditingButton = (ImageButton) v.findViewById(R.id.done_editing);
        doneEditingButton.setOnClickListener(mOnDoneEditingClick);

        mTitle = (EditTextWithValidation) v.findViewById(R.id.title_input);
        mAlbum = (EditTextWithValidation) v.findViewById(R.id.album_input);
        mArtist = (EditTextWithValidation) v.findViewById(R.id.artist_input);
        mYear = (EditTextWithValidation) v.findViewById(R.id.year_input);

        /*Should be called after initializing EditText views*/
        if (savedInstanceState == null) {
            new QueryTrackInfoAsyncTask(this, mQueryCreator, this).execute();
        }
        return v;
    }

    public void doneEditing() {
        if (!isAllEnteredTextValid()) return;

        ContentValues contentValues = new ContentValues();
        if (mTitle.isChanged()) {
            contentValues.put(mQueryCreator.getTitleColumn(),
                    mTitle.getText().toString());
        }

        if (mAlbum.isChanged()) {
            contentValues.put(mQueryCreator.getAlbumColumn(),
                    mAlbum.getText().toString());
        }

        if (mArtist.isChanged()) {
            contentValues.put(mQueryCreator.getArtistColumn(),
                    mArtist.getText().toString());
        }

        if (mYear.isChanged()) {
            contentValues.put(mQueryCreator.getYearColumn(),
                    mYear.getText().toString());
        }

        if(contentValues.size() == 0) {
            getActivity().finish();
            return;
        }

        new UpdateTrackInfoAsyncTask(this, contentValues, mQueryCreator, this).execute();
    }


    private boolean isAllEnteredTextValid() {
        if (!isSpecificEnteredTextValid(mTitle)) {
            return false;
        }

        if (!isSpecificEnteredTextValid(mAlbum)) {
            return false;
        }

        if (!isSpecificEnteredTextValid(mArtist)) {
            return false;
        }

        if (!isSpecificEnteredTextValid(mYear)) {
            return false;
        }

        return true;
    }

    private boolean isSpecificEnteredTextValid(EditTextWithValidation editText) {
        editText.setValidators(new TitleValidatorsSetCreator(getActivity()).create());
        BaseValidator.ValidationResult validationResult = new BaseValidator.ValidationResult();
        editText.validateInput(validationResult);
        if (!validationResult.isSuccessful) {
            notifyUserAboutInvalidInput(editText.getContentDescription().toString(),
                    validationResult.invalidityMessage);
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
        mServiceWrapper.updateCurrentTrackInfo();
        Activity activity = getActivity();
        if (activity == null) return;
        activity.finish();
    }

    @Override
    public void queryCompleted(Cursor c) {
        /*if couldn't retrieve track info*/
        if ((c == null) || (!c.moveToFirst())) {
            onQueryError();
            return;
        }
        onQuerySucceed(c);
    }

    private void onQueryError() {
        Activity activity = getActivity();
        if (activity == null) return;
        Toast.makeText(activity, activity.getResources()
                .getString(R.string.query_track_info_error), Toast.LENGTH_SHORT).show();
    }

    private void onQuerySucceed(Cursor c) {
        int idx = c.getColumnIndexOrThrow(mQueryCreator.getTitleColumn());
        mTitle.setEnabled(true);
        String title = c.getString(idx);
        mTitle.setInitialText(title);
        mTitle.setSelection(title.length());

        idx = c.getColumnIndexOrThrow(mQueryCreator.getAlbumColumn());
        mAlbum.setEnabled(true);
        mAlbum.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mQueryCreator.getArtistColumn());
        mArtist.setEnabled(true);
        mArtist.setInitialText(c.getString(idx));

        idx = c.getColumnIndexOrThrow(mQueryCreator.getYearColumn());
        mYear.setEnabled(true);
        mYear.setInitialText(Integer.toString(c.getInt(idx)));

        c.close();
    }

    @Override
    public void ServiceConnected() {
    }

    @Override
    public void ServiceDisconnected() {
    }
}
