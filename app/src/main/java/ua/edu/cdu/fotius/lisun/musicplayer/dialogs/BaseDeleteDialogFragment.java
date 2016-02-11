package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeleteTracksAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDeleteDialogClick;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogNegativeClick;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public abstract class BaseDeleteDialogFragment extends BaseDialogFragment{
    public static String IDS_KEY = "IDS_key";

    protected long[] mIds;

    public static BaseDeleteDialogFragment prepareFragment(BaseDeleteDialogFragment fragment,
                                                           long[] ids) {
        Bundle arguments = new Bundle();
        arguments.putLongArray(IDS_KEY, ids);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mIds = arguments.getLongArray(IDS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delete_dialog, null);

        TextView titleView = (TextView) v.findViewById(R.id.dialog_title);
        titleView.setText(getTitle());

        TextView messageView = (TextView) v.findViewById(R.id.dialog_message);
        messageView.setText(getMessage());

        Button positiveButton = (Button) v.findViewById(R.id.dialog_positive_button);
        positiveButton.setOnClickListener(getPositiveButtonListener());

        Button negativeButton = (Button) v.findViewById(R.id.dialog_negative_button);
        negativeButton.setOnClickListener(new OnDialogNegativeClick(this));

        return v;
    }

    protected abstract String getTitle();
    protected abstract String getMessage();
    protected abstract View.OnClickListener getPositiveButtonListener();
}
