package com.gmercat.bpm.bpmplus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

    public View getView (int aPosition, View aView, ViewGroup aParent) {
        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate (R.layout.element_list, aParent, false);

        TextView title = (TextView)itemView.findViewById(R.id.element_title_text);
        TextView bpm   = (TextView)itemView.findViewById(R.id.element_text_bpm);

        title.setText(mListDatas.get (aPosition).getName ());
        bpm.setText(mListDatas.get (aPosition).getBpmStr());

        return itemView;
    }
}
