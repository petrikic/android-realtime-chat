package com.example.realtimechat;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimechat.controller.SocketController;
import com.example.realtimechat.database.ControllerDB;
import com.example.realtimechat.model.Message;
import com.example.realtimechat.model.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Messages extends Fragment {

    Socket socket;
    private GroupAdapter adapter;
    private RecyclerView rv;
    private ControllerDB controllerDB;
    private Map<Long, Message> mapMessages = new HashMap<Long, Message>();
    private int adapterSize = 0;

    public Messages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        controllerDB = ControllerDB.getInstance();
        socket = SocketController.getInstance();

        rv = view.findViewById(R.id.recycler);
        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                if (item instanceof UserItem) {
                    Intent intent = new Intent(view.getContext(), Chat.class);

                    UserItem userItem = (UserItem) item;
                    intent.putExtra("user", userItem.user);
                    startActivity(intent);
                }
            }
        });

        socket.on("getUser", getUser);
        socket.on("receiveMessage", receiveMessage);


        return view;
    }

    private Emitter.Listener receiveMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.clear();
                        //fetchMessages();
                    }
                });
            }
        }
    };

    private Emitter.Listener getUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject user = (JSONObject) args[0];
                        try {
                            int id = user.getInt("id");
                            String username = user.getString("username");
                            String url = user.getString("urlPhoto");
                            User u = new User(id, url, username);
                            Message message = controllerDB.getLastMessage(u);
                            adapter.add(0, new UserItem(u, message));
                        } catch (JSONException e) {
                            Log.e("JSONError", e.getMessage(), e);
                        }
                    }
                });
            }
        }
    };

    private void fetchMessages() {
        List<Long> usersId = controllerDB.getUsersId();
        if (usersId.size() <= 0) {
            adapter.add(new EmptyUserItem());
            return;
        }
        for (Long uuid : usersId) {
            JSONObject value = new JSONObject();
            try {
                value.put("uuid", uuid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("getUser", value);
        }
    }

    private class EmptyUserItem extends Item<ViewHolder> {

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

        }

        @Override
        public int getLayout() {
            return R.layout.empty_message;
        }
    }

    private class UserItem extends Item<ViewHolder> {

        User user;
        Message message;

        private UserItem(User user, Message message) {
            this.user = user;
            this.message = message;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView username = viewHolder.itemView.findViewById(R.id.username_profile);
            TextView textMessage = viewHolder.itemView.findViewById(R.id.message_text);
            TextView timestamp = viewHolder.itemView.findViewById(R.id.timestamp);
            ImageView userImage = viewHolder.itemView.findViewById(R.id.image_profile);
            Date date = new Date(message.getTimestamp());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            username.setText(user.getUsername());
            textMessage.setText(message.getText());
            timestamp.setText(sdf.format(date));


            Picasso.get()
                    .load(user.getUrlPhoto())
                    .into(userImage);
        }

        @Override
        public int getLayout() {
            return R.layout.item_recent_message;
        }
    }

    @Override
    public void onResume() {
        adapter.clear();
        fetchMessages();
        super.onResume();
    }
}
