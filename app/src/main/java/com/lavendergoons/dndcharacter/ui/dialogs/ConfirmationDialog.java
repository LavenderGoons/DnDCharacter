package com.lavendergoons.dndcharacter.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.lavendergoons.dndcharacter.R;

/**
 * Generic Confirmation Dialog to confirm
 * Sensitive actions.
 */

public class ConfirmationDialog extends DialogFragment {

    public ConfirmationDialog() {
        super();
    }

    public interface ConfirmationDialogInterface {
        void ConfirmDialogOk(Object o);
        void ConfirmDialogCancel(Object o);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ConfirmationDialogInterface mInterface = (ConfirmationDialogInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +" must implement ConfirmationDialogInterface");
        }
    }

    public static void showConfirmDialog(Context context, String mMessage, final ConfirmationDialog.ConfirmationDialogInterface target, final Object o) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.confirm));

        String message = (mMessage.length() > 0) ? mMessage : context.getString(R.string.title_generic_confirm);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                target.ConfirmDialogOk(o);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                target.ConfirmDialogCancel(o);
            }
        });
        builder.create().show();
    }
}
