package com.example.xp460b;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.BitmapToByteData.BmpType;
import net.posprinter.utils.DataForSendToPrinterPos76;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class P76Activity extends Activity {
    private Button bt1, bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p76_activity);
        setupView();
        addListener();
    }

    private void addListener() {
        bt1.setOnClickListener(v -> {
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
                    String str = "Welcome to use the impact and thermal printer manufactured by professional POS receipt printer company!";
                    byte[] data1 = strTobytes(str);
                    list.add(data1);
                    list.add(DataForSendToPrinterPos76
                            .printAndFeedLine());
                    return list;
                });
            } else {
                Toast.makeText(getApplicationContext(), "�������Ӵ�ӡ����", Toast.LENGTH_SHORT).show();
            }
        });
        bt2.setOnClickListener(v -> {
            if (MainActivity.isConnect) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
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
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
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
                list.add(DataForSendToPrinterPos76.initializePrinter());
                list.add(DataForSendToPrinterPos76.selectBmpModel(0, bitmap, BmpType.Dithering));
                return list;
            });
        }

    }

    private void setupView() {
        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
    }

    public static byte[] strTobytes(String str) {
        byte[] b = null, data = null;
        try {
            b = str.getBytes(StandardCharsets.UTF_8);
            data = new String(b, StandardCharsets.UTF_8).getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
