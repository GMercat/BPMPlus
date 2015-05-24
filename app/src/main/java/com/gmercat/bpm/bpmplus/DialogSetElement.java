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
import android.widget.TextView;

public class DialogSetElement extends DialogFragment{

    public interface DialogSetElementListener {
        void onDialogSetElementPositiveClick(DialogSetElement dialog);
    }

    /// Members
    DialogSetElementListener    mListener;
    EditText                    mElementNameEdit;
    String                      mElementName;

    public void setElementName(String aElementName) {
        mElementName = aElementName;
    }

    public String getElementNameEdit() {
        return mElementNameEdit.getText().toString();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set, null);
        mElementNameEdit = (EditText) view.findViewById(R.id.set_element);
        mElementNameEdit.setText(mElementName, TextView.BufferType.EDITABLE);

        builder.setTitle(R.string.title_set_element);

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
                    mListener.onDialogSetElementPositiveClick(DialogSetElement.this);
                }
            });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogSetElementListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogNewElementListener");
        }
    }
}
