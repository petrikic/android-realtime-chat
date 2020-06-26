package com.example.realtimechat;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Button;

import com.example.realtimechat.controller.SocketController;
import com.github.nkzawa.socketio.client.Socket;


public class Messages extends Fragment {

    Button btn;
    Socket socket;

    public Messages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        socket = SocketController.getInstance();

        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        btn = view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.emit("teste", "teste");
            }
        });
        return view;
    }
}
