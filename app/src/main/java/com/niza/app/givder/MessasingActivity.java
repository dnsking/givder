package com.niza.app.givder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.db.helpers.GivderMessageDBHelper;
import com.niza.app.givder.fragments.ImagesFragment;
import com.niza.app.givder.networking.actions.GiverMessageNetwork;
import com.niza.app.givder.networking.authentication.GivderContentHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MessasingActivity extends AppCompatActivity {

    private GiverMessageNetwork[] giverMessageNetworkTexts;
    private TextInputEditText messageEditText;
    private ArrayList<GiverMessageNetwork> messages;
    private String myNumber;
    private RecyclerView messageRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messasing);

        giverMessageNetworkTexts = new Gson().fromJson(getIntent().getStringExtra(App.Content),GiverMessageNetwork[].class);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);

        messages = new ArrayList<>();
        init();
    }
    private void sendMessage(){
        final GiverMessageNetwork giverMessageNetwork = new GiverMessageNetwork(myNumber
                , Long.toString(new Date().getTime()),giverMessageNetworkTexts[0].getFrom(),messageEditText.getText().toString(),
                App.MessageType_Text);


        GivderMessageDBHelper givderMessageDBHelper = new GivderMessageDBHelper(this);
        givderMessageDBHelper.open();
        givderMessageDBHelper.insertEntry(giverMessageNetwork);
        givderMessageDBHelper.close();

        messageEditText.setText(null);
        messages.add(giverMessageNetwork);

        messageRecyclerView.getAdapter().notifyItemInserted(messages.size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GivderContentHelper.AddContent(MessasingActivity.this,giverMessageNetwork);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void init(){
        messages.addAll(Arrays.asList(giverMessageNetworkTexts));

        GivderMessageDBHelper givderMessageDBHelper = new GivderMessageDBHelper(this);
        givderMessageDBHelper.open();

        messages.addAll(Arrays.asList( givderMessageDBHelper.queryAllTo(giverMessageNetworkTexts[0].getFrom())));

        givderMessageDBHelper.close();

        Collections.sort(messages, new Comparator<GiverMessageNetwork>() {
            @Override
            public int compare(GiverMessageNetwork teamMember1, GiverMessageNetwork teamMember2) {
                return teamMember1.getTime().toUpperCase().compareTo(teamMember2.getTime().toUpperCase());
            }
        });
        myNumber = Utils.GetUserName(this);



        messageEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });
        LinearLayoutManager listManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        messageRecyclerView.setLayoutManager(listManager);

        messageRecyclerView.setAdapter(new ContentAdapter());

    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        public View item;
        public TextView userNameTextView,timeTextView,contentTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            item = itemView;
        }
    }
    private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final int HEADER = 0;
        private static final int CONTENT = 1;
        private int previousSelected = -1;
        public ContentAdapter(){
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            if(viewType==HEADER){
                return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.received_message_layout, container, false));

            }
            else{
                return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.received_message_layout, container, false));
            }

        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            if(position!=0){
                GiverMessageNetwork giverMessageNetwork=giverMessageNetworkTexts[position-1];

                holder.userNameTextView.setText(giverMessageNetwork.getFrom());
                holder.timeTextView.setText(giverMessageNetwork.getTime());
                holder.contentTextView.setText(giverMessageNetwork.getMessage());
                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            else{}


        }
        @Override
        public int getItemCount() {
            return giverMessageNetworkTexts.length+1;
        }
    }
}
