package com.gmercat.bpm.bpmplus;

public class BPM {
    /// Members
    private long    mId;
    private String  mTitle;
    private String  mArtist;
    private int     mBpm;

    public BPM(int aId, String aName, String aArtist, int aBpm) {
        super();
        mId     = aId;
        mTitle  = aName;
        mArtist = aArtist;
        mBpm    = aBpm;
    }

    public long getId() {
        return mId;
    }

    public void setId(long aId) {
        mId = aId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setTitle(String aTitle) {
        mTitle = aTitle;
    }

    public void setArtist(String aArtist) {
        mArtist = aArtist;
    }

    public int getBpm() {
        return mBpm;
    }

    public String getBpmStr() {
        return String.valueOf (mBpm);
    }

    public void setBpm(int aBpm) {
        mBpm = aBpm;
    }
}
