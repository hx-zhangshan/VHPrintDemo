package com.example.vhprintdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vhprintdemo.R;
import com.example.vhprintdemo.bean.Animal;
import com.example.vhprintdemo.bean.EquiCheckPd;

import java.util.LinkedList;

public class EquiPdAdapter extends BaseAdapter {
    private LinkedList<EquiCheckPd> mData;
    private Context mContext;

    public EquiPdAdapter(LinkedList<EquiCheckPd> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {


        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_equipd, parent,false);
        ImageView img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.txt_aName);
        TextView txt_aSpeak = (TextView) convertView.findViewById(R.id.txt_aSpeak);
        img_icon.setBackgroundResource(R.mipmap.ic_icon_pd);
        txt_aName.setText(mData.get(position).getDeptName());
        txt_aSpeak.setText(mData.get(position).getCheckNo());
        return convertView;
    }
}
