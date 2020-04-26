package com.niza.app.givder.fragments;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.niza.app.givder.R;
import com.niza.app.givder.Utils.UiUtils;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.data.VideoItem;
import com.niza.app.givder.networking.actions.UserNetworkAction;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private CardStackView card_stack_view;

    public UserNetworkAction[] getUserNetworkActions() {
        return userNetworkActions;
    }

    public void setUserNetworkActions(UserNetworkAction[] userNetworkActions) {
        this.userNetworkActions = userNetworkActions;
        init();
    }

    private UserNetworkAction[] userNetworkActions;
    public ContentFragment() {
        // Required empty public constructor
    }
    public static ContentFragment newInstance(UserNetworkAction[] userNetworkActions){
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setUserNetworkActions(userNetworkActions);
        return contentFragment;
    }

    public void init(){
        if(card_stack_view!=null){

            card_stack_view.setLayoutManager(new CardStackLayoutManager(getActivity(),
                    new CardStackListener(){
                        @Override
                        public void onCardDragging(Direction direction, float ratio) {

                        }

                        @Override
                        public void onCardSwiped(Direction direction) {

                        }

                        @Override
                        public void onCardRewound() {

                        }

                        @Override
                        public void onCardCanceled() {

                        }

                        @Override
                        public void onCardAppeared(View view, int position) {

                        }

                        @Override
                        public void onCardDisappeared(View view, int position) {

                        }
                    }));
            if(userNetworkActions!=null)
                card_stack_view.setAdapter(new ContentAdapter());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        card_stack_view = view.findViewById(R.id.card_stack_view);
        return view;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        public View item;
        public View contentHolder,firstCircle,secondCircle;
        public TextView numberOfPlatesTextView,descriptionTextView,distanceTextView,timeView;
        public ViewHolder(View itemView) {
            super(itemView);
            contentHolder = itemView.findViewById(R.id.contentHolder);
            firstCircle = itemView.findViewById(R.id.firstCircle);
            secondCircle = itemView.findViewById(R.id.secondCircle);
            numberOfPlatesTextView = itemView.findViewById(R.id.numberOfPlatesTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            timeView = itemView.findViewById(R.id.timeView);
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
            return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.feed_item_layout, container, false));

        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            UserNetworkAction userNetworkAction = userNetworkActions[position];

            int color = Color.parseColor(userNetworkAction.getColor());

            holder.contentHolder.setBackgroundColor(color);
            holder.firstCircle.setBackground( Utils.CircleFromColor(UiUtils.ManipulateColor(color,0.7f)));
            holder.secondCircle.setBackground( Utils.CircleFromColor(UiUtils.ManipulateColor(color,0.7f)));

            holder.numberOfPlatesTextView.setText(userNetworkAction.getPlates());
            holder.descriptionTextView.setText(userNetworkAction.getDescription());
            holder.timeView.setText(new Date(new Date().getTime()-userNetworkAction.getTimeExpiration()).getHours());



        }
        @Override
        public int getItemCount() {
            return userNetworkActions.length;
        }
    }




}
