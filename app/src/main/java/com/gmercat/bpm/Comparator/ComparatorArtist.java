package com.gmercat.bpm.Comparator;

import com.gmercat.bpm.bpmplus.BPM;

import java.util.Comparator;

public class ComparatorArtist implements Comparator<BPM> {

    @Override
    public int compare(BPM aBPM1, BPM aBPM2) {
        int resCompar = aBPM1.getArtist().compareToIgnoreCase(aBPM2.getArtist());
        if (resCompar == 0) {
            resCompar = aBPM1.getTitle().compareToIgnoreCase(aBPM2.getTitle());
            if (resCompar == 0) {
                if (aBPM1.getBpm() < aBPM2.getBpm()) {
                    resCompar = -1;
                } else if (aBPM1.getBpm() > aBPM2.getBpm()) {
                    resCompar = 1;
                }
            }
        }
        return resCompar;
    }
}
