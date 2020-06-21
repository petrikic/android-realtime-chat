package com.example.realtimechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimechat.controller.TokenController;
import com.github.nkzawa.socketio.client.Socket;


public class MainActivity extends AppCompatActivity {

    private Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //socket = SocketController.getInstance();
        //socket.connect();

        if(TokenController.isEmpty()){
            login();
        } else {
            Toast.makeText(this, "Token obtido" + Toast.LENGTH_SHORT, Toast.LENGTH_SHORT);
        }

    }

    private void login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
