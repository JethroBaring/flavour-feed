package com.finalproject.flavourfeed.Utitilies;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class MyBaseFragment extends Fragment {
    protected boolean isActive = true;

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
        updateUserStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
        updateUserStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
        updateUserStatus();
    }

    private void updateUserStatus() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("userInformation").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .update("active", isActive);
        }
    }
}
