package com.example.realtimechat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.realtimechat.controller.OnlineController;
import com.example.realtimechat.controller.SocketController;
import com.example.realtimechat.database.ControllerDB;
import com.example.realtimechat.model.Message;
import com.example.realtimechat.model.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    TabLayout tabMenu;
    FragmentManager fm = getFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    private Socket socket;
    private JSONArray users;
    private ControllerDB controllerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        controllerDB = ControllerDB.getInstance();

        socket = SocketController.getInstance();
        socket.connect();

        socket.on("listUsers", listUsers);
        socket.on("newUser", newUser);
        socket.on("receiveMessage", receiveMessage);
        socket.on("dropUser", dropUser);

        loadMessagesFragment();
        tabMenu = findViewById(R.id.tab_menu);
        tabMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadMessagesFragment();
                        break;
                    case 1:
                        loadOnlineFragment();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private Emitter.Listener listUsers = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        users = data.getJSONArray("users");
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = (JSONObject) users.get(i);
                            int id = user.getInt("id");
                            String username = user.getString("username");
                            String url = user.getString("urlPhoto");
                            User u = new User(id, url, username);
                            OnlineController.addOne(u);
                        }
                    } catch (JSONException e) {
                        Log.e("JSONError", e.getMessage(), e);
                    }
                }
            });
        }
    };

    private Emitter.Listener newUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject user = (JSONObject) args[0];
                    try {
                        int id = user.getInt("id");
                        String username = user.getString("username");
                        String url = user.getString("urlPhoto");
                        User u = new User(id, url, username);
                        OnlineController.addOne(u);
                    } catch (JSONException e) {
                        Log.e("JSONError", e.getMessage(), e);
                    }
                }
            });
        }
    };

    private Emitter.Listener dropUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject user = (JSONObject) args[0];
                    try {
                        int id = user.getInt("id");
                        String username = user.getString("username");
                        String url = user.getString("urlPhoto");
                        User u = new User(id, url, username);
                        OnlineController.removeOne(u);
                    } catch (JSONException e) {
                        Log.e("JSONError", e.getMessage(), e);
                    }
                }
            });
        }
    };

    private final Emitter.Listener receiveMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String text = data.getString("text");
                        long senderId = data.getLong("senderId");
                        long userReferenceId = data.getLong("userReferenceId");
                        long timestamp = data.getLong("timestamp");
                        Message message = new Message(text, userReferenceId, senderId, timestamp);

                        controllerDB.insert(message);
                    } catch (JSONException e) {
                        Log.e("JSONError", e.getMessage(), e);
                    }
                }
            });
        }
    };

    private void loadOnlineFragment() {
        Online fragOnline = new Online();
        ft = fm.beginTransaction();
        ft.replace(R.id.content, fragOnline, "Online");
        ft.commit();
    }

    private void loadMessagesFragment() {
        Messages fragMessages = new Messages();
        ft = fm.beginTransaction();
        ft.replace(R.id.content, fragMessages, "Messages");
        ft.commit();
    }
}
