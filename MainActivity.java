package com.focuspoint.homework;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isOnline()){
            Toast.makeText(MainActivity.this,"Необходимо подключение к интернету",Toast.LENGTH_SHORT).show();
            return;
        }

        //Обработка ввода URL
        final EditText editURL = (EditText) findViewById(R.id.editText);
        editURL.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Intent intent = new Intent(MainActivity.this, HTMLActivity.class);
                    intent.putExtra("URL", checkURL(editURL.getText().toString()));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }


    //Проверка подключения к интернету
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    //Проверка на наличие http и https
    public String checkURL(String url){
        if (url.startsWith("http")){
            return url;
        }else{
            return "http://"+url;
        }
    }

}


