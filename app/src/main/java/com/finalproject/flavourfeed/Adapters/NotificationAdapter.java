package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Entity.NotificationEntity;
import com.finalproject.flavourfeed.R;

import org.w3c.dom.Text;

import java.util.zip.Inflater;


public class NotificationAdapter extends ListAdapter<NotificationEntity,NotificationAdapter.NotificationViewHolder> {
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
        TextView displayName;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.searchDisplayName);
        }

        public void bind(NotificationEntity notification) {
            if(notification.getNotificationType() == 1) {
                displayName.setText("add");
            } else {
                displayName.setText("comment");
            }
        }
    }

    public interface NotificationClickInterface {
        public void onDelete(int pos);
    }
}
