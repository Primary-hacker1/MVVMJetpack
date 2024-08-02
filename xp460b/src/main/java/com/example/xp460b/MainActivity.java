package com.example.xp460b;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.BitmapToByteData.BmpType;
import net.posprinter.utils.DataForSendToPrinterTSC;
import net.posprinter.utils.PosPrinterDev;
import net.posprinter.utils.ReadExcel;
import net.posprinter.utils.RoundQueue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class MainActivity extends Activity {

    public static IMyBinder binder;//IMyBinder接口，所有可供调用的连接和发送数据的方法都封装在这个接口内
    //bindService的参数conn
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定成功
            binder = (IMyBinder) service;
        }
    };
    public static boolean isConnect = false;//用来标识连接状态的一个boolean值
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn_scan, btn_sb, bt8, btn9, btnExcel, btnCheck;
    RelativeLayout main;
    Spinner spinner;
    EditText et;
    int pos;
    BluetoothAdapter blueAdapter;
    private ArrayAdapter<String> adapter1, adapter2, adapter3;
    private ListView lv1, lv2, lv_usb;
    private LinearLayout ll1;
    private final ArrayList<String> deviceList_bonded = new ArrayList<String>();
    private final ArrayList<String> deviceList_found = new ArrayList<String>();
    AlertDialog dialog;
    public String mac = "", usbDev = "";
    TextView tv_usb;
    private List<String> usbList, usblist;
    private final DeviceReceiver myDevice = new DeviceReceiver();

    //	private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定service，获取ImyBinder对象
        Intent intent = new Intent(this, PosprinterService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        // 注册蓝牙广播接收者
        IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(myDevice, filterStart);
        registerReceiver(myDevice, filterEnd);
        //初始化控件
        btn0 = (Button) findViewById(R.id.button0);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);
        btn_sb = (Button) findViewById(R.id.button7);
        spinner = (Spinner) findViewById(R.id.spinner1);
        et = (EditText) findViewById(R.id.editText1);
        et.setText("192.168.1.138");
        bt8 = (Button) findViewById(R.id.button8);
        btn9 = (Button) findViewById(R.id.button9);
        btnExcel = (Button) findViewById(R.id.btn_excel);
        btnCheck = (Button) findViewById(R.id.btn_check);

        main = (RelativeLayout) findViewById(R.id.main);

        //给控件添加监听事件
        addListener();
    }

    @SuppressLint({"SetTextI18n", "SdCardPath"})
    private void addListener() {
        btn9.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, P76Activity.class)));
        bt8.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PosActivity.class)));
        btn_sb.setOnClickListener(v -> {
            switch (pos) {
                case 1:
                    connectBLE();
                    break;
                case 2:
                    setUsb();
                    break;
                default:
                    break;
            }
        });
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                pos = arg2;
                switch (arg2) {
                    case 0:
                        et.setText("");
                        et.setHint(getString(R.string.hint));
                        et.setEnabled(true);
                        btn_sb.setVisibility(View.GONE);
                        break;
                    case 1:
                        et.setText("");
                        et.setHint(getString(R.string.bleselect));
                        et.setEnabled(false);
                        btn_sb.setVisibility(View.VISIBLE);
                        //connectBLE();
                        break;
                    case 2:
                        et.setText("");
                        et.setHint(getString(R.string.usbselect));
                        et.setEnabled(false);
                        btn_sb.setVisibility(View.VISIBLE);
                        //setUsb();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //新增的打印图片示例代码
        btn6.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 0);
        });
        //点击连接按钮，连接打印机
        btn0.setOnClickListener(v -> {
            switch (pos) {
                case 0:
                    connetNet();
                    break;

                case 1:
                    //connectBLE();
                    sendble();
                    break;
                case 2:
                    connectUSB();
                    break;
                default:
                    break;
            }

        });
        //断开按钮btn1的监听事件
        btn1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (isConnect) {//如果是连接状态才执行断开操作
                binder.disconnectCurrentPort(new UiExecute() {

                    @Override
                    public void onsucess() {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_discon_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onfailed() {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_discon_faile), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_present_con), Toast.LENGTH_SHORT).show();
            }
        });
        //打印文本,直线，条码内容按钮btn2监听事件
        btn2.setOnClickListener(v -> {
            if (isConnect) {
                //向打印机发生打印指令和打印数据，调用此方法
                //第一个参数，还是UiExecute接口的实现，分别是发生数据成功和失败后在ui线程的处理
                //第二个参数是ProcessData接口的实现
				//这个接口的重写processDataBeforeSend这个处理你要发送的指令
                binder.writeDataByYouself(new UiExecute() {

                    @Override
                    public void onsucess() {
                        Toast.makeText(getApplicationContext(), getString(R.string.send_success), Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onfailed() {
                        Toast.makeText(getApplicationContext(), getString(R.string.send_failed), Toast.LENGTH_SHORT)
                                .show();
                    }
                }, () -> {
                    // TODO Auto-generated method stub
                    //初始化一个list
                    ArrayList<byte[]> list = new ArrayList<byte[]>();
                    //在打印请可以先设置打印内容的字符编码类型，默认为gbk，请选择打印机可识别的类型，参看编程手册，打印代码页
                    DataForSendToPrinterTSC.setCharsetName("gbk");//不设置，默认为gbk
                    //通过工具类得到一个指令的byte[]数据,以文本为例
                    //首先得设置size标签尺寸,宽60mm,高30mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册
                    byte[] data0 = DataForSendToPrinterTSC
                            .sizeBymm(80, 40);
                    list.add(data0);
                    //设置Gap,同上
                    list.add(DataForSendToPrinterTSC.gapBymm(2, 0));

                    //清除缓存
                    list.add(DataForSendToPrinterTSC.cls());
                    //条码指令，参数：int x，x方向打印起始点；int y，y方向打印起始点；
                    //string font，字体类型；int rotation，旋转角度；
                    //int x_multiplication，字体x方向放大倍数
                    //int y_multiplication,y方向放大倍数
                    //string content，打印内容
                    byte[] data1 = DataForSendToPrinterTSC
                            .text(10, 10, "4", 0, 1, 1,
                                    "123abc");
                    list.add(data1);
//									//打印直线,int x;int y;int width,线的宽度，int height,线的高度
                    list.add(DataForSendToPrinterTSC.bar(20,
                            40, 200, 3));
//									//打印条码
                    list.add(DataForSendToPrinterTSC.barCode(
                            60, 50, "128", 100, 1, 0, 2, 2,
                            "abcdef12345"));
                    //打印二维码
                    list.add(DataForSendToPrinterTSC.qrCode(20, 180, "L", 4, "A", 0, "M2", "S7", "abcdef"));
                    //打印
                    list.add(DataForSendToPrinterTSC.print(1));
                    return list;
                });
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.not_con_printer), Toast.LENGTH_SHORT).show();
            }
        });
        //单独打印文本
        btn3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //此处用binder里的另外一个发生数据的方法,同样，也要按照编程手册上的示例一样，先设置标签大小
                //如果数据处理较为复杂，请勿选择此方法
                //上面的发送方法的数据处理是在工作线程中完成的，不会阻塞UI线程
                byte[] data0 = DataForSendToPrinterTSC.sizeBydot(640, 320);
                byte[] data4 = DataForSendToPrinterTSC.gapBydot(16, 0);
                byte[] data1 = DataForSendToPrinterTSC.cls();

                byte[] data2 = DataForSendToPrinterTSC.text(10, 10, "5", 0, 2, 2, "12345678");
                byte[] data3 = DataForSendToPrinterTSC.print(1);
                byte[] data = byteMerger(byteMerger(byteMerger(byteMerger(data0, data4), data1), data2), data3);
                if (isConnect) {
                    binder.write(data, new UiExecute() {

                        @Override
                        public void onsucess() {
                            Toast.makeText(getApplicationContext(), getString(R.string.send_success), Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onfailed() {
                            Toast.makeText(getApplicationContext(), getString(R.string.send_failed), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.not_con_printer), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //打印条码
        btn4.setOnClickListener(v -> {
            if (isConnect) {
                binder.writeDataByYouself(new UiExecute() {

                    @Override
                    public void onsucess() {

                    }

                    @Override
                    public void onfailed() {

                    }
                }, new ProcessData() {

                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        //初始化一个list
                        ArrayList<byte[]> list = new ArrayList<byte[]>();
                        //通过工具类得到一个指令的byte[]数据,以文本为例
                        //首先得设置size标签尺寸,宽60mm,高30mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册
                        byte[] data0 = DataForSendToPrinterTSC.sizeBymm(80,
                                40);
                        list.add(data0);
                        //设置Gap,同上
                        list.add(DataForSendToPrinterTSC.gapBymm(2, 0));
                        //清除缓存
                        list.add(DataForSendToPrinterTSC.cls());

                        //打印条码
                        list.add(DataForSendToPrinterTSC.barCode(60, 50,
                                "128", 100, 1, 0, 2, 2, "abcdef12345"));
                        //打印二维码
//							list.add(DataForSendToPrinterTSC.qrCode(200,100,"L", 4, "M", 0, "M2", "S7", "abcdef12345"));

                        //打印
                        list.add(DataForSendToPrinterTSC.print(1));
                        return list;
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.not_con_printer), Toast.LENGTH_SHORT).show();
            }
        });
        //读取打印机发送到缓存的环形队列里的数据，前提是，你已经开启了读取打印机数据的方法，btn0里的
        //binder.acceptdatafromprinter方法
        btn5.setOnClickListener(v -> {
            RoundQueue<byte[]> que = binder.readBuffer();
            byte[] data = que.getLast();
            Log.i("TAG", "data=" + data);
            if (data != null) {

                btn5.setText(Arrays.toString(data));
            } else {
                btn5.setText("null");
            }
        });

        //读取Excel数据
        btnExcel.setOnClickListener(v -> {
            //输入值为xls文件路径，和sheet号
            String key = "编号";
            int num = 9;
            HashMap<String, String[]> map;
            try {
                map = ReadExcel.readExcel(MainActivity.this, "/sdcard/TBXprinter/file/test2.xls", 0);
                String str = Objects.requireNonNull(map.get(key))[num - 1];
                Toast.makeText(MainActivity.this, key + "中的第" + num + "个数据为：" + str, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //检纸
        btnCheck.setOnClickListener(v -> {
            try {
                binder.write(DataForSendToPrinterTSC.check(), new UiExecute() {
                    @Override
                    public void onsucess() {
                    }

                    @Override
                    public void onfailed() {
                    }
                });
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        });
    }

    protected void setUsb() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView3 = inflater.inflate(R.layout.usb_link, null);
        tv_usb = (TextView) dialogView3.findViewById(R.id.textView1);
        lv_usb = (ListView) dialogView3.findViewById(R.id.listView1);


        usbList = PosPrinterDev.GetUsbPathNames(this);
        usblist = usbList;
        if (usbList == null) {
            usbList = new ArrayList<>();
        }
        tv_usb.setText(getString(R.string.usb_pre_con) + usbList.size());
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usbList);
        lv_usb.setAdapter(adapter3);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView3)
                .create();
        dialog.show();
        set_lv_usb_listener(dialog);

    }

    private void set_lv_usb_listener(final AlertDialog dialog) {
        lv_usb.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            usbDev = usbList.get(arg2);
            et.setText(usbDev);
            binder.connectUsbPort(getApplicationContext(), usbDev, new UiExecute() {

                @Override
                public void onsucess() {

                }

                @Override
                public void onfailed() {

                }
            });
            dialog.cancel();
            Log.i("TAG", usbDev);
        });
    }

    protected void connectUSB() {
        binder.connectUsbPort(getApplicationContext(), et.getText().toString(), new UiExecute() {

            @Override
            public void onsucess() {
                //连接成功后在UI线程中的执行
                isConnect = true;
                Toast.makeText(getApplicationContext(), getString(R.string.con_success), Toast.LENGTH_SHORT).show();
                btn0.setText(getString(R.string.con_success));
                //此处也可以开启读取打印机的数据
                //参数同样是一个实现的UiExecute接口对象
                //如果读的过程重出现异常，可以判断连接也发生异常，已经断开
                //这个读取的方法中，会一直在一条子线程中执行读取打印机发生的数据，
                //直到连接断开或异常才结束，并执行onfailed
				/*binder.acceptdatafromprinter(new UiExecute() {
					
					@Override
					public void onsucess() {

					}
					
					@Override
					public void onfailed() {
						isConnect=false;
						Toast.makeText(getApplicationContext(), getString(R.string.con_has_discon), 0).show();
					}
				});*/
            }

            @Override
            public void onfailed() {
                //连接失败后在UI线程中的执行
                isConnect = false;
                Toast.makeText(getApplicationContext(), getString(R.string.con_failed), Toast.LENGTH_SHORT).show();
                btn0.setText(getString(R.string.con_failed));
            }
        });
    }

    protected void connectBLE() {
        setbluetooth();
    }

    public void sendble() {
        binder.connectBtPort(et.getText().toString(), new UiExecute() {

            @Override
            public void onsucess() {
                //连接成功后在UI线程中的执行
                isConnect = true;
                Toast.makeText(getApplicationContext(), getString(R.string.con_success), Toast.LENGTH_SHORT).show();
                btn0.setText(getString(R.string.con_success));
                //此处也可以开启读取打印机的数据
                //参数同样是一个实现的UiExecute接口对象
                //如果读的过程重出现异常，可以判断连接也发生异常，已经断开
                //这个读取的方法中，会一直在一条子线程中执行读取打印机发生的数据，
                //直到连接断开或异常才结束，并执行onfailed
                binder.acceptDataFromPrinter(new UiExecute() {

                    @Override
                    public void onsucess() {

                    }

                    @Override
                    public void onfailed() {
                        isConnect = false;
                        Toast.makeText(getApplicationContext(), getString(R.string.con_has_discon), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {
                //连接失败后在UI线程中的执行
                isConnect = false;
                Toast.makeText(getApplicationContext(), getString(R.string.con_failed), Toast.LENGTH_SHORT).show();
                //btn0.setText("连接失败");
            }
        });
    }

    protected void setbluetooth() {
        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        //确认开启蓝牙
        if (!blueAdapter.isEnabled()) {
            //请求用户开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, Conts.ENABLE_BLUETOOTH);

        } else {
            //蓝牙已开启
            showblueboothlist();
        }

    }

    @SuppressLint("MissingPermission")
    private void showblueboothlist() {
        if (!blueAdapter.isDiscovering()) {
            blueAdapter.startDiscovery();
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.printer_list, null);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1 = (ListView) dialogView.findViewById(R.id.listView1);
        btn_scan = (Button) dialogView.findViewById(R.id.btn_scan);
        ll1 = (LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2 = (ListView) dialogView.findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog = new AlertDialog.Builder(this).setTitle("BLE").setView(dialogView).create();
        dialog.show();
        setlistener();
        findAvalibleDevice();
    }

    protected void connetNet() {
        //示例：连接打印机网口，参数为：（string）ip地址，（int）端口号，和一个实现的UiExecute接口对象
        //这个接口的实现在连接过程结束后执行（执行于UI线程），onsucess里执行连接成功的代码，onfailed反之；
        binder.connectNetPort(et.getText().toString(), 9100, new UiExecute() {

            @Override
            public void onsucess() {
                //连接成功后在UI线程中的执行
                isConnect = true;
                Toast.makeText(getApplicationContext(), getString(R.string.con_success), Toast.LENGTH_SHORT).show();
                btn0.setText(getString(R.string.con_success));
                //此处也可以开启读取打印机的数据
                //参数同样是一个实现的UiExecute接口对象
                //如果读的过程重出现异常，可以判断连接也发生异常，已经断开
                //这个读取的方法中，会一直在一条子线程中执行读取打印机发生的数据，
                //直到连接断开或异常才结束，并执行onfailed
                binder.acceptDataFromPrinter(new UiExecute() {

                    @Override
                    public void onsucess() {

                    }

                    @Override
                    public void onfailed() {
                        isConnect = false;
                        Toast.makeText(getApplicationContext(), getString(R.string.con_has_discon), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {
                //连接失败后在UI线程中的执行
                isConnect = false;
                Toast.makeText(getApplicationContext(), getString(R.string.con_failed), Toast.LENGTH_SHORT).show();
                btn0.setText(getString(R.string.con_failed));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            //通过去图库选择图片，然后得到返回的bitmap对象
            Uri selectedImage = data.getData();
            String picturePath = PictureHelper.getPath(getApplicationContext(),
                    selectedImage);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            final Bitmap bitmap;
            bitmap = BitmapFactory.decodeFile(picturePath, options);
            if (bitmap == null) {
                Log.e("TEST BITMAP", "NULL  NULL  NULL  NULL");
            }


            //发送数据的处理和封装
            binder.writeDataByYouself(new UiExecute() {

                @Override
                public void onsucess() {

                }

                @Override
                public void onfailed() {

                }
            }, () -> {
                int paperWidth = 80;
                int paperHeight = 40;
                ArrayList<byte[]> list = new ArrayList<byte[]>();
                byte[] data0 = DataForSendToPrinterTSC.sizeBymm(paperWidth, paperHeight);
                list.add(data0);
                // 设置Gap,同上
                list.add(DataForSendToPrinterTSC.gapBymm(2, 0));
                // 清除缓存
                list.add(DataForSendToPrinterTSC.cls());

                list.add(DataForSendToPrinterTSC.bitmap(0, 0, 0, bitmap, BmpType.Dithering, (int) (paperWidth * 8),
                        (int) (paperHeight * 8)));
                Log.e("TEST BITMAP", bitmap.getHeight() + ":::::" + bitmap.getWidth());
                list.add(DataForSendToPrinterTSC.print(1));
                createFileWithByte(list);
                return list;
            });
        }
        if (requestCode == Conts.ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            showblueboothlist();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binder.disconnectCurrentPort(new UiExecute() {

            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        });
        unbindService(conn);
    }

    private void setlistener() {
        btn_scan.setOnClickListener(v -> {
            ll1.setVisibility(View.VISIBLE);
        });
        //已配对的设备的点击连接
        lv1.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            try {
                if (blueAdapter != null && blueAdapter.isDiscovering()) {
                    blueAdapter.cancelDiscovery();

                }

                String msg = deviceList_bonded.get(arg2);
                mac = msg.substring(msg.length() - 17);
                String name = msg.substring(0, msg.length() - 18);
                //lv1.setSelection(arg2);
                dialog.cancel();
                et.setText(mac);
                //Log.i("TAG", "mac="+mac);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //未配对的设备，点击，配对，再连接
        lv2.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            try {
                if (blueAdapter != null && blueAdapter.isDiscovering()) {
                    blueAdapter.cancelDiscovery();

                }
                String msg = deviceList_found.get(arg2);
                mac = msg.substring(msg.length() - 17);
                String name = msg.substring(0, msg.length() - 18);
                //lv2.setSelection(arg2);
                dialog.cancel();
                et.setText(mac);
                Log.i("TAG", "mac=" + mac);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void findAvalibleDevice() {
        //获取可配对蓝牙设备
        Set<BluetoothDevice> device = blueAdapter.getBondedDevices();

        deviceList_bonded.clear();
        if (blueAdapter != null && blueAdapter.isDiscovering()) {
            adapter1.notifyDataSetChanged();
        }
        if (device.size() > 0) {
            //存在已经配对过的蓝牙设备
            for (BluetoothDevice btd : device) {
                deviceList_bonded.add(btd.getName() + '\n' + btd.getAddress());
                adapter1.notifyDataSetChanged();
            }
        } else {  //不存在已经配对过的蓝牙设备
            deviceList_bonded.add("No can be matched to use bluetooth");
            adapter1.notifyDataSetChanged();
        }

    }

    /**
     * byte数组拼接
     */
    private byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * 蓝牙搜索状态广播监听
     */
    private class DeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {    //搜索到新设备
                BluetoothDevice btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索没有配过对的蓝牙设备
                if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!deviceList_found.contains(btd.getName() + '\n' + btd.getAddress())) {

                        deviceList_found.add(btd.getName() + '\n' + btd.getAddress());
                        try {
                            adapter2.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {   //搜索结束

                if (lv2.getCount() == 0) {
                    deviceList_found.add("No can be matched to use bluetooth");
                    try {
                        adapter2.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * 根据byte数组生成文件
     *
     * @param bytes 生成文件用到的byte数组
     */
    private static void createFileWithByte(List<byte[]> bytes) {
        int length = 0;
        @SuppressLint("SdCardPath") File file = new File("/sdcard",
                "test");
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            for (int i = 0; i < bytes.size(); i++) {
                bufferedOutputStream.write(bytes.get(i));
                length = length + bytes.get(i).length;
            }
            Log.d("MSG", "LENGYH OF IMAGE DATA   " + length);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }


}
