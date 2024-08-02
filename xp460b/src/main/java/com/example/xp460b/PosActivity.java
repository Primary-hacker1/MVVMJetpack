package com.example.xp460b;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.BitmapToByteData.AlignType;
import net.posprinter.utils.BitmapToByteData.BmpType;
import net.posprinter.utils.DataForSendToPrinterPos80;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PosActivity extends Activity {
    Button bt1, bt2, bt3, bt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos_activity);
        setupview();
        addlistener();
    }

    private void addlistener() {
        bt1.setOnClickListener(v -> {
            if (MainActivity.isConnect) {
                // TODO Auto-generated method stub
                MainActivity.binder.writeDataByYouself(new UiExecute() {

                    @Override
                    public void onsucess() {

                    }

                    @Override
                    public void onfailed() {

                    }
                }, () -> {
                    List<byte[]> list = new ArrayList<byte[]>();
                    String str = "Welcome to use the impact and thermal printer manufactured by professional POS receipt printer company!";
                    byte[] data1 = strTobytes(str);
                    list.add(data1);
                    list.add(DataForSendToPrinterPos80
                            .printAndFeedLine());
                    return list;
                });
            } else {
                Toast.makeText(getApplicationContext(), "�������Ӵ�ӡ����", Toast.LENGTH_SHORT).show();
            }
        });

        bt2.setOnClickListener(v -> {
            if (MainActivity.isConnect) {
                MainActivity.binder.writeDataByYouself(new UiExecute() {

                    @Override
                    public void onsucess() {

                    }

                    @Override
                    public void onfailed() {

                    }
                }, () -> {
                    List<byte[]> list = new ArrayList<byte[]>();
                    list.add(DataForSendToPrinterPos80
                            .initializePrinter());
                    list.add(DataForSendToPrinterPos80
                            .selectAlignment(1));
                    list.add(DataForSendToPrinterPos80
                            .selectHRICharacterPrintPosition(02));
                    list.add(DataForSendToPrinterPos80
                            .setBarcodeWidth(3));
                    list.add(DataForSendToPrinterPos80
                            .setBarcodeHeight(162));
                    list.add(DataForSendToPrinterPos80.printBarcode(65,
                            11, "01234567890"));
                    list.add(DataForSendToPrinterPos80
                            .printAndFeedLine());
                    return list;
                });
            } else {
                Toast.makeText(getApplicationContext(), "�������Ӵ�ӡ����", Toast.LENGTH_SHORT).show();
            }

        });


        bt3.setOnClickListener(v -> {
            if (MainActivity.isConnect) {
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(getApplicationContext(), "�������Ӵ�ӡ����", Toast.LENGTH_SHORT).show();
            }
        });

        bt4.setOnClickListener(v -> {
            if (MainActivity.isConnect) {
                MainActivity.binder.writeDataByYouself(new UiExecute() {

                    @Override
                    public void onsucess() {

                    }

                    @Override
                    public void onfailed() {

                    }
                }, () -> {
                    ArrayList<byte[]> list = new ArrayList<byte[]>();
                    list.add(DataForSendToPrinterPos80
                            .initializePrinter());
                    list.add(DataForSendToPrinterPos80
                            .SetsTheSizeOfTheQRCodeSymbolModule(3));
                    list.add(DataForSendToPrinterPos80
                            .SetsTheErrorCorrectionLevelForQRCodeSymbol(48));
                    list.add(DataForSendToPrinterPos80
                            .StoresSymbolDataInTheQRCodeSymbolStorageArea("Welcome to Printer Technology to create advantages Quality to win in the future"));
                    list.add(DataForSendToPrinterPos80
                            .PrintsTheQRCodeSymbolDataInTheSymbolStorageArea());
                    list.add(DataForSendToPrinterPos80
                            .printAndFeedLine());
                    return list;
                });
            } else {
                Toast.makeText(getApplicationContext(), "�������Ӵ�ӡ����", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();
            final Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            //�������ݵĴ���ͷ�װ
            MainActivity.binder.writeDataByYouself(new UiExecute() {

                @Override
                public void onsucess() {

                }

                @Override
                public void onfailed() {

                }
            }, () -> {
                ArrayList<byte[]> list = new ArrayList<byte[]>();
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                int x = 0;
                if (w < 576) {//576λ80��ӡ���Ĵ�ӡֽ�Ŀɴ�ӡ���
                    x = (576 - w) / 2;
                }
                int m = x % 256;
                int n = x / 256;
                Log.i("���Դ�ӡλ��", "m=" + m + ",n=" + n);


                list.add(DataForSendToPrinterPos80.printRasterBmp(0, bitmap, BmpType.Threshold, AlignType.Center, 576, 100));
                return list;
            });
        }

    }

    private void setupview() {
        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);

    }

    /**
     * �ַ���תbyte����
     */
    public static byte[] strTobytes(String str) {
        byte[] b = null, data = null;
        try {
            b = str.getBytes(StandardCharsets.UTF_8);
            data = new String(b, StandardCharsets.UTF_8).getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
}
