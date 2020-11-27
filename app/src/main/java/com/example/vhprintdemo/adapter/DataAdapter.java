package com.example.vhprintdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.vhprintdemo.R;
import com.example.vhprintdemo.bean.EquiData;

import java.util.List;

/**
 * @author HP
 */
public class DataAdapter extends ArrayAdapter<EquiData> {
    private int resourceId;

    public DataAdapter(@NonNull Context context, int resource, @NonNull List<EquiData> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EquiData equiData = getItem(position);

        //获取子视图控件实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView fruitImage = (TextView) view.findViewById(R.id.equi_no);
        TextView fruitName = (TextView) view.findViewById(R.id.equi_name);
        //将Fruit实例内的水果名和水果图片赋值给子视图控件实例
        fruitImage.setText(equiData.getEquiNo());
        fruitName.setText(equiData.getEquiName());

        return view;
    }
}
