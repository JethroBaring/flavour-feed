package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.finalproject.flavourfeed.Adapters.ChatSearchAdapter;
import com.finalproject.flavourfeed.Models.ResultModel;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatSearchPage extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;
    List<ResultModel> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_search_page);
        EditText search = findViewById(R.id.search);
        RecyclerView searchRecyclerView = findViewById(R.id.searchRecyclerView);
        TextView notFound = findViewById(R.id.notFound);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        results = new ArrayList<>();
        CollectionReference usersRef = db.collection("userInformation");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        search.requestFocus();
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    String toBeSearch = search.getText().toString();
                    if (!toBeSearch.isEmpty()) {

                        Query query = usersRef.orderBy("displayName").startAt(toBeSearch).endAt(toBeSearch + "\uf8ff").limit(10);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    results.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String userId = document.getString("userId");
                                        if(!userId.equals(user.getUid()))
                                            results.add(new ResultModel(userId));
                                    }
                                    search.setText("");
                                    searchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    searchRecyclerView.setAdapter(new ChatSearchAdapter(getApplicationContext(), results, new ChatSearchAdapter.ChatSearchClickInterface() {
                                        @Override
                                        public void onSearchResultClick(String otherUserId) {
                                            Intent intent = new Intent(getApplicationContext(), MessagePage.class);
                                            intent.putExtra("otherUserId", otherUserId);
                                            startActivity(intent);
                                        }
                                    }));
                                } else {
                                    notFound.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
                return false;
            }
        });
    }
}