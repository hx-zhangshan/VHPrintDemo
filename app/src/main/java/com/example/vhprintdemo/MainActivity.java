package com.example.vhprintdemo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vhprintdemo.javascript.ImoocJsInterface;
import com.example.vhprintdemo.javascript.JsBridge;

public class MainActivity extends AppCompatActivity implements JsBridge {

    private WebView mWebView;
    private TextView mTextView;
    private Handler mHandler;
    private Button mButton;
    private EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets(savedInstanceState);
//        Log.d("MainActivity","onCreat");

    }

    private void initWidgets(Bundle savedInstanceState) {
        mWebView = findViewById(R.id.webView);
        mTextView=findViewById(R.id.tv_result);
        mEditText=findViewById(R.id.edittext);
        mButton=findViewById(R.id.button);
        mHandler=new Handler();
        //允许 webView中加载js代码
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 添加js接口  给webView
        mWebView.addJavascriptInterface(new ImoocJsInterface(this),"imoocLauncher");

        // 调用 html的代码  路径
        mWebView.loadUrl("file:///android_asset/index.html");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=mEditText.getText().toString();
                //传输给js代码
                mWebView.loadUrl("javascript:if(window.remote){remote('"+str+"')}");
            }
        });
        WebView.setWebContentsDebuggingEnabled(true);
    }

    @Override
    public void setTextViewValue(final String value) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(value);
            }
        });
    }

}
