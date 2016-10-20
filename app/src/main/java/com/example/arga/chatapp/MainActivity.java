package com.example.arga.chatapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mList;
    private ArrayList<String> arrayList;
    private MyCustomAdapter mAdapter;
    private Client mClient;
    public Boolean login;
    public Boolean newnick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = false;
        newnick = false;
        arrayList = new ArrayList<String>();

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button)findViewById(R.id.send_button);

        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);

        new connectTask().execute("");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString();
                if ((!login) && (!newnick)){
                    if (mClient != null) {
                        mClient.sendMessage("Login: "+Client.username.replace(" ","<"));
                    }
                }else if((!login) && (newnick)){
                    if (mClient != null) {
                        Client.username = message;
                        mClient.sendMessage("Login: "+message);
                    }
                }
                if (login){
                    if (message.startsWith("@")){
                        int i = message.indexOf(" ");
                        String user = message.substring(1,i);
                        Log.e("User reciv",user);
                        String msg = message.substring(i+1,message.length());
                        if (mClient != null) {
                            mClient.sendMessage("PrivatePost " + msg.replace(" ", "<") + "," + user);
                        }
                    }else if(message.startsWith("Info")){
                        if (mClient != null) {
                            mClient.sendMessage("Info @Info "+message.substring(5)+","+Client.username);
                        }
                    } else {
                        if (mClient != null) {
                            mClient.sendMessage("Post " + message);
                        }
                    }
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.sendMessage("Logout");
        login = false;
        mClient.stopClient();
        try {
            mClient.socket.close();
            mClient.in.close();
            mClient.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class connectTask extends AsyncTask<String,String,Client> {

        @Override
        protected Client doInBackground(String... message) {
           mClient = new Client(new Client.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            });
            mClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.e("Log Message : ",values[0].toString());
            if(values[0].toString().startsWith("Username")){
                Log.e("Login error","New Nick");
                newnick = true;
                arrayList.add(values[0]);
            }else if (values[0].toString().startsWith("List")){
                Log.e("Success","Login");
                login = true;
                arrayList.add(values[0].toString().substring(5,values[0].length()));
            }else{
                arrayList.add(values[0]);
            }

            mAdapter.notifyDataSetChanged();
        }
    }
}
