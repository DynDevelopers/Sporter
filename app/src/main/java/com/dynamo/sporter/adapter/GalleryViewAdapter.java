package com.dynamo.sporter.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;

import com.dynamo.sporter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.ViewHolder> {

    List<Task<Uri>> images;
    Context context;

    public GalleryViewAdapter(List<Task<Uri>> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public GalleryViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image_view_layout, parent, false);

        // Passing view to ViewHolder
        GalleryViewAdapter.ViewHolder viewHolder = new GalleryViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewAdapter.ViewHolder holder, int position) {
        Task<Uri> url = images.get(position);
        url.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("Image URL", uri.toString());
                Picasso.get().load(uri).into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error Image Retrival", e.getLocalizedMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imageView = (ImageView) view.findViewById(R.id.image);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
