package com.example.vhprintdemo;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vhprintdemo.adapter.DataAdapter;
import com.example.vhprintdemo.bean.EquiData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HP
 */
public class QueryActivity  extends AppCompatActivity {
    private List equiDataList=new ArrayList();
    private ListView mListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_activity);
        initData();
        //创建 适配器
        DataAdapter dataAdapter=new DataAdapter(QueryActivity.this,R.layout.data_list,equiDataList);
        mListView=(ListView) findViewById(R.id.listViewEqui);
        mListView.setAdapter(dataAdapter);
    }

    private void initData() {
        for (int i=0;i<2;i++){
            equiDataList.add(new EquiData("121212","KKKKKK"));
            equiDataList.add(new EquiData("121212","dsjkdsfj "));
            equiDataList.add(new EquiData("121212","撒旦发射点"));
            equiDataList.add(new EquiData("121212","单点等"));
            equiDataList.add(new EquiData("121212","K反对法地方KKKKK"));
        }
    }
}
