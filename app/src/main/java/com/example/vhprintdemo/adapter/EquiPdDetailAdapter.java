package com.example.vhprintdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vhprintdemo.R;
import com.example.vhprintdemo.bean.EquiCheckPd;
import com.example.vhprintdemo.bean.EquiCheckPdDetail;

import java.util.LinkedList;

public class EquiPdDetailAdapter extends BaseAdapter {
    private LinkedList<EquiCheckPdDetail> mData;
    private Context mContext;

    public EquiPdDetailAdapter(LinkedList<EquiCheckPdDetail> mData, Context mContext) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_equipddetail, parent,false);
        ImageView img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.txt_aName);
        TextView txt_aSpeak = (TextView) convertView.findViewById(R.id.txt_aSpeak);
        TextView txt_pai = (TextView) convertView.findViewById(R.id.txt_pai);
        img_icon.setBackgroundResource(R.mipmap.ic_icon_dy);
        txt_aName.setText("编码:"+mData.get(position).getEquiArchCode());
        txt_aSpeak.setText("名称:"+mData.get(position).getEquiName());
        txt_pai.setText("品牌:无  "+"型号:"+mData.get(position).getEquiModel());
        return convertView;
    }
}
