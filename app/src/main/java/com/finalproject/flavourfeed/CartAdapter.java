package com.finalproject.flavourfeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CartAdapter extends ListAdapter<CartItemModel, CartAdapter.CartViewHolder> {
    FirebaseFirestore db;
    FirebaseUser user;

    CartCheckInterface cartCheckInterface;
    public CartAdapter(@NonNull DiffUtil.ItemCallback<CartItemModel> diffCallback, CartCheckInterface cartCheckInterface) {
        super(diffCallback);
        this.cartCheckInterface = cartCheckInterface;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemModel cartItemModel = getItem(position);
        holder.bind(cartItemModel);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        EditText quantity;
        ImageView decrementQuantity;
        ImageView incrementQuantity;
        ImageView productPicture;
        CheckBox checkBox;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            decrementQuantity = itemView.findViewById(R.id.decrementQuantity);
            incrementQuantity = itemView.findViewById(R.id.incrementQuantity);
            productPicture = itemView.findViewById(R.id.productPicture);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public void bind(CartItemModel cartItemModel) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            db = FirebaseFirestore.getInstance();
            db.collection("allProducts").document(cartItemModel.getProductId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        productName.setText(snapshot.getString("name"));
                        productPrice.setText(Integer.toString(snapshot.getLong("price").intValue()));
                        Glide.with(itemView.getContext()).load(snapshot.getString("photoUrl")).into(productPicture);
                    }
                }
            });
            quantity.setText(Integer.toString(cartItemModel.getQuantity()));
            decrementQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("userInformation").document(user.getUid()).collection("cart").document(cartItemModel.getCartItemId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();
                                int newQ = snapshot.getLong("quantity").intValue() - 1;
                                if (newQ >= 1) {
                                    quantity.setText(Integer.toString(newQ));
                                    db.collection("userInformation").document(user.getUid()).collection("cart").document(cartItemModel.getCartItemId()).update("quantity", newQ);
                                }
                            }
                        }
                    });
                }
            });
            incrementQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.collection("userInformation").document(user.getUid()).collection("cart").document(cartItemModel.getCartItemId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();
                                int newQ = snapshot.getLong("quantity").intValue() + 1;
                                quantity.setText(Integer.toString(newQ));
                                db.collection("userInformation").document(user.getUid()).collection("cart").document(cartItemModel.getCartItemId()).update("quantity", newQ);

                            }
                        }
                    });
                }
            });

            cartCheckInterface.onItemCheck(checkBox, cartItemModel);
        }
    }

    public interface CartCheckInterface {
        public void onItemCheck(CheckBox checkBox, CartItemModel cartItemModel);
    }
}
