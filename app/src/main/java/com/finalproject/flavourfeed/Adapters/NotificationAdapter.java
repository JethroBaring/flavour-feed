package com.finalproject.flavourfeed.Adapters;

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
import com.finalproject.flavourfeed.Firebase.FirebaseOperations;
import com.finalproject.flavourfeed.Models.NotificationModel;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class NotificationAdapter extends ListAdapter<NotificationModel, NotificationAdapter.NotificationViewHolder> {
    NotificationClickInterface notificationClickInterface;

    public NotificationAdapter(@NonNull DiffUtil.ItemCallback<NotificationModel> diffCallback, NotificationClickInterface notificationClickInterface) {
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
        NotificationModel notification = getItem(position);
        holder.bind(notification);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView notificationProfile;
        TextView notificationDisplayName;
        TextView notificationText;
        LinearLayout requestButtons;
        RelativeLayout notificationLayout;
        LinearLayout acceptRequest;
        LinearLayout rejectRequest;

        TextView txtAccept;
        TextView txtReject;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationProfile = itemView.findViewById(R.id.notificationProfile);
            notificationDisplayName = itemView.findViewById(R.id.notificationDisplayName);
            notificationText = itemView.findViewById(R.id.notificationText);
            requestButtons = itemView.findViewById(R.id.requestButtons);
            notificationLayout = itemView.findViewById(R.id.notification);
            acceptRequest = itemView.findViewById(R.id.acceptRequest);
            rejectRequest = itemView.findViewById(R.id.rejectRequest);
            txtAccept = itemView.findViewById(R.id.txtAccept);
            txtReject = itemView.findViewById(R.id.txtReject);
        }

        public void bind(NotificationModel notification) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("userInformation").document(notification.getFromUserId());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        notificationDisplayName.setText(documentSnapshot.getString("displayName"));
                        Glide.with(itemView.getContext()).load(documentSnapshot.getString("profileUrl")).into(notificationProfile);
                    }
                }
            });

            if (notification.getNotificationType() == NotificationModel.FOLLOW_NOTIFICATION) {
                notificationText.setText(" is following you.");
            } else if (notification.getNotificationType() == NotificationModel.COMMENT_NOTIFICATION) {
                notificationText.setText(" commented on your post.");
                requestButtons.setVisibility(View.INVISIBLE);
            } else if (notification.getNotificationType() == NotificationModel.LIKE_NOTIFICATION) {
                notificationText.setText("like your post");
                requestButtons.setVisibility(View.INVISIBLE);
            } else if (notification.getNotificationType() == NotificationModel.ACCEPTED_NOTIFICATION) {
                rejectRequest.setVisibility(View.GONE);
                txtAccept.setText("Followed");
            } else if(notification.getNotificationType() == NotificationModel.REJECTED_NOTIFICATION){
                acceptRequest.setVisibility(View.GONE);
                txtReject.setText("Ignored");
            } else {
                notificationText.setText(" followed you back.");
                acceptRequest.setVisibility(View.GONE);
                rejectRequest.setVisibility(View.GONE);
            }

            acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                //update notification to accepted
                                FirebaseOperations.updateNotification(notification.getNotificationId(), NotificationModel.ACCEPTED_NOTIFICATION, db);
                                rejectRequest.setVisibility(View.GONE);
                                txtAccept.setText("Followed");
                                //add sender to current users following
                                FirebaseOperations.addFollow(user.getUid(), notification.getFromUserId(), documentSnapshot.getString("displayName"), NotificationModel.FOLLOWINGS, db);
                                //add current user to senders follower
                                FirebaseOperations.addFollow(notification.getFromUserId(), user.getUid(), user.getDisplayName(), NotificationModel.FOLLOWERS, db);

                                //add followed back notification to sender
                                NotificationModel newNotification = new NotificationModel(notification.getFromUserId(), user.getUid(), NotificationModel.FOLLOWED_BACK_NOTIFICATION);
                                FirebaseOperations.addNotification(newNotification,db);
                            }
                        }
                    });

                }
            });

            rejectRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update notification to rejected
                    FirebaseOperations.updateNotification(notification.getNotificationId(), NotificationModel.REJECTED_NOTIFICATION, db);
                    acceptRequest.setVisibility(View.GONE);
                    txtReject.setText("Ignored");
                }
            });

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

        public void onNotificationClick(NotificationModel notification);
    }
}
