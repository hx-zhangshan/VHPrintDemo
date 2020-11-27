package com.example.vhprintdemo.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class BluetoothConnectReceiver extends BroadcastReceiver {
    String strPsw = "1234";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            try {
                abortBroadcast();
                Method removeBondMethod = btDevice.getClass().getDeclaredMethod("setPin", new Class[]{byte[].class});
                Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice, new Object[]{strPsw.getBytes()});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING://正在配对
                    Log.d("BlueToothTestActivity", "正在配对......");
                    Toast.makeText(context, "正在配对......", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothDevice.BOND_BONDED://配对结束
                    Log.d("BlueToothTestActivity", "完成配对");
                    //abortBroadcast();
                    Toast.makeText(context, "完成配对", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothDevice.BOND_NONE://取消配对/未配对
                    Log.d("BlueToothTestActivity", "取消配对");
                    Toast.makeText(context, "取消配对", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    }
}
