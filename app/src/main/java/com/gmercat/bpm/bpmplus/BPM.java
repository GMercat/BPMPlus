package com.gmercat.bpm.bpmplus;

public class BPM {
    /// Members
    private long    mId;
    private String  mName;
    private int     mBpm;

    public BPM (String aName, int aBpm) {
        super ();
        mId     = -1;
        mName   = aName;
        mBpm    = aBpm;
    }

    public long getId () {
        return mId;
    }

    public void setId (long aId) {
        mId = aId;
    }

    public String getName () {
        return mName;
    }

    public void setName (String aName) {
        mName = aName;
    }

    public int getBpm () {
        return mBpm;
    }
    public String getBpmStr () {
        return String.valueOf (mBpm);
    }

    public void setBpm (int aBpm) {
        mBpm = aBpm;
    }
}
