package com.example.realtimechat;
import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimechat.controller.OnlineController;
import com.example.realtimechat.model.User;
import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import org.json.JSONArray;
import java.util.ArrayList;


public class Online extends Fragment {

    Socket socket;
    private GroupAdapter adapter;
    private JSONArray users;

    public Online() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_online, container, false);
        //socket = SocketController.getInstance();

        RecyclerView rv = view.findViewById(R.id.recycler);
        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(view.getContext(), Chat.class);

                UserItem userItem = (UserItem) item;
                intent.putExtra("user", userItem.user);
                startActivity(intent);
            }
        });

        fetchUsers();

        return view;
    }

    private class UserItem extends Item<ViewHolder> {

        User user;

        private UserItem(User user) {
            this.user = user;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView username = viewHolder.itemView.findViewById(R.id.username_profile);
            ImageView userImage = viewHolder.itemView.findViewById(R.id.image_profile);

            username.setText(user.getUsername());


            Picasso.get()
                    .load(user.getUrlPhoto())
                    .into(userImage);
        }

        @Override
        public int getLayout() {
            return R.layout.item_user;
        }
    }

    private void fetchUsers() {
        ArrayList<User> users = OnlineController.getUsers();
        for(User user: users) {
            adapter.add(new UserItem(user));
        }
    }

}