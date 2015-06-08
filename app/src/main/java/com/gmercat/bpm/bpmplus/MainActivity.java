package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gmercat.bpm.DAO.BPMAdapter;
import com.gmercat.bpm.DAO.BPMDAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static java.lang.Math.*;


public class MainActivity   extends ActionBarActivity
                            implements  DialogNewElement.DialogNewElementListener,
                                        DialogSetElement.DialogSetElementListener,
                                        DialogDeleteElement.DialogDeleteElementListener,
                                        DialogDeleteAllElement.DialogDeleteAllElementListener  {

    /// Members
    private ArrayList<BPM>  mBPMList        = new ArrayList<>();
    private TextView        mBPMText        = null;
    private BPMDAO          mBPMDataAcces   = null;
    private BPMAdapter      mBPMAdapter     = null;

    private long    mLastCurrentTime            = 0;
    private int     mNbGap                      = -1;
    private int     mGapTime                    = 0;
    private int     mBPMValue                   = 0;
    private int     mPositionElementSelected    = -1;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView bpmListView = (ListView) findViewById(R.id.listBPM);
        bpmListView.setVisibility(View.VISIBLE);

        mBPMText = (TextView)findViewById(R.id.bpm_text);
        mBPMText.setText(String.valueOf(mBPMValue));

        ImageButton bpmButton = (ImageButton)findViewById(R.id.bpm_button);
        bpmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                ++mNbGap;
                if (0 != mNbGap) {
                    mGapTime += currentTime - mLastCurrentTime;
                    float meanGap = (float) mGapTime / (float) mNbGap;

                    mBPMValue = (int) ceil(60000.0 / meanGap);
                    mBPMText.setText(String.valueOf(mBPMValue));
                }
                mLastCurrentTime = currentTime;
            }
        });

        ImageButton resetButton = (ImageButton)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBPMValue();
            }
        });

        ImageButton saveButton = (ImageButton)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewElement newDialogNewElement = new DialogNewElement();
                newDialogNewElement.show(getFragmentManager(), "newElement");
            }
        });

        mBPMDataAcces = new BPMDAO(this);
        mBPMDataAcces.open();
        Cursor bpmCursor = mBPMDataAcces.getAllBPMs();

        while (bpmCursor.moveToNext()) {
            int    id       = bpmCursor.getInt(bpmCursor.getColumnIndex(BPMDAO.KEY));
            String title    = bpmCursor.getString(bpmCursor.getColumnIndex(BPMDAO.TITLE));
            String artist   = bpmCursor.getString(bpmCursor.getColumnIndex(BPMDAO.ARTIST));
            int    bpmValue = bpmCursor.getInt(bpmCursor.getColumnIndex(BPMDAO.VALUE));

            BPM bpmElement = new BPM(id, title, artist, bpmValue);
            mBPMList.add(bpmElement);
        }

        bpmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView aParentView, View aChildView, int aPosition, long aId) {
                mPositionElementSelected = aPosition;
                if (mPositionElementSelected != -1) {
                    DialogSetElement newDialogSetElement = new DialogSetElement();
                    newDialogSetElement.setTitle(mBPMList.get(mPositionElementSelected).getTitle());
                    newDialogSetElement.setArtist(mBPMList.get(mPositionElementSelected).getArtist());
                    newDialogSetElement.show(getFragmentManager(), "setElement");
                }
            }
        });

        bpmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView aParentView, View aChildView, int aPosition, long aId) {
                mPositionElementSelected = aPosition;
                DialogDeleteElement newDialogDeleteElement = new DialogDeleteElement();
                newDialogDeleteElement.show(getFragmentManager(), "deleteElement");
                return true;
            }
        });

        mBPMAdapter = new BPMAdapter(this, mBPMList);
        bpmListView.setAdapter(mBPMAdapter);
        mBPMAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBPMDataAcces.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu aMenu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, aMenu);

        // Locate MenuItem with ShareActionProvider
        MenuItem menuItem = aMenu.findItem(R.id.action_share_all);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        return true;
    }

    private void setShareIntent(Intent aShareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(aShareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem aMenuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (aMenuItem.getItemId()) {

            case R.id.action_share_all:
                onShareList();
                return true;

            case R.id.action_delete_all:
                DialogDeleteAllElement newDialogDeleteAllElement = new DialogDeleteAllElement();
                newDialogDeleteAllElement.show(getFragmentManager(), "deleteAllElement");
                return true;
            default:
                return super.onOptionsItemSelected(aMenuItem);
        }
    }

    @Override
    public void onDialogNewElementPositiveClick(DialogNewElement dialog) {
        String title = dialog.getTitle();
        String artist = dialog.getArtist();
        if (!title.isEmpty()) {
            // Database
            BPM newBPM = new BPM(0, title, artist, mBPMValue);
            int idNewBPM = mBPMDataAcces.add(newBPM);
            newBPM.setId(idNewBPM);

            mBPMList.add(newBPM);

            resetBPMValue();
        }
    }

    @Override
    public void onDialogSetElementPositiveClick(DialogSetElement aDialog) {
        String title = aDialog.getTitle();
        String artist = aDialog.getArtist();
        if (!title.isEmpty() && (mPositionElementSelected != -1)) {
            mBPMList.get(mPositionElementSelected).setTitle(title);
            mBPMList.get(mPositionElementSelected).setArtist(artist);
            mBPMDataAcces.update(mBPMList.get(mPositionElementSelected));
            mBPMAdapter.notifyDataSetChanged();

            mPositionElementSelected = -1;
        }
    }

    @Override
    public void onDialogDeleteElementPositiveClick() {
        if (mPositionElementSelected != -1) {
            mBPMDataAcces.del(mBPMList.get(mPositionElementSelected).getId());
            mBPMList.remove(mPositionElementSelected);
            mBPMAdapter.notifyDataSetChanged();

            mPositionElementSelected = -1;
        }
    }

    @Override
    public void onDialogDeleteAllElementPositiveClick() {
        mBPMDataAcces.delAll();
        mBPMList.clear();
        mBPMAdapter.notifyDataSetChanged();

        mPositionElementSelected = -1;
    }

    private void resetBPMValue() {
        mLastCurrentTime = 0;
        mNbGap = -1;
        mGapTime = 0;
        mBPMValue = 0;

        mBPMText.setText(String.valueOf(mBPMValue));
    }

    private void onShareList() {
        File requestFile = buildFile();

        // Most file-related method calls need to be in try-catch blocks.
        // Use the FileProvider to get a content URI
        try {
            Uri fileUri = FileProvider.getUriForFile(MainActivity.this, "com.gmercat.bpm.bpmplus", requestFile);
            // Set up an Intent to send back to apps that request a file
            Intent requestFileIntent = new Intent(Intent.ACTION_SEND);
            if (fileUri != null) {
                setShareIntent(requestFileIntent);
                // Grant temporary read permission to the content URI
                requestFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                requestFileIntent.setType("application/csv");
                requestFileIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                startActivity(Intent.createChooser(requestFileIntent, getString(R.string.title_share)));

                // Set the result
                MainActivity.this.setResult(Activity.RESULT_OK, requestFileIntent);
            } else {
                requestFileIntent.setDataAndType(null, "");

                // Set the result
                MainActivity.this.setResult(RESULT_CANCELED, requestFileIntent);
            }
        } catch (IllegalArgumentException e) {
            Log.e("File Selector", "The selected file can't be shared: " + requestFile.getName());
        }
    }

    private File buildFile() {
        String fileName = "BPMList.csv";

        BufferedWriter writer = null;
        // Get the files/ subdirectory of internal storage
        File privateRootDir = getFilesDir();
        // Get the files/lists subdirectory;
        File listsDir = new File(privateRootDir, "lists");
        File requestFile = new File(listsDir, fileName);
        try {
            if (!listsDir.exists()) {
                listsDir.mkdir();
            }
            if (requestFile.exists()) {
                requestFile.delete();
            }
            requestFile.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(requestFile)));
            for (BPM bpm : mBPMList) {
                writer.write(bpm.getTitle() + ";" + bpm.getArtist() + ";" + bpm.getBpmStr());
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return requestFile;
    }
}
