package com.finalproject.flavourfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProductPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    ImageView productPicture;
    TextInputEditText txtInptNewDisplayName;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    FirebaseAuth mAuth;

    FirebaseFirestore db;
    DocumentReference postRef;
    String imageUrl;
    ImageView cameraIcon;

    EditText productName;
    EditText productPrice;

    Spinner productCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_page);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        productPicture = findViewById(R.id.productPicture);
        ImageView btnAddProduct = findViewById(R.id.btnAddProduct);
        RelativeLayout productPictureContainer = findViewById(R.id.productPictureContainer);
        cameraIcon = findViewById(R.id.cameraIcon);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productCategory = findViewById(R.id.productCategory);
        String [] categories = new String[]{"Main Dishes", "Salads","Desserts","Drinks"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        productCategory.setAdapter(adapter);

        ImageView btnClose = findViewById(R.id.btnClose);

        productPictureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductPage.super.onBackPressed();
            }
        });


        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            productPicture.setImageURI(imageUri);
            cameraIcon.setVisibility(View.INVISIBLE);
        } else if(imageUri == null){
            cameraIcon.setVisibility(View.VISIBLE);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveChanges() {
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child("product/" + UUID.randomUUID().toString());

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = fileReference.getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db = FirebaseFirestore.getInstance();
                                    Map<String, Object> newProduct = new HashMap<>();
                                    newProduct.put("photoUrl", uri.toString());
                                    newProduct.put("name", productName.getText().toString());
                                    newProduct.put("price", Integer.parseInt(productPrice.getText().toString()));
                                    newProduct.put("timestamp", FieldValue.serverTimestamp());
                                    newProduct.put("category", productCategory.getSelectedItem());
                                    newProduct.put("sellerId", user.getUid());
                                    db.collection("storeInformation").document(user.getUid()).collection("products").add(newProduct).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String productId = documentReference.getId();
                                            db.collection("storeInformation").document(user.getUid()).collection("products").document(productId).update("productId",productId);
                                            Snackbar.make(findViewById(android.R.id.content), "Success", Snackbar.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), MyStorePage.class));
                                            newProduct.put("productId", productId);
                                            db.collection("allProducts").document(productId).set(newProduct);
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(findViewById(android.R.id.content), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
        }
    }
}