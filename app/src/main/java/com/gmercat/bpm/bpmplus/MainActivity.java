package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Math.*;


public class MainActivity   extends Activity
                            implements  DialogNewElement.DialogNewElementListener,
                                        DialogSetElement.DialogSetElementListener,
                                        DialogDeleteElement.DialogDeleteElementListener,
                                        DialogDeleteAllElement.DialogDeleteAllElementListener  {

    /// Members
    private ArrayList<BPM>  BPMList         = new ArrayList<> ();
    private TextView        BPMText         = null;
    private BPMDAO          BPMDataAcces    = null;
    private BPMAdapter      BPMAdapter      = null;

    private long    LastCurrentTime         = 0;
    private int     NbGap                   = -1;
    private int     GapTime                 = 0;
    private int     BPMValue                = 0;
    private int     PositionElementSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView bpmListView = (ListView) findViewById(R.id.listBPM);
        bpmListView.setVisibility(View.VISIBLE);

        BPMText = (TextView)findViewById(R.id.bpm_text);
        BPMText.setText (String.valueOf (BPMValue));

        ImageButton bpmButton = (ImageButton)findViewById(R.id.bpm_button);
        bpmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long CurrentTime = System.currentTimeMillis();

                ++NbGap;
                if (0 != NbGap) {
                    GapTime += CurrentTime - LastCurrentTime;
                    float MeanGap = (float) GapTime / (float) NbGap;

                    BPMValue = (int) ceil(60000.0 / MeanGap);
                    BPMText.setText(String.valueOf(BPMValue));
                }
                LastCurrentTime = CurrentTime;
            }
        });

        ImageButton resetButton =  (ImageButton)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBPMValue ();
            }
        });

        ImageButton saveButton =  (ImageButton)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewElement newDialogNewElement = new DialogNewElement();
                newDialogNewElement.show(getFragmentManager(), "newElement");
            }
        });

        BPMDataAcces = new BPMDAO (this);
        BPMDataAcces.open();
        Cursor BPMCursor = BPMDataAcces.getAllBPMs ();

        while (BPMCursor.moveToNext()) {
            int    id       = BPMCursor.getInt(BPMCursor.getColumnIndex(BPMDAO.KEY));
            String title    = BPMCursor.getString(BPMCursor.getColumnIndex(BPMDAO.NAME));
            int    bpmValue = BPMCursor.getInt(BPMCursor.getColumnIndex(BPMDAO.VALUE));

            BPM BPMElement = new BPM (id, title, bpmValue);
            BPMList.add(BPMElement);
        }

        BPMAdapter = new BPMAdapter (this, BPMList);
        bpmListView.setAdapter (BPMAdapter);
        BPMAdapter.notifyDataSetChanged ();

        bpmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick (AdapterView aParentView, View aChildView, int aPosition, long aId) {
                PositionElementSelected = aPosition;
                if (PositionElementSelected != -1) {
                    DialogSetElement newDialogSetElement = new DialogSetElement();
                    newDialogSetElement.setElementName(BPMList.get(PositionElementSelected).getName());
                    newDialogSetElement.show(getFragmentManager(), "setElement");
                }
            }
        });

        bpmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick (AdapterView aParentView, View aChildView, int aPosition, long aId) {
                PositionElementSelected = aPosition;
                DialogDeleteElement newDialogDeleteElement = new DialogDeleteElement();
                newDialogDeleteElement.show(getFragmentManager(), "deleteElement");
                return true;
            }
        });
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        BPMDataAcces.close ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO options
                return true;
            case R.id.action_delete_all:
                DialogDeleteAllElement newDialogDeleteAllElement = new DialogDeleteAllElement();
                newDialogDeleteAllElement.show(getFragmentManager(), "deleteAllElement");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogNewElementPositiveClick(DialogNewElement dialog) {
        // Database
        BPM NewBPM = new BPM (0, dialog.getTitle (), BPMValue);
        int IdNewBPM = BPMDataAcces.add(NewBPM);
        NewBPM.setId (IdNewBPM);

        BPMList.add(NewBPM);

        resetBPMValue ();
    }

    @Override
    public void onDialogSetElementPositiveClick(DialogSetElement dialog) {
        if (PositionElementSelected != -1) {
            BPMList.get(PositionElementSelected).setName(dialog.getElementNameEdit());
            BPMDataAcces.update(BPMList.get(PositionElementSelected));
            BPMAdapter.notifyDataSetChanged();

            PositionElementSelected = -1;
        }
    }

    @Override
    public void onDialogDeleteElementPositiveClick() {
        if (PositionElementSelected != -1) {
            BPMDataAcces.del(BPMList.get(PositionElementSelected).getId());
            BPMList.remove(PositionElementSelected);
            BPMAdapter.notifyDataSetChanged();

            PositionElementSelected = -1;
        }
    }

    @Override
    public void onDialogDeleteAllElementPositiveClick() {
        BPMDataAcces.delAll();
        BPMList.clear();
        BPMAdapter.notifyDataSetChanged();

        PositionElementSelected = -1;
    }

    private void resetBPMValue () {
        LastCurrentTime = 0;
        NbGap = -1;
        GapTime = 0;
        BPMValue = 0;

        BPMText.setText(String.valueOf(BPMValue));
    }
}
