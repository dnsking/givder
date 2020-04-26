package com.niza.app.givder.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niza.app.givder.App;
import com.niza.app.givder.R;
import com.niza.app.givder.Utils.UiUtils;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.networking.actions.CheckGiverMessageNetwork;
import com.niza.app.givder.networking.actions.GiverMessageNetwork;
import com.niza.app.givder.networking.authentication.GivderContentHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment {


    private GiverMessageNetwork[] giverMessageNetwork;
    private RecyclerView matchesRecyclerView;


    private GiverMessageNetwork[] giverMessageNetworkRequests;
    private GiverMessageNetwork[] giverMessageNetworkTexts;
    private String myNumber;
    public MatchesFragment() {
        // Required empty public constructor
    }

    public GiverMessageNetwork[] getGiverMessageNetwork() {
        return giverMessageNetwork;
    }

    public void setGiverMessageNetwork(GiverMessageNetwork[] giverMessageNetwork) {
        this.giverMessageNetwork = giverMessageNetwork;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myNumber = Utils.GetUserName(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    giverMessageNetwork=
                            GivderContentHelper. CheckMessages(getActivity(), new CheckGiverMessageNetwork (myNumber));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            init();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void init(){
        if(matchesRecyclerView!=null&&giverMessageNetwork!=null){

            ArrayList<GiverMessageNetwork> giverMessageNetworkRequestsList = new ArrayList<>();
            ArrayList<GiverMessageNetwork> giverMessageNetworkTextsList = new ArrayList<>();

            for(GiverMessageNetwork item :giverMessageNetwork){
                if(item.getType().equals(App.MessageType_Request)){
                    giverMessageNetworkRequestsList.add(item);
                }
                else{
                    giverMessageNetworkTextsList.add(item);
                }
            }
            giverMessageNetworkRequests = giverMessageNetworkRequestsList.toArray(new GiverMessageNetwork[]{});
            giverMessageNetworkTexts = giverMessageNetworkTextsList.toArray(new GiverMessageNetwork[]{});

            LinearLayoutManager listManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

            matchesRecyclerView.setLayoutManager(listManager);

            matchesRecyclerView.setAdapter(new ContentAdapter());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_matches, container, false);
        matchesRecyclerView = view.findViewById(R.id.matchesRecyclerView);
        return view;
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
