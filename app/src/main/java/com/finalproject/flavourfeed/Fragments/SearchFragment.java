package com.finalproject.flavourfeed.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.finalproject.flavourfeed.Adapters.PostAdapter;
import com.finalproject.flavourfeed.Adapters.SearchAdapter;
import com.finalproject.flavourfeed.Entity.PostEntity;
import com.finalproject.flavourfeed.Entity.ResultEntity;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    FirebaseFirestore db;
    List<ResultEntity> results;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        EditText search = view.findViewById(R.id.search);
        RecyclerView searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        TextView notFound = view.findViewById(R.id.notFound);
        db = FirebaseFirestore.getInstance();
        results = new ArrayList<>();
        CollectionReference usersRef = db.collection("userInformation");
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH  || keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    String toBeSearch = search.getText().toString();
                    if(!toBeSearch.isEmpty()) {

                        Query query = usersRef.orderBy("displayName").startAt(toBeSearch).endAt(toBeSearch + "\uf8ff").limit(10);

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    results.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String userId = document.getString("userId");
                                        results.add(new ResultEntity(userId));
                                    }
                                    search.setText("");
                                    searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    searchRecyclerView.setAdapter(new SearchAdapter(getContext(), results));
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
        return view;
    }
}