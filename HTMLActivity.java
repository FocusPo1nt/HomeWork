package com.focuspoint.homework;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HTMLActivity extends Activity{
    Handler handler;
    TextView responseCode;

    static final int RESPONSE = 1;
    static final int ERROR = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html);

         responseCode = (TextView) findViewById(R.id.responseCode);


        //Обработчик ответа из потока
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case RESPONSE:
                        responseCode.setText(msg.getData().getString("response"));
                        break;
                    case ERROR:
                        responseCode.setText("Произошла ошибка: "+msg.getData().getString("error"));
                        Toast.makeText(HTMLActivity.this,"что-то пошло не так", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        getHTML(getIntent().getExtras().getString("URL"));


    }

    //Запрос на сайт в отдельном потоке
    public void getHTML(final String intutData) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                Message msg = new Message();
                Bundle data = new Bundle();
                try {
                    URL url = new URL(intutData);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(10000);//10 секунд
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    StringBuilder response = new StringBuilder();
                    String buffer;

                    //чтение из стрима
                    while ((buffer=reader.readLine())!=null){
                        response.append(buffer);
                    }

                    data.putString("response",response.toString());
                    msg.setData(data);
                    msg.what = RESPONSE;
                } catch (IOException e){
                    msg.what = ERROR;
                    data.putString("error",e.toString());
                    msg.setData(data);
                }
                handler.sendMessage(msg);
            }
        });
        thread.start();

    }
}
