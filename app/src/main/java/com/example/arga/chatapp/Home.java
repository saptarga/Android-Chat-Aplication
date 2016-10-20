package com.example.arga.chatapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    private Button connect;
    private EditText ipAdress,username;
    private Client mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ipAdress=(EditText) findViewById(R.id.editText1);
        username = (EditText) findViewById(R.id.editText2);
        connect=(Button)findViewById(R.id.button1);
        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String ip=ipAdress.getText().toString();
                String user = username.getText().toString();
                if (ip.equals("")){
                    Toast.makeText(getBaseContext(),"Ip Address Can not be blank",Toast.LENGTH_SHORT).show();
                }else if (user.equals("")){
                    Toast.makeText(getBaseContext(),"Username Can not be blank",Toast.LENGTH_SHORT).show();
                }else{
                    Client.SERVERIP=ip;
                    Client.username = user;
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    //Log.e("ServerIP", Client.SERVERIP);
                    startActivity(intent);
                }

            }
        });
    }

}
