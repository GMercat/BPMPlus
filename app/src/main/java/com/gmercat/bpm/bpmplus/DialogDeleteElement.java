package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogDeleteElement extends DialogFragment{

    public interface DialogDeleteElementListener {
        void onDialogDeleteElementPositiveClick();
    }

    /// Members
    DialogDeleteElementListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();

//        View view = inflater.inflate(R.layout.dialog_delete, null);
//        TextView Text = (TextView)view.findViewById(R.id.delete_element);
//        Text.setText (R.string.delete_text);
//        builder.setView(view)

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
                    listener.onDialogDeleteElementPositiveClick();
                }
            });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DialogDeleteElementListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogDeleteElementListener");
        }
    }
}
