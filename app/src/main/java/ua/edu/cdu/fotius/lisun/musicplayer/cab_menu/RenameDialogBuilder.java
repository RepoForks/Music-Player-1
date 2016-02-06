package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class RenameDialogBuilder extends BaseDialog.Builder {
    private LayoutInflater mInflater;
    private String mOldName;

    public RenameDialogBuilder(Context context, LayoutInflater inflater, String oldName) {
        super(context);
        mInflater = inflater;
        mOldName = oldName;
    }

    public RenameDialogBuilder(Context context, int theme, LayoutInflater inflater, String oldName) {
        super(context, theme);
        mInflater = inflater;
        mOldName = oldName;
    }

    @Override
    public AlertDialog create() {
        setInitInputValue();
        setTitle(getContext().getResources().getString(R.string.rename_dialog_title));
        setPositiveButton(R.string.rename_dialog_positive_button,
                new OnRenameDialogPositiveClick(mAsyncTask));
        setNegativeButton(R.string.dialog_negative_button, new OnDialogNegativeClick());
        return super.create();
    }

    private void setInitInputValue() {
        View v = mInflater.inflate(R.layout.rename_dialog, null);
        EditText inputField = (EditText) v.findViewById(R.id.playlist_name);
        inputField.setText(mOldName);
        inputField.setSelection(mOldName.length());
        setView(v);
    }
}
