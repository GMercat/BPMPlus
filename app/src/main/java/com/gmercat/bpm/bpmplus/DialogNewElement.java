package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogNewElement extends DialogFragment{

    public interface DialogNewElementListener {
        void onDialogNewElementPositiveClick(DialogNewElement dialog);
    }

    /// Members
    DialogNewElementListener mListener;
    EditText mTitle;
    EditText mArtist;

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getArtist() {
        return mArtist.getText().toString();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new, null);
        mTitle = (EditText) view.findViewById(R.id.add_title);
        mArtist = (EditText) view.findViewById(R.id.add_artist);

        builder.setTitle(R.string.title_add_element);

        builder.setView(view)
        // Add action buttons
            .setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Nothing
                }
            })
            .setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListener.onDialogNewElementPositiveClick(DialogNewElement.this);
                }
            });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogNewElementListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogNewElementListener");
        }
    }
}
