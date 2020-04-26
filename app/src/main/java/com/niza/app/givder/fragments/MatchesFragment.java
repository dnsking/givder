package com.niza.app.givder.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.niza.app.givder.App;
import com.niza.app.givder.MainActivity;
import com.niza.app.givder.R;
import com.niza.app.givder.Utils.UiUtils;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.networking.actions.CheckGiverMessageNetwork;
import com.niza.app.givder.networking.actions.GiverMessageNetwork;
import com.niza.app.givder.networking.actions.UserNetworkAction;
import com.niza.app.givder.networking.authentication.GivderContentHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment {


    private GiverMessageNetwork[] giverMessageNetworks;
    private RecyclerView matchesRecyclerView;


    private GiverMessageNetwork[] giverMessageNetworkRequests;
    private GiverMessageNetwork[] giverMessageNetworkTexts;
    private String myNumber;
    public MatchesFragment() {
        // Required empty public constructor
    }

    public GiverMessageNetwork[] getGiverMessageNetwork() {
        return giverMessageNetworks;
    }

    public void setGiverMessageNetwork(GiverMessageNetwork[] giverMessageNetwork) {
        this.giverMessageNetworks = giverMessageNetwork;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myNumber = Utils.GetUserName(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    giverMessageNetworks=
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
        if(matchesRecyclerView!=null&&giverMessageNetworks!=null){
            ((MainActivity) getActivity()).hideProgressBar();

            ArrayList<GiverMessageNetwork> giverMessageNetworkRequestsList = new ArrayList<>();
            ArrayList<GiverMessageNetwork> giverMessageNetworkTextsList = new ArrayList<>();

            for(GiverMessageNetwork item :giverMessageNetworks){
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
        public View item,acceptButton,callButton,textButton;
        public TextView numberOfPlatesTextView,descriptionTextView,distanceTextView,timeView;
        public ViewHolder(View itemView) {
            super(itemView);
            numberOfPlatesTextView = itemView.findViewById(R.id.numberOfPlatesTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            timeView = itemView.findViewById(R.id.timeView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            callButton = itemView.findViewById(R.id.callButton);
            textButton = itemView.findViewById(R.id.textButton);
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

            if(viewType==HEADER)
                return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.received_match, container, false));
             else
                return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.accept_received_match, container, false));


        }

        @Override
        public int getItemViewType(int position) {
            if(  giverMessageNetworks[position].equals(App.MessageType_Request))
                return 0;
            else return 1;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

               final GiverMessageNetwork giverMessageNetwork=giverMessageNetworks[position];


            final UserNetworkAction userNetworkAction = new Gson().fromJson(giverMessageNetwork.getMessage(),UserNetworkAction.class);

            int color = Integer.parseInt(userNetworkAction.getColor());


            holder.numberOfPlatesTextView.setText(userNetworkAction.getPlates());
            holder.descriptionTextView.setText(userNetworkAction.getDescription());
            holder.timeView.setText(Long.toString(new Date(new Date().getTime()-userNetworkAction.getTimeExpiration()).getHours()));


            try {
                holder.distanceTextView.setText(  Utils.LocationName(getActivity(),Double.parseDouble(userNetworkAction.getLat()),
                        Double.parseDouble(userNetworkAction.getLon())));
            } catch (IOException e) {
                e.printStackTrace();

                holder.distanceTextView.setText("Unknown Location");
            }
            if( getItemViewType( position)==0){

                holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        giverMessageNetworks[position].setType(App.MessageType_Accept);
                        final GiverMessageNetwork giverMessageNetwork = new GiverMessageNetwork(myNumber
                                , Long.toString(new Date().getTime()),giverMessageNetworks[position].getFrom()
                                ,giverMessageNetworks[position].getMessage(),
                                App.MessageType_Accept);
                        notifyItemChanged(position);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    GivderContentHelper.AddContent(getActivity(),giverMessageNetwork);


                                    GivderContentHelper.AddContent(getActivity(),giverMessageNetworks[position]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }
            else{
                holder.callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + giverMessageNetwork.getFrom()));
                        startActivity(intent);
                    }
                });
                holder.textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", giverMessageNetwork.getFrom(), null)));
                    }
                });
            }


        }
        @Override
        public int getItemCount() {
            return giverMessageNetworks.length;
        }
    }

}
