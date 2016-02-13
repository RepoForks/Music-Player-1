package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public abstract class SingleInputDialogFragment extends BaseDialogFragment{

    protected EditTextWithValidation mInputView;
    protected ImageButton mPositiveButton;
    protected ImageButton mNegativeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_single_input, container);
        mInputView = (EditTextWithValidation)v.findViewById(R.id.dialog_input);

        mPositiveButton = (ImageButton)v.findViewById(R.id.dialog_positive_button);
        mPositiveButton.setOnClickListener(getOnPositiveClickListener());

        mNegativeButton = (ImageButton)v.findViewById(R.id.dialog_negative_button);
        mNegativeButton.setOnClickListener(getOnNegativeClickListener());
        return v;
    }

    public abstract View.OnClickListener getOnPositiveClickListener();
    public abstract View.OnClickListener getOnNegativeClickListener();
}
