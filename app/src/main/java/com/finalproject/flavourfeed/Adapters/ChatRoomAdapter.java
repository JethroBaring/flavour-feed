package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Models.ChatRoomModel;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.transition.Hold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Arrays;


public class ChatRoomAdapter extends ListAdapter<ChatRoomModel, ChatRoomAdapter.ChatRoomViewHolder> {
    FirebaseUser user;
    ChatRoomInterface chatRoomInterface;
    public ChatRoomAdapter(@NonNull DiffUtil.ItemCallback<ChatRoomModel> diffCallback, ChatRoomInterface chatRoomInterface) {
        super(diffCallback);
        this.chatRoomInterface = chatRoomInterface;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomAdapter.ChatRoomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoomModel chatRoomModel = getItem(position);
        holder.bind(chatRoomModel);
    }


    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        ImageView chatRoomProfile;
        TextView chatRoomDisplayName;
        TextView lastModified;
        TextView lastMessage;

        CardView chatRoom;
        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            chatRoomProfile = itemView.findViewById(R.id.chatRoomProfile);
            chatRoomDisplayName = itemView.findViewById(R.id.chatRoomDisplayName);
            lastModified = itemView.findViewById(R.id.lastModified);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            chatRoom = itemView.findViewById(R.id.chatRoom);
        }

        public void bind(ChatRoomModel chatRoomModel) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();

            String[] ids = {user.getUid(), chatRoomModel.getOtherUserId()};
            Arrays.sort(ids);
            String chatRoomId = String.join("_", ids);

            chatRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatRoomInterface.onChatClick(chatRoomModel.otherUserId);
                }
            });

            db.collection("userInformation").document(chatRoomModel.getOtherUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        Glide.with(itemView.getContext()).load(snapshot.getString("profileUrl")).into(chatRoomProfile);
                        chatRoomDisplayName.setText(snapshot.getString("displayName"));
                    }
                }
            });

            db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        ChatRoomModel chatRoom = snapshot.toObject(ChatRoomModel.class);
                        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                        lastModified.setText(formatter.format(chatRoom.getLastModified()));
                    }
                }
            });

            db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).collection("messages").orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (!snapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = snapshot.getDocuments().get(0);
                            if (documentSnapshot.getString("senderId").equals(user.getUid())) {
                                String lmessage = String.format("You: %s", documentSnapshot.getString("message"));
                                lastMessage.setText(lmessage);
                            } else {
                                lastMessage.setText(documentSnapshot.getString("message"));
                            }

                        }
                    }
                }
            });
        }
    }

    public interface ChatRoomInterface {
        public void onChatClick(String otherUserId);
    }
}
