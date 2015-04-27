package com.gmercat.bpm.bpmplus;

import android.app.FragmentManager;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Math.*;


public class MainActivity extends ActionBarActivity implements DialogTitle.Communicator{

    /// Members
    private ArrayList<BPM> BPMList = new ArrayList<> ();
    private TextView    BPMText = null;
    private BPMDAO      BPMDataAcces = null;

    private long    LastCurrentTime = 0;
    private int     NbGap       = -1;
    private int     GapTime     = 0;
    private int     BPMValue    = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView bpmListView = (ListView) findViewById(R.id.listBPM);
        bpmListView.setVisibility(View.VISIBLE);

        BPMText = (TextView)findViewById(R.id.bpm_text);
        BPMText.setText (String.valueOf (BPMValue));

        Button bpmButton = (Button)findViewById(R.id.bpm_button);
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

        Button resetButton =  (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBPMValue ();
            }
        });

        Button saveButton =  (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                FragmentManager manager = getFragmentManager();
                DialogTitle dialogTitle = new DialogTitle();
                dialogTitle.show(manager, "dialog_title");
            }
        });

        BPMDataAcces = new BPMDAO (this);
        BPMDataAcces.open();
        Cursor BPMCursor = BPMDataAcces.getAllBPMs ();

        while (BPMCursor.moveToNext()) {
            String Title    = BPMCursor.getString(BPMCursor.getColumnIndex(BPMDAO.NAME));
            int    BPMValue = BPMCursor.getInt(BPMCursor.getColumnIndex(BPMDAO.VALUE));

            BPM BPMElement = new BPM (Title, BPMValue);
            BPMList.add(BPMElement);
        }

        BPMAdapter myBPMAdapter = new BPMAdapter (this, BPMList);
        bpmListView.setAdapter (myBPMAdapter);
        myBPMAdapter.notifyDataSetChanged ();
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogMessage(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show(); // TODO reprendre le message

        // Database
        BPM NewBPM = new BPM (Message, BPMValue);
        int IdNewBPM = BPMDataAcces.add(NewBPM);
        NewBPM.setId (IdNewBPM);

        BPM BPMElement = new BPM (Message, BPMValue);
        BPMList.add(BPMElement);

        resetBPMValue ();
    }

    private void resetBPMValue () {
        LastCurrentTime = 0;
        NbGap = -1;
        GapTime = 0;
        BPMValue = 0;

        BPMText.setText(String.valueOf(BPMValue));
    }
}
