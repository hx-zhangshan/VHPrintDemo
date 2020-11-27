package com.example.vhprintdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vhprintdemo.dialog.bluetoothselectdialog;

/**
 * @author HP
 */
public class SplashActivity extends AppCompatActivity {
    private Button mEnterBtn, mBtnConnect, mbtnList;
    private bluetoothselectdialog bluetoothselectdialog;
    private Intent intent;

    private View.OnClickListener mOnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.enter_btn:
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
//                case R.id.button_connect://连接蓝牙
//                    bluetoothselectdialog.Show(SplashActivity.this);
//                    LinearLayout ll = bluetoothselectdialog.getly();
//                    ListView listView = (ListView) ll.findViewById(R.id.list_item);
//                    setClick(listView, thread);
//                    break;
                case R.id.button_query:
//                    intent = new Intent(SplashActivity.this, QueryActivity.class);
                    intent = new Intent(SplashActivity.this, ExListActivity.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("address",address);
//
//                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        bluetoothselectdialog = new bluetoothselectdialog();

        mEnterBtn = (Button) findViewById(R.id.enter_btn);
        mEnterBtn.setOnClickListener(mOnClickLister);
        mBtnConnect = (Button) findViewById(R.id.button_connect);
        mBtnConnect.setOnClickListener(mOnClickLister);
        mbtnList=(Button) findViewById(R.id.button_query);
        mbtnList.setOnClickListener(mOnClickLister);

    }


}
