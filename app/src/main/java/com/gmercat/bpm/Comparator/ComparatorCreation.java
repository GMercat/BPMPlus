package com.gmercat.bpm.Comparator;

import com.gmercat.bpm.bpmplus.BPM;

import java.util.Comparator;

public class ComparatorCreation implements Comparator<BPM>{

    @Override
    public int compare(BPM aBPM1, BPM aBPM2) {
        if (aBPM1.getId() < aBPM2.getId()) {
            return -1;
        } else if (aBPM1.getId() > aBPM2.getId()) {
            return 1;
        } else {
            return 0;
        }
    }
}
