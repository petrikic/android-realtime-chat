package com.example.realtimechat.controller;
import com.example.App;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SocketController {
    private static Socket mSocket;

    private SocketController() {
    }

    private static void createInstance() {

        final TokenController tokenController = TokenController.getInstance();

        try {
            mSocket = IO.socket(App.APLICATION_ADDRESS);
            mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Transport transport = (Transport)args[0];

                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            @SuppressWarnings("unchecked")
                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                            // modify request headers
                            headers.put("authorization", Arrays.asList(tokenController.getToken()));
                        }
                    });
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void clearInstance() {
        mSocket = null;
    }

    public static synchronized Socket getInstance() {
        if(mSocket == null)
            createInstance();
        return  mSocket;
    }
}
