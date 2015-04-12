package com.gmercat.bpm.bpmplus;

import android.app.FragmentManager;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.*;


public class MainActivity extends ActionBarActivity implements DialogTitle.Communicator{

    /// Members
    private List<HashMap<String, String>> BPMList = new ArrayList<>();
    private ListView    BPMListView;
    private TextView    BPMText = null;
    private Button      ResetButton = null;
    private Button      BPMButton = null;
    private Button      SaveButton = null;
    private BPMDAO      BPMDataAcces = null;

    private long    LastCurrentTime = 0;
    private int     NbGap       = -1;
    private int     GapTime     = 0;
    private int     BPMValue    = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BPMListView = (ListView) findViewById(R.id.listBPM);
        BPMListView.setVisibility(View.VISIBLE);

        BPMText = (TextView)findViewById(R.id.bpm_text);
        BPMText.setText (String.valueOf (BPMValue));

        BPMButton = (Button)findViewById(R.id.bpm_button);
        BPMButton.setOnClickListener(new View.OnClickListener() {
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

        ResetButton =  (Button)findViewById(R.id.reset_button);
        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBPMValue ();
            }
        });

        SaveButton =  (Button)findViewById(R.id.save_button);
        SaveButton.setOnClickListener(new View.OnClickListener() {
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

            HashMap<String, String> Element = new HashMap<>();
            Element.put("Title", Title);
            Element.put("BPM", Integer.toString(BPMValue));
            BPMList.add(Element);
        }

    // TODO Use ArrayAdapter
        ListAdapter MyListAdapter = new SimpleAdapter(
                this,
                BPMList,
                R.layout.element_list,
                new String[] {"Title", "BPM"},
                new int[] {R.id.element_title_text, R.id.element_text_bpm});

        BPMListView.setAdapter (MyListAdapter);

        ((BaseAdapter)MyListAdapter).notifyDataSetChanged ();
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

        //noinspection SimplifiableIfStatement
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

        // List TODO list<BPM>
        HashMap<String, String> Element = new HashMap<>();
        Element.put("Title", Message);
        Element.put("BPM", Integer.toString(BPMValue));
        BPMList.add(Element);

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
