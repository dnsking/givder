package com.niza.app.givder.fragments;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentUris;
import android.content.Intent;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.niza.app.givder.App;
import com.niza.app.givder.MessasingActivity;
import com.niza.app.givder.NewPostActivity;
import com.niza.app.givder.R;
import com.niza.app.givder.Utils.UiUtils;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.data.VideoItem;
import com.niza.app.givder.networking.actions.GiverMessageNetwork;
import com.niza.app.givder.networking.actions.UserNetworkAction;
import com.niza.app.givder.networking.authentication.GivderContentHelper;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private CardStackView card_stack_view;
    private View skip_button,rewind_button,like_button;
    private String myNumber;
    private int currentPosition=-1;

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
        if(card_stack_view!=null&&userNetworkActions!=null){

            card_stack_view.setLayoutManager(new CardStackLayoutManager(getActivity(),
                    new CardStackListener(){
                        @Override
                        public void onCardDragging(Direction direction, float ratio) {

                        }

                        @Override
                        public void onCardSwiped(Direction direction) {
                            if(direction.equals(Direction.Left)){
                                App.Log("Swiped left on "+currentPosition);
                                Snackbar.make(card_stack_view,"Swiped Left",Snackbar.LENGTH_SHORT).show();}
                           else if(direction.equals(Direction.Right)){
                                Snackbar.make(card_stack_view,"Swiped Right",Snackbar.LENGTH_SHORT).show();
                                App.Log("Swiped Right on "+currentPosition);

                              final GiverMessageNetwork giverMessageNetwork = new GiverMessageNetwork(myNumber
                                        , Long.toString(new Date().getTime()),userNetworkActions[currentPosition].getPhoneNumber()
                                      ,new Gson().toJson(userNetworkActions[currentPosition]),
                                        App.MessageType_Request);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            GivderContentHelper.AddContent(getActivity(),giverMessageNetwork);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }

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

                            Snackbar.make(card_stack_view,"Card gone "+position,Snackbar.LENGTH_SHORT).show();
                            App.Log("Card gone "+position);
                            currentPosition = position;
                        }
                    }));

                card_stack_view.setAdapter(new ContentAdapter());
            skip_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ( (CardStackLayoutManager) card_stack_view.getLayoutManager()).setSwipeAnimationSetting(
                            new SwipeAnimationSetting.Builder()
                                    .setDirection(Direction.Left)
                                    .setDuration(Duration.Normal.duration)
                                    .setInterpolator(new AccelerateInterpolator())
                                    .build());
                    card_stack_view.swipe();
                }
            });
            rewind_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card_stack_view.rewind();
                }
            });
            like_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ( (CardStackLayoutManager) card_stack_view.getLayoutManager()).setSwipeAnimationSetting(
                            new SwipeAnimationSetting.Builder()
                                    .setDirection(Direction.Right)
                                    .setDuration(Duration.Normal.duration)
                                    .setInterpolator(new AccelerateInterpolator())
                                    .build());
                    card_stack_view.swipe();
                }
            });

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myNumber = Utils.GetUserName(getActivity());
        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userNetworkActions=      GivderContentHelper.GetContent(getActivity());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        card_stack_view = view.findViewById(R.id.card_stack_view);
        skip_button= view.findViewById(R.id.skip_button);
        rewind_button= view.findViewById(R.id.rewind_button);
        like_button= view.findViewById(R.id.like_button);
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

            int color = Integer.parseInt(userNetworkAction.getColor());

            holder.contentHolder.setBackgroundColor(color);
            holder.firstCircle.setBackground( Utils.CircleFromColor(UiUtils.ManipulateColor(color,0.7f)));
            holder.secondCircle.setBackground( Utils.CircleFromColor(UiUtils.ManipulateColor(color,0.7f)));

            holder.numberOfPlatesTextView.setText(userNetworkAction.getPlates());
            holder.descriptionTextView.setText(userNetworkAction.getDescription());
            holder.timeView.setText(Long.toString(new Date(new Date().getTime()-userNetworkAction.getTimeExpiration()).getHours()));



        }
        @Override
        public int getItemCount() {
            return userNetworkActions.length;
        }
    }




}
