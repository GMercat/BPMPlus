package com.gmercat.bpm.Comparator;

import com.gmercat.bpm.bpmplus.BPM;

import java.util.Comparator;

public class ComparatorBPM implements Comparator<BPM> {

    @Override
    public int compare(BPM aBPM1, BPM aBPM2) {
        if (aBPM1.getBpm() < aBPM2.getBpm()) {
            return -1;
        } else if (aBPM1.getBpm() > aBPM2.getBpm()) {
            return 1;
        } else {
            int compTemp = aBPM1.getArtist().compareToIgnoreCase(aBPM2.getArtist());
            if (compTemp == 0) {
                compTemp = aBPM1.getTitle().compareToIgnoreCase(aBPM2.getTitle());
            }
            return compTemp;
        }
    }
}
