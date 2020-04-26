package com.niza.app.givder.fragments;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.niza.app.givder.R;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.data.VideoItem;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment implements Step {

    private RecyclerView videosListView,bmsRecyclerView;
    private VideoItem[] bitmaps;
    private OnImageChangedListener onImageChangedListener;



    public void setBitmaps(VideoItem[] bitmaps) {
        this.bitmaps = bitmaps;
    }
    public static ImagesFragment newInstance(VideoItem[] bitmaps,OnImageChangedListener onImageChangedListener){
        ImagesFragment imagesFragment = new ImagesFragment();
        imagesFragment.setBitmaps(bitmaps);
        imagesFragment.setOnImageChangedListener(onImageChangedListener);
        return imagesFragment;
    }

    public ImagesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    public interface OnImageChangedListener{
        void onImageChangedListener(VideoItem bm);
    }

    public void setOnImageChangedListener(OnImageChangedListener onImageChangedListener) {
        this.onImageChangedListener = onImageChangedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_images, container, false);
        videosListView = view.findViewById(R.id.imgsRecyclerView);
        bmsRecyclerView = view.findViewById(R.id.bmsRecyclerView);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadContent();
        initBitmaps();
    }
    public void loadContent(){


            initImageRecyclerView();

    }



    private static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImg;
        public View item;
        public ViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.imgView);
            item = itemView;
        }
    }

    private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<VideoItem> mImageItems;
        private static final int HEADER = 0;
        private static final int CONTENT = 1;
        private int previousSelected = -1;
        public ContentAdapter(ArrayList<VideoItem> mImageItems){
            this.mImageItems = mImageItems;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            if(mImageItems.size()>0&&mImageItems.get(0).bitmap!=null)
            return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.layoutimg_selection_2, container, false));
            else
                return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.layoutimg_selection, container, false));

        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {



            //   holder.mImg.setImageURI(Uri.parse( mImageItems.get(position).path));
            // Utils.Get

            //   Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mImageItems.get(position).path, MediaStore.Video.Thumbnails.MICRO_KIND);
            //    holder.mImg.setImageBitmap(bitmap);
            if(mImageItems.get(position).bitmap==null){

                holder.mImg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipData.Item item = new ClipData.Item(mImageItems.get(position).path);

                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                        ClipData dragData = new ClipData(mImageItems.get(position).path,mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder( holder.mImg);

                        v.startDrag(dragData,myShadow,null,0);
                        // imgView.setVisibility(View.VISIBLE);

                        // Glide.with(getActivity()) .load(mImageItems.get(position).path)  .into(imgView);



                        return true;
                    }
                });
                holder.mImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                Glide.with(getActivity())
                        .load(mImageItems.get(position).path)
                        .into(holder.mImg);


                holder.mImg.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {

                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        switch(event.getAction()) {


                            case DragEvent.ACTION_DRAG_LOCATION  :
                                // Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                                x_cord = (int) event.getX();
                                y_cord = (int) event.getY();
                            //    imgView.setX(x_cord);
                                //    imgView.setY(y_cord);
                                break;

                            default: break;
                        }
                        return true;
                    }
                });
            }
            else{
                Glide.with(getActivity())
                        .load(mImageItems.get(position).bitmap)
                        .into(holder.mImg);


                holder.mImg.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        switch(event.getAction()) {

                            case DragEvent.ACTION_DRAG_ENTERED:
                                //Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                                int x_cord = (int) event.getX();
                                int y_cord = (int) event.getY();

                                holder.mImg.animate().scaleX(1.4f).start();
                                holder.mImg.animate().scaleY(1.4f).start();

                                break;

                            case DragEvent.ACTION_DRAG_EXITED :
                               /*Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                                 x_cord = (int) event.getX();
                                y_cord = (int) event.getY();
                                layoutParams.leftMargin = x_cord;
                                layoutParams.topMargin = y_cord;
                                v.setLayoutParams(layoutParams);*/

                                holder.mImg.animate().scaleX(1f).start();
                                holder.mImg.animate().scaleY(1f).start();
                                break;

                            case DragEvent.ACTION_DROP:
                                //  imgView.setVisibility(View.GONE);
                              //  Log.d(msg, "ACTION_DROP event");

                                // Do nothing

                                Glide.with(getActivity())
                                        .load(
                                                event.getClipData().getItemAt(0).getText())
                                        .into(holder.mImg);
                                onImageChangedListener.onImageChangedListener(
                                        new VideoItem(mImageItems.get(position).id,
                                                Utils.UriToBitmap(getActivity(),Uri.parse(event.getClipData().getItemAt(0).getText().toString()))

                                                )
                                );
                                break;
                            default: break;
                        }
                        return true;
                    }
                });

            }


        }
        @Override
        public int getItemCount() {
            return mImageItems.size();
        }
    }


    private void initImageRecyclerView(){
        Uri externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ArrayList<VideoItem> mImageItems = new ArrayList<>();
        String[] projection = {  MediaStore.Images.Media._ID,MediaStore.Images.Media.DATE_MODIFIED

        };


        Cursor mCursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null,MediaStore.Images.Media.DATE_MODIFIED+" DESC");

        while (mCursor.moveToNext()) {

            long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri uri = ContentUris.withAppendedId(externalUri, id);
            mImageItems.add(new VideoItem(uri.toString(),mCursor.getString(0)
                    ,mCursor.getString(0)
                    ,mCursor.getString(0)
                    ,mCursor.getString(0)));
        }
        mCursor.close();
        GridLayoutManager listManager = new GridLayoutManager(getContext(),2, GridLayoutManager.HORIZONTAL, false);

        videosListView.setLayoutManager(listManager);

        videosListView.setAdapter(new ContentAdapter(mImageItems));
        Log.i("images ","images size "+mImageItems.size());
    }
    private void initBitmaps(){


        ArrayList<VideoItem> mImageItems = new ArrayList<>();
        /*for(VideoItem bm : bitmaps){
            mImageItems.add(bm);
        }*/
        LinearLayoutManager listManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        bmsRecyclerView.setLayoutManager(listManager);

        bmsRecyclerView.setAdapter(new ContentAdapter(mImageItems));
    }

}
