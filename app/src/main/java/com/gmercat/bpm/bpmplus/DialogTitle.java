package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DialogTitle extends DialogFragment implements View.OnClickListener{
    /// Members
    EditText title;
    Button cancelButton, okButton;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_title, null);
        title = (EditText) view.findViewById(R.id.title);
        cancelButton = (Button)view.findViewById(R.id.cancel_button);
        okButton = (Button)view.findViewById(R.id.ok_button);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cancel_button) {
            communicator.onDialogMessage("Cancel");
            dismiss();
        }
        else if (view.getId() == R.id.ok_button) {
            communicator.onDialogMessage(title.getText().toString());
            dismiss();
        }
    }

    interface Communicator {
        public void onDialogMessage (String message);
    }
}
