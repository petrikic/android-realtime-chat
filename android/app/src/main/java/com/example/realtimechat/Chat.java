package com.example.realtimechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.realtimechat.controller.SocketController;
import com.example.realtimechat.database.ControllerDB;
import com.example.realtimechat.model.Message;
import com.example.realtimechat.model.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Chat extends AppCompatActivity {

    private GroupAdapter adapter;
    private RecyclerView rv;
    private Toolbar mToolbar;
    private Socket socket;
    private EditText edt_message;
    private User user;
    private ControllerDB controllerDB;
    private int messageSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        controllerDB = ControllerDB.getInstance();

        socket = SocketController.getInstance();
        mToolbar = findViewById(R.id.tb_chat);
        ImageView img = findViewById(R.id.image_profile);
        TextView txt = findViewById(R.id.title_toolbar);

        user = getIntent().getExtras().getParcelable("user");
        Picasso.get().load(user.getUrlPhoto()).into(img);
        txt.setText(user.getUsername());

        edt_message = findViewById(R.id.edt_chat);
        ImageView back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btn_send = findViewById(R.id.btn_chat);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                hideInput(v);
            }
        });


        rv = findViewById(R.id.recycler);
        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        socket.on("receiveMessage", receiveMessage);

        fetchMessages();
        rv.scrollToPosition(messageSize - 1);

    }

    private void sendMessage() {
        if (edt_message.getText().length() > 0) {
            String text = edt_message.getText().toString();
            edt_message.setText("");
            long uuid = user.getId();
            JSONObject message = new JSONObject();
            try {
                message.put("receiverId", uuid);
                message.put("text", text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("sendMessage", message);
        }
    }

    private final Emitter.Listener receiveMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String text = data.getString("text");
                        long userReferenceId = data.getLong("userReferenceId");
                        long senderId = data.getLong("senderId");
                        long timestamp = data.getLong("timestamp");
                        Message message = new Message(text, userReferenceId, senderId, timestamp);
                        adapter.add(new MessageItem(message));
                        rv.scrollToPosition(messageSize++);

                    } catch (JSONException e) {
                        Log.e("JSONError", e.getMessage(), e);
                    }
                }
            });
        }
    };

    private void fetchMessages() {
        List<Message> messages = controllerDB.getMessages(user);
        for (Message message : messages) {
            adapter.add(new MessageItem(message));
            messageSize++;
        }
    }


    private void hideInput(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class MessageItem extends Item<ViewHolder> {

        private final Message message;

        private MessageItem(Message message) {
            this.message = message;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView text = viewHolder.itemView.findViewById(R.id.text);
            text.setText(message.getText());

        }

        @Override
        public int getLayout() {
            return message.getSenderId() == user.getId() ?
                    R.layout.item_from_message : R.layout.item_to_message;
        }
    }
}
