package com.example.vhprintdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vhprintdemo.adapter.EquiPdAdapter;
import com.example.vhprintdemo.bean.EquiCheckPd;
import com.example.vhprintdemo.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

public class ExListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private List<EquiCheckPd> mData = null;
    private List<EquiCheckPd> mDataCopy = null;
    private Context mContext;
    private EquiPdAdapter mAdapter = null;
    private ListView list_animal;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //switch是为了放止多个线程出浑谣，如果只是一个的话可以不写
            switch (msg.what) {
                case 1:
                    //得到请求获得的Gson串
                    String pose=msg.obj.toString();
                    JsonParser parser = new JsonParser();
                    JsonElement je = parser.parse(pose);
                    JsonElement data = je.getAsJsonObject().get("data");
                    Gson gson = new Gson();
                    //解析gson串，存入对象Bean中
                    List<EquiCheckPd> list= gson.fromJson(data.toString(), new TypeToken<List<EquiCheckPd>>() {}.getType());
                    for (EquiCheckPd m:list){
                        mData.add(m);
                        mDataCopy.add(m);
                    }
                    Log.e("str",list.size()+"");
                    //刷新适配器
                    mAdapter.notifyDataSetChanged();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_activity);


        mContext = ExListActivity.this;
        list_animal = (ListView) findViewById(R.id.listViewEqui);
        list_animal.setTextFilterEnabled(true);
        //动态加载顶部View和底部View
//        final LayoutInflater inflater = LayoutInflater.from(this);
//        View headView = inflater.inflate(R.layout.view_header, null, false);
//        View footView = inflater.inflate(R.layout.view_footer, null, false);

        mData = new LinkedList<EquiCheckPd>();
        mDataCopy = new LinkedList<EquiCheckPd>();
        mAdapter = new EquiPdAdapter((LinkedList<EquiCheckPd>)mData, mContext);

        //添加表头和表尾需要写在setAdapter方法调用之前！！！
//        list_animal.addHeaderView(headView);
//        list_animal.addFooterView(footView);
        list_animal.setAdapter(mAdapter);
        list_animal.setOnItemClickListener(this);

        //开启子线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                //请求数据的接口
                String utils = HttpUtil.post("http://192.168.196.25:9999/getAllPdEqui","null");
                Message msg=new Message();
                //拿到gson串
                msg.obj=utils;
                msg.what=1;
                //传入handler
                handler.sendMessage(msg);
            }
        }.start();

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击表头的时候 position 为零
//        if(position==0){
//           return;
//        }
        EquiCheckPd equiCheckPd = (EquiCheckPd)parent.getAdapter().getItem(position);
        //跳转到详情页
        Intent intent = new Intent(ExListActivity.this, EquiDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("checkId",equiCheckPd.getId()+"");
                    intent.putExtras(bundle);
        startActivity(intent);

    }

}
