package com.gmercat.bpm.DAO;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmercat.bpm.bpmplus.BPM;
import com.gmercat.bpm.bpmplus.R;

import java.util.ArrayList;

public class BPMAdapter extends BaseAdapter{

    private Activity        mActivity;
    private ArrayList<BPM>  mListDatas;

    public BPMAdapter (Activity aActivity, ArrayList<BPM> aListDatas)
    {
        mActivity = aActivity;
        mListDatas = aListDatas;
    }

    public int getCount () {
        return mListDatas.size();
    }

    public Object getItem(int aPosition) {
        return null;
    }

    public long getItemId(int aPosition) {
        return 0;
    }

    public View getView (int aPosition, View aConvertView, ViewGroup aParent) {
        View view = aConvertView;
        // First check to see if the view is null. if so, we have to inflate it.
        // To inflate it basically means to render, or show, the view.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.element_list, null);
        }

        TextView title = (TextView)view.findViewById(R.id.element_title_text);
        TextView bpm   = (TextView)view.findViewById(R.id.element_text_bpm);

        BPM bpmElement = mListDatas.get (aPosition);
        if (bpmElement != null) {
            title.setText(bpmElement.getName ());
            bpm.setText(bpmElement.getBpmStr());
        }

        return view;
    }
}
