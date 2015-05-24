package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogDeleteElement extends DialogFragment{

    public interface DialogDeleteElementListener {
        void onDialogDeleteElementPositiveClick();
    }

    /// Members
    DialogDeleteElementListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage (R.string.delete_text)
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
                    mListener.onDialogDeleteElementPositiveClick();
                }
            });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogDeleteElementListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogDeleteElementListener");
        }
    }
}
