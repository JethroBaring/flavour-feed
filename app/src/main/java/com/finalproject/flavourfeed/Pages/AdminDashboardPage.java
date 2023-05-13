package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.finalproject.flavourfeed.Fragments.AdminDashboardFragment;
import com.finalproject.flavourfeed.Fragments.AdminOrdersFragment;
import com.finalproject.flavourfeed.Fragments.AdminProductsFragment;
import com.finalproject.flavourfeed.Fragments.AdminUsersFragment;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDashboardPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;

    DrawerLayout drawerLayout;
    AdminDashboardFragment adminDashboardFragment = new AdminDashboardFragment();
    AdminUsersFragment adminUsersFragment = new AdminUsersFragment();
    AdminProductsFragment adminProductsFragment = new AdminProductsFragment();
    AdminOrdersFragment adminOrdersFragment = new AdminOrdersFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard_page);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ImageView menu = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawerLayout);
        LinearLayout dashboard = findViewById(R.id.dashboard);
        LinearLayout users = findViewById(R.id.users);
        LinearLayout products = findViewById(R.id.products);
        LinearLayout orders = findViewById(R.id.orders);
        LinearLayout logout = findViewById(R.id.logout);

        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, adminDashboardFragment).commit();
        menu.setOnClickListener(View -> {
            openDrawer(drawerLayout);
        });
        dashboard.setOnClickListener(view -> {
            onClick(dashboard);
        });
        users.setOnClickListener(view -> {
            onClick(users);
        });
        products.setOnClickListener(view -> {
            onClick(products);
        });
        orders.setOnClickListener(view -> {
            onClick(orders);
        });
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LogInPage.class));
        });

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("userInformation").document(user.getUid()).update("active", false);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, adminDashboardFragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.users:
                getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, adminUsersFragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.products:
                getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, adminProductsFragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, adminOrdersFragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
    }
}
