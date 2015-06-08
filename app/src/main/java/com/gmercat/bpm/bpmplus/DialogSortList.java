package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DialogSortList extends DialogFragment{

    public interface DialogSortListListener {
        void onDialogSortListPositiveClick(DialogSortList aDialog);
    }

    /// Members
    DialogSortListListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sort, null);

        builder.setTitle(R.string.sort_list_text);

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
                    mListener.onDialogSortListPositiveClick(DialogSortList.this);
                }
            });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogSortListListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogSortListListener");
        }
    }
}
