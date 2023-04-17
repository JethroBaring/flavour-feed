package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.finalproject.flavourfeed.R;

public class PostPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page);
        ImageView btnClosePost = findViewById(R.id.btnClosePost);

        btnClosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostPage.super.onBackPressed();
            }
        });
    }
}