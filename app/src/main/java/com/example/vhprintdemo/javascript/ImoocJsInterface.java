package com.example.vhprintdemo.javascript;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 *
 */
public class ImoocJsInterface {

    private static final String TAG ="ImoocJsInterface" ;
    private JsBridge iJsBridge;
    public ImoocJsInterface(JsBridge iJsBridge){
        this.iJsBridge=iJsBridge;
    }

    /**
     * 这个方法 不在 主线程 执行
     * @param value
     */
    @JavascriptInterface
    public void setValue(final String value){
        Log.d(TAG,"value="+value);
        iJsBridge.setTextViewValue(value);
    }
}
