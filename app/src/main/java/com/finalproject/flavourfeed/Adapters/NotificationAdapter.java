package com.finalproject.flavourfeed.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;
import com.finalproject.flavourfeed.Entity.NotificationEntity;
import com.finalproject.flavourfeed.Pages.PostPage;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.StartupTime;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.zip.Inflater;


public class NotificationAdapter extends ListAdapter<NotificationEntity, NotificationAdapter.NotificationViewHolder> {
    NotificationClickInterface notificationClickInterface;

    public NotificationAdapter(@NonNull DiffUtil.ItemCallback<NotificationEntity> diffCallback, NotificationClickInterface notificationClickInterface) {
        super(diffCallback);
        this.notificationClickInterface = notificationClickInterface;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationEntity notification = getItem(position);
        holder.bind(notification);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView notificationProfile;
        TextView notificationDisplayName;
        TextView notificationText;
        LinearLayout requestButtons;
        RelativeLayout notificationLayout;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationProfile = itemView.findViewById(R.id.notificationProfile);
            notificationDisplayName = itemView.findViewById(R.id.notificationDisplayName);
            notificationText = itemView.findViewById(R.id.notificationText);
            requestButtons = itemView.findViewById(R.id.requestButtons);
            notificationLayout = itemView.findViewById(R.id.notification);
        }

        public void bind(NotificationEntity notification) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("userInformation").document(notification.getFromUserId());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        notificationDisplayName.setText(documentSnapshot.getString("displayName"));
                        Glide.with(itemView.getContext()).load(documentSnapshot.getString("profileUrl")).into(notificationProfile);
                    }
                }
            });

            if(notification.getNotificationType() == NotificationEntity.FRIEND_REQUEST_NOTIFICATION) {
                notificationText.setText(" sent you a friend request.");
            }
            else if(notification.getNotificationType() == NotificationEntity.COMMENT_NOTIFICATION) {
                notificationText.setText(" commented on your post.");
                requestButtons.setVisibility(View.INVISIBLE);
            } else {
                notificationText.setText("like your post");
                requestButtons.setVisibility(View.INVISIBLE);
            }
            notificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationClickInterface.onNotificationClick(notification);
                }
            });
        }
    }


    public interface NotificationClickInterface {
        public void onDelete(int pos);
        public void onNotificationClick(NotificationEntity notification);
    }
}
