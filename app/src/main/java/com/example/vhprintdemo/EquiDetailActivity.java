package com.example.vhprintdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vhprintdemo.adapter.EquiPdDetailAdapter;
import com.example.vhprintdemo.bean.EquiCheckPdDetail;
import com.example.vhprintdemo.bluetooth.BluetoothConnectReceiver;
import com.example.vhprintdemo.dialog.bluetoothselectdialog;
import com.example.vhprintdemo.utils.HttpUtil;
import com.example.vhprintdemo.utils.QRCodeGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author HP
 */
public class EquiDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private List<EquiCheckPdDetail> mData = null;
    private List<EquiCheckPdDetail> mDataCopy = null;
    private Context mContext;
    private EquiPdDetailAdapter mAdapter = null;
    private ListView list_animal;
    private bluetoothselectdialog bluetoothselectdialog;


    private final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    // 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
    private BluetoothDevice selectDevice;
    // 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
    private BluetoothSocket clientSocket;
    // 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
    private OutputStream os;
    String checkId;
    String address="";
    AcceptThread thread ;
    BluetoothConnectReceiver myreceiver;
    int labelCount=1;//总数
    int labelIndex=1;//当前页数
    int W=30;int H=80;

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
                    List<EquiCheckPdDetail> list= gson.fromJson(data.toString(), new TypeToken<List<EquiCheckPdDetail>>() {}.getType());
                    for (EquiCheckPdDetail m:list){
                        mData.add(m);
                        mDataCopy.add(m);
                    }
                    Log.d("str",list.size()+"");
                    //刷新适配器
                    mAdapter.notifyDataSetChanged();

                    break;
            }
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//
//        // 关联检索配置和SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//
//        return true;
//    }
//    private void handleIntent(Intent intent) {
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            //通过某种方法，根据请求检索你的数据
//            ArrayList<EquiCheckPdDetail> arrayList=new ArrayList(mData.size());
//            for (EquiCheckPdDetail m:mData){
//                arrayList.add(m);
//            }
//            mData.clear();
//            for (EquiCheckPdDetail m:arrayList){
//                if(m.getEquiName().indexOf(query)!=-1||m.getEquiArchCode().indexOf(query)!=-1){
//                    mData.add(m);
//                }
//            }
//            mAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        handleIntent(getIntent());
//    }
    public void getEqui(View view){
        switch (((TextView)view).getText().toString()){
            case "全部资产":
                ((TextView) view).setBackgroundColor(Color.rgb(67,148,235));
                findViewById(R.id.not_equi).setBackgroundColor(Color.rgb(67,187,235));
                mData.clear();
                for (EquiCheckPdDetail m:mDataCopy){
                    mData.add(m);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case "未盘点":
                ((TextView) view).setBackgroundColor(Color.rgb(67,148,235));
                findViewById(R.id.all_equi).setBackgroundColor(Color.rgb(67,187,235));
                mData.clear();
                for (EquiCheckPdDetail m:mDataCopy){
                    if(m.getCheckState()!=0){
                        mData.add(m);
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equidetail_activity);

        //获取 参数
        Bundle bd=this.getIntent().getExtras();
        checkId = bd.getString("checkId");
        bluetoothselectdialog = new bluetoothselectdialog();
        thread = new EquiDetailActivity.AcceptThread();
        myreceiver=new BluetoothConnectReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(myreceiver, filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Android M Permission check
            //android 6.0 以上 需要授权位置服务 否则搜索不到蓝牙2.0
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }


        mContext = EquiDetailActivity.this;
        list_animal = (ListView) findViewById(R.id.listViewEquiDetail);
        list_animal.setTextFilterEnabled(true);

        mData = new LinkedList<EquiCheckPdDetail>();
        mDataCopy = new LinkedList<EquiCheckPdDetail>();
        mAdapter = new EquiPdDetailAdapter((LinkedList<EquiCheckPdDetail>)mData, mContext);

        //添加表头和表尾需要写在setAdapter方法调用之前！！！
        list_animal.setAdapter(mAdapter);
        list_animal.setOnItemClickListener(this);

        //开启子线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                //请求数据的接口
                String utils = HttpUtil.post("http://192.168.196.25:9999/getAllPdEquiDetail","checkId="+checkId);
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
//            return;
//        }

        EquiCheckPdDetail checkPdDetail = (EquiCheckPdDetail)parent.getAdapter().getItem(position);
        new AlertDialog.Builder(this).setTitle("是否打印此资产？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("打印", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String ackp=checkPdDetail.getEquiArchCode();
                        createQRcode(checkPdDetail);
                        //这里是执行的方法
                        //Toast.makeText(mContext,"你点击了第" + position + "项"+animal.getDeptName(),Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("不打印", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
//                            finish();
                    }
                }).show();

    }
    private Bitmap createText(){
        String txt="a  b c d e f";
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(20);
        Bitmap txtBitmap = Bitmap.createBitmap((int)textPaint.measureText(txt),200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(txtBitmap);
        canvas.drawBitmap(txtBitmap, 0, 0, null);
        StaticLayout sl= new StaticLayout(txt, textPaint, txtBitmap.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        sl.draw(canvas);
        return txtBitmap;
    }
    public void print(EquiCheckPdDetail checkPdDetail) {
        if (selectDevice == null&&address!="") {
            //通过地址获取到该设备
            selectDevice= BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        }

        try {
            // 判断客户端接口是否为空
            if (clientSocket == null&&selectDevice!=null) {
                // 获取到客户端接口
                clientSocket = selectDevice.createRfcommSocketToServiceRecord(PRINTER_UUID);
                // 向服务端发送连接
                clientSocket.connect();
            }

            if(clientSocket!=null&&!clientSocket.isConnected()){
                clientSocket.connect();
            }

            if(os==null&&clientSocket!=null){
                // 获取到输出流，向外写数据
                os = clientSocket.getOutputStream();

            }

            if(!thread.isAlive()){
                // 线程开始
                thread.start();
            }
            Bitmap test = BitmapFactory.decodeResource(getResources(), R.drawable.test);
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo_hnzl);
            //生成二维码 数据  并且进行 排版
            Bitmap bmpee= QRCodeGenerator.createQRCodeBitmap(checkPdDetail.getEquiArchCode(), 160, 160,
                    "UTF-8", "H", "1",
                    Color.BLACK, Color.WHITE);//createText()
            Bitmap bmp=QRCodeGenerator.toConformBitmap(test,bmpee,logo,checkPdDetail);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ArrayList<byte[]> rowDataList=new ArrayList<byte[]>();
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
            List<byte[]> D1 = bitmaptobyte(bmp, W, H);
            int length = D1.size();
            int spaceStart = 0;
            int sIndex = 0;
            boolean isSpace = true;
            int spaceCount = 0;
            int spaceEnd = 0;
            int nL, nH;
            int sL, sH;
            Bitmap bitmap203 = Bitmap.createBitmap(W*8, H *8, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap203);
            canvas.drawBitmap(bmp, new Rect(0, 0, bmp.getWidth(), bmp.getHeight()), new Rect(0, 0, W*8, H*8), new Paint());
            bmp=bitmap203;
            int fw=bmp.getWidth()/8;
            int bh=bmp.getHeight();

            for (int i = 0; i < length; i++) {
                byte[] rowData = D1.get(i);
                if (rowData.length == 2 && rowData[0] == 0x15) {
                    if (isSpace) {
                        ++spaceStart;
                        sIndex = i;
                    } else {
                        ++spaceCount;
                    }
                } else {
                    while (spaceCount > 0) {
                        baos.write(new byte[]{ 0x15, 0x01});
                        rowDataList.add(new byte[]{ 0x15, 0x01});
                        --spaceCount;
                    }
                    isSpace = false;
                    baos.write(rowData);
                    rowDataList.add(rowData);
                }
            }

            for (int i = length - 1; i > sIndex; i--) {
                byte[] rowData = D1.get(i);
                if (rowData.length == 2 && rowData[0] == 0x15) {
                    spaceEnd++;
                } else {
                    break;
                }
            }

            int usePrint = length - spaceStart - spaceEnd;
            nL = usePrint % 256; // 有效打印区域
            nH = usePrint / 256;
            sL = spaceStart % 256; // 头部空白区域
            sH = spaceStart / 256;

            // 计算baos的总字节数
            int D11 = 0, D2 = 0, D3 = 0, D4 = 0;
            byte[] data = baos.toByteArray();
            int dataCount = data.length;
            D11 = dataCount & 0xff;
            D2 = (dataCount >> 8) & 0xff;
            D3 = (dataCount >> 16) & 0xff;
            D4 = (dataCount >> 24) & 0xff;

            for(;labelIndex<=labelCount;labelIndex++) {
                if (os != null) {
                    os.write(new byte[]{0x17, (byte) nL, (byte) nH, (byte) sL, (byte) sH,
                            (byte) fw, (byte) 0, (byte) labelCount, (byte) labelIndex, (byte) D11,
                            (byte) D2, (byte) D3, (byte) D4});

                    ArrayList<byte[]> sendData = new ArrayList<byte[]>();
                    int packSize = 512;
                    int send = 0;
                    byte[] cmd = new byte[0];
                    for (byte[] d : rowDataList) {
                        if (send + d.length > packSize) {
                            sendData.add(cmd);
                            send = 0;
                            cmd = new byte[0];
                        }
                        send += d.length;
                        byte[] byte_3 = new byte[cmd.length + d.length];
                        System.arraycopy(cmd, 0, byte_3, 0, cmd.length);
                        System.arraycopy(d, 0, byte_3, cmd.length, d.length);
                        cmd = byte_3;
                    }
                    if (cmd.length > 0) {
                        sendData.add(cmd);
                    }

                    for (byte[] d : sendData) {
                        os.write(d);
                    }

                    //发送结尾0x0C
                    os.write(new byte[]{0x0C});

                }
                Thread.sleep(2000);
            }

            labelCount=1;
            labelIndex=1;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void createQRcode(EquiCheckPdDetail checkPdDetail) {
        if(address==""){
            bluetoothselectdialog.Show(EquiDetailActivity.this);
            LinearLayout ll = bluetoothselectdialog.getly();
            ListView listView = (ListView) ll.findViewById(R.id.list_item);
            setClick(listView, thread);
        }
        //打印数据 todo
        print(checkPdDetail);


    }
    public void setClick(ListView listView, final Thread th) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                @SuppressWarnings("unchecked")
                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                // 获取
                address = map.get("address");
                String name = map.get("title");

                bluetoothselectdialog.getbluetooth().cancelDiscovery();
                // 关闭窗口
                bluetoothselectdialog.Hide();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                        //PrinterInstance.getPrinterInstance(bluetoothDevice, null, 0, null);
                        //PrinterInstance.mPrinter.openConnection();
                        selectDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                        if (clientSocket == null && selectDevice != null) {
                            // 获取到客户端接口
                            try {
                                clientSocket = selectDevice.createRfcommSocketToServiceRecord(PRINTER_UUID);
                                // 向服务端发送连接
                                clientSocket.connect();
                                th.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                                clientSocket = null;
                            }
                        }
                    }
                });
                thread.start();
            }
        });
    }


    // 服务端接收信息线程
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;// 服务端接口
        //private BluetoothSocket socket;// 获取到客户端的接口
        private InputStream is;// 获取到输入流
        private OutputStream os;// 获取到输出流

        public AcceptThread() {
        }

        @Override
        public void run() {
            try {
                if(clientSocket!=null) {
                    // 获取到输入流
                    is = clientSocket.getInputStream();
                    // 获取到输出流
                    os = clientSocket.getOutputStream();
                }
                // 无线循环来接收数据
                while (true) {
                    if (is != null ){
                        int datasize=is.available();
                        if( datasize > 0) {
                            byte[] buffer = new byte[is.available()];
                            is.read(buffer);
                            String data = "";
                            for (int i = 0; i < buffer.length; i++) {
                                data = data + String.valueOf(buffer[i]) + "   ";
                            }

                        }

                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }

    private static long msgtime=0;
    //T20 打印错误数据处理
    public Handler t20Handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] buffer = bundle.getByteArray("result");
            if(buffer[0] == 1||msgtime==0){
                long a=System.currentTimeMillis()-msgtime;
                if(msgtime!=0&&a>10000){
                    msgtime=0;
                    Toast.makeText(EquiDetailActivity.this, String.valueOf(a),Toast.LENGTH_SHORT).show();
                }else {
                    Message time = new Message();
                    Bundle data = new Bundle();
                    data.putByteArray("result", new byte[]{1});
                    time.setData(data);
                    Toast.makeText(EquiDetailActivity.this, String.valueOf(a)+"send",Toast.LENGTH_SHORT).show();
                    t20Handler.sendMessageDelayed(time,10000);
                }
            }
            if (buffer.length >= 4) {
                msgtime = System.currentTimeMillis();
                String data="";
                for (int i=0;i<buffer.length;i++){
                    data=data+"    "+String.valueOf(buffer[i]);
                }
                Toast.makeText(EquiDetailActivity.this, String.valueOf(msgtime)+"data:"+data,Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (clientSocket != null&&clientSocket.isConnected()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clientSocket=null;
        unregisterReceiver(myreceiver);
        super.onDestroy();
    }

    private List<byte[]> bitmaptobyte(Bitmap bmp,int W,int H){
        Bitmap bitmap203 = Bitmap.createBitmap(W*8, H*8, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap203);
        canvas.drawBitmap(bmp, new Rect(0, 0, bmp.getWidth(), bmp.getHeight()), new Rect(0, 0, W*8, H*8), new Paint());
        bmp=bitmap203;
        int Width=bmp.getWidth();
        int Height=bmp.getHeight();
        int useRowWith = (Width >= 48 * 8) ? 48 * 8 :Width;//有效宽度（不超出最大宽度）

        List<byte[]> D1=new ArrayList<byte[]>();

        byte rowData[] = new byte[useRowWith / 8];
        for(int i=0;i<Height;i++) {
            pixelToByteArray(useRowWith, bmp, i, rowData);

            // =========================================判断是否全空行=================================
            int rowOffset = 0;
            int tail = rowData.length - 1;
            for (; tail >= 0; tail--) {
                if (rowData[tail] != 0) {
                    break;
                }
            }
            if (tail < 0) {
                D1.add(new byte[]{0x15, 0x01});// 空行指令
            } else {
                int index = 0; // 16 n n个数据 [0,tail] tail+1 数据
                byte[] result = new byte[1 + 1 + rowOffset + tail + 1]; // 长度: [16] +
                // [n] + [n个数据]
                result[index++] = 0x16;
                result[index++] = (byte) (rowOffset + tail + 1);
                for (int j = 0; j < rowOffset; j++) {
                    result[index++] = 0x00; // 无效指令
                }
                for (int j = 0; j <= tail; j++) {
                    result[index++] = rowData[j];
                }
                D1.add(result);
            }
        }
        return D1;
    }



    private static void pixelToByteArray(int useRowWith, Bitmap bitmap,
                                         int row, byte[] rowData) {
        // 从图片矩阵转换为byte数据
        int gray, sum = 0, index = 0;
        for (int w = 0; w < useRowWith; w++) {
            int pixels = bitmap.getPixel(w, row);
            gray = toGray(Color.red(pixels), Color.green(pixels),
                    Color.blue(pixels));
            if (gray <= 172) {
                sum |= 1 << (7 - w % 8);
            }
            if ((w + 1) % 8 == 0) {
                rowData[index++] = (byte) sum;
                sum = 0;
            }
        }
    }

    public static int toGray(int r, int g, int b) {
        int sum = r * 19661 + g * 38666 + b * 7209;
        return sum >> 16 & 0xFF;
    }

}

