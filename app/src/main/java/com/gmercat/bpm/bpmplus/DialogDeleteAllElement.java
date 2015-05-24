package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogDeleteAllElement extends DialogFragment{

    public interface DialogDeleteAllElementListener {
        void onDialogDeleteAllElementPositiveClick();
    }

    /// Members
    DialogDeleteAllElementListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage (R.string.delete_all_text)
        // Add action buttons
            .setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Nothing
            }
            })
            .setPositiveButton(R.string.delete_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListener.onDialogDeleteAllElementPositiveClick();
                }
            });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogDeleteAllElementListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogDeleteAllElementListener");
        }
    }
}
