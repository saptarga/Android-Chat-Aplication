package com.example.arga.chatapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Arga on 10/12/2016.
 */
public class Client {
    private String serverMessage;
    private String pesan;
    public static  String SERVERIP ; // your computer IP address
    public static String username;
    public static final int SERVERPORT = 2222;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private boolean login = false;
    private MainActivity main;

    PrintWriter out;
    BufferedReader in;
    Socket socket;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages
     * received from server
     */
    public Client(OnMessageReceived listener) {
        mMessageListener = listener;

    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message
     *            text entered by client
     */
    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            Log.e("Post User : ",message);
            out.flush();
        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {

            // here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Log.e("serverAddr", serverAddr.toString());
            Log.e("TCP Client", "C: Connecting...");

            // create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVERPORT);
            Log.e("TCP Server IP", SERVERIP);
            try {

                // send the message to the server
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                // receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                if (!login){
                    if(out != null) {
                        out.println("Login: "+username.replace(" ","<"));
                        Log.e("", "tampilllllllllllllllllll");
                    }
                }
                while (mRun) {
                    serverMessage = in.readLine();
                    Log.e("Message From Server : ",serverMessage);
                    if (serverMessage != null && mMessageListener != null) {
                        // call the method messageReceived from MyActivity class
                        if (serverMessage.startsWith("New Nick")) {
                            mMessageListener.messageReceived("Username Sudah Ada, Silahkan Masukan Username Baru");
                        }else if(serverMessage.startsWith("List")){
                            mMessageListener.messageReceived(("List Welcome to Chat..."));
                        }else if(serverMessage.startsWith("Recieve")){
                            pesan = serverMessage.replace("<"," ");
                            mMessageListener.messageReceived(pesan.substring(8,pesan.length()));
                        }else if (serverMessage.startsWith("PrivateRecieve")){
                            pesan = serverMessage.replace("<"," ");
                            mMessageListener.messageReceived("Private Messages : "+pesan.substring(14,pesan.length()));
                        }else if(serverMessage.startsWith("Request")){
                            String tampil = serverMessage.substring(8,serverMessage.length());
                            pesan = tampil.replace("<>","\n");
                            mMessageListener.messageReceived(pesan);
                        }

                    }
                    serverMessage = null;

                }
            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                // the socket must be closed. It is not possible to reconnect to
                // this socket
                // after it is closed, which means a new socket instance has to
                // be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    // Declare the interface. The method messageReceived(String message) will
    // must be implemented in the MyActivity
    // class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
