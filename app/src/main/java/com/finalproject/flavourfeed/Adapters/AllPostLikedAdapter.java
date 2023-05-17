package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Models.PostModel;
import com.finalproject.flavourfeed.R;

public class AllPostLikedAdapter extends ListAdapter<PostModel, AllPostLikedAdapter.AllPostLikedViewHolder> {
    PostClickInterface postClickInterface;
    public AllPostLikedAdapter(@NonNull DiffUtil.ItemCallback<PostModel> diffCallback, PostClickInterface postClickInterface) {
        super(diffCallback);
        this.postClickInterface = postClickInterface;
    }

    @NonNull
    @Override
    public AllPostLikedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllPostLikedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_post_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllPostLikedViewHolder holder, int position) {
        PostModel postModel = getItem(position);
        holder.bind(postModel);
    }

    class AllPostLikedViewHolder extends RecyclerView.ViewHolder {
        ImageView allpost;
        public AllPostLikedViewHolder(@NonNull View itemView) {
            super(itemView);
            allpost = itemView.findViewById(R.id.allpost);
        }

        public void bind(PostModel postModel) {
            Glide.with(itemView.getContext()).load(postModel.getPhotoUrl()).into(allpost);
            allpost.setOnClickListener(v -> postClickInterface.onPostClick(postModel.getPostId()));
        }
    }

    public interface PostClickInterface {
        public void onPostClick(String postId);
    }
}
