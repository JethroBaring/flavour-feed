package com.finalproject.flavourfeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Pages.PostPage;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_card, parent, false);
        return new PostAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {

        holder.displayName.setText(posts.get(position).getDisplayName());
        holder.email.setText(posts.get(position).getEmail());
        Glide.with(holder.itemView.getContext()).load(posts.get(position).getProfileUrl()).into(holder.profile);
        Glide.with(holder.itemView.getContext()).load(posts.get(position).getPhotoUrl()).into(holder.postPhoto);
        holder.caption.setText(posts.get(position).getCaption());
        holder.postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), PostPage.class));
            }
        });
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postPhoto;
        ImageView profile;
        TextView displayName;
        TextView email;
        TextView caption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postPhoto = itemView.findViewById(R.id.postPhoto);
            profile = itemView.findViewById(R.id.profile);
            displayName = itemView.findViewById(R.id.displayName);
            email = itemView.findViewById(R.id.email);
            caption = itemView.findViewById(R.id.caption);
        }
    }
}