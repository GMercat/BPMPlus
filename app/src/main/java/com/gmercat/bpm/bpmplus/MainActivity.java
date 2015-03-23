package com.gmercat.bpm.bpmplus;

import android.app.FragmentManager;
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
    List<HashMap<String, String>> BPMList = new ArrayList<>(); // TODO Charger les données
    ListView    BPMListView;
    TextView    BPMText = null;
    Button      ResetButton = null;
    Button      BPMButton = null;
    Button      SaveButton = null;

    long    LastCurrentTime = 0;
    int     NbGap       = -1;
    int     GapTime     = 0;
    int     BPMValue    = 0;

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
                LastCurrentTime = 0;
                NbGap = -1;
                GapTime = 0;
                BPMValue = 0;

                BPMText.setText(String.valueOf(BPMValue));
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
    public void onDialogMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); // TODO reprendre le message
        HashMap<String, String> Element = new HashMap<>();
        Element.put("Title", message);
        Element.put("BPM", Integer.toString(BPMValue));
        BPMList.add(Element);
    }
}
