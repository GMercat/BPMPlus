package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

public class DialogSortList extends DialogFragment{

    public interface DialogSortListListener {
        void onDialogSortListPositiveClick(DialogSortList aDialog);
    }

    public enum SortType {
        eCreation,
        eTitle,
        eArtist,
        eBPM
    }

    /// Members
    DialogSortListListener mListener;

    RadioGroup  mRadioGroup;

    SortType mRadioChecked = SortType.eCreation;

    public SortType getRadioChecked () {
        return mRadioChecked;
    }

    public void setRadioChecked (SortType aRadioChecked) {
        mRadioChecked = aRadioChecked;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sort, null);
        mRadioGroup = (RadioGroup)view.findViewById(R.id.sort_radioGroup);

        switch (mRadioChecked) {
            case eTitle:
                mRadioGroup.check(R.id.sort_by_title);
                break;
            case eArtist:
                mRadioGroup.check(R.id.sort_by_artist);
                break;
            case eBPM:
                mRadioGroup.check(R.id.sort_by_bpm);
                break;
            case eCreation:
            default:
                mRadioGroup.check(R.id.sort_by_creation);
                break;
        }

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
                    mRadioChecked = sortIdToSortType(mRadioGroup.getCheckedRadioButtonId());
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

    private SortType sortIdToSortType(int aId) {
        SortType sortType;
        switch (aId) {
            case R.id.sort_by_title:
                sortType = SortType.eTitle;
                break;
            case R.id.sort_by_artist:
                sortType = SortType.eArtist;
                break;
            case R.id.sort_by_bpm:
                sortType = SortType.eBPM;
                break;
            case R.id.sort_by_creation:
            default:
                sortType = SortType.eCreation;
                break;
        }
        return sortType;
    }
}
