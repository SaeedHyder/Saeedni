package com.ingic.saeedni.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.DockActivity;
import com.ingic.saeedni.interfaces.onDeleteImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created on 4/7/2017.
 */

public class RecyclerViewAdapterImages extends RecyclerView.Adapter<RecyclerViewAdapterImages.MyViewHolder> {


    private DockActivity context;
    private ImageLoader imageLoader;
    private List<String> addedImages;
    private onDeleteImage onDeleteImage;


    public RecyclerViewAdapterImages(List<String> Addedimages, DockActivity a, onDeleteImage onDeleteImage) {
        this.addedImages = Addedimages;
        this.context = a;
        imageLoader = ImageLoader.getInstance();
        this.onDeleteImage = onDeleteImage;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addimage_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String imagespath = addedImages.get(position);

        //Picasso.with(context).load(imagespath).into(holder.img_addedimages);
        if (imagespath.contains("https")) {
            imageLoader.displayImage(imagespath, holder.img_addedimages);
        } else {
            imageLoader.displayImage("file://" + imagespath, holder.img_addedimages);
        }
        holder.delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.replaceDockableFragment(ChatFragment.newInstance(), "Chat Fragment");
                onDeleteImage.onDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addedImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_addedimages;
        public ImageView delete_image;

        public MyViewHolder(View view) {
            super(view);
            img_addedimages = (ImageView) view.findViewById(R.id.img_addedimages);
            delete_image = (ImageView) view.findViewById(R.id.delete_image);

        }
    }
}