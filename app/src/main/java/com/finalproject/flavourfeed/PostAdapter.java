package com.finalproject.flavourfeed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Pages.PostPage;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    int[] test;

    public PostAdapter(Context context) {
        this.context = context;
        test = new int[10];
        test[0] = 1;
        test[1] = 2;
        test[2] = 3;
        test[3] = 4;
        test[4] = 5;
        test[5] = 6;
        test[6] = 7;
        test[7] = 8;
        test[8] = 9;
        test[9] = 10;
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
        holder.postPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), PostPage.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postPicture;
        ImageView userProfilePicture;
        TextView userDisplayName;
        TextView userEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postPicture = itemView.findViewById(R.id.postPicture);
            userProfilePicture = itemView.findViewById(R.id.userProfilePicture);
            userDisplayName = itemView.findViewById(R.id.userDisplayName);
            userEmail = itemView.findViewById(R.id.userEmail);
            postPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}