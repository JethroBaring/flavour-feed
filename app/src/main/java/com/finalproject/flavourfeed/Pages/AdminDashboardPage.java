package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.finalproject.flavourfeed.Fragments.AdminThemeFragment;
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
    AdminThemeFragment adminTheme = new AdminThemeFragment();

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
        LinearLayout theme = findViewById(R.id.theme);

        changeFragment(adminDashboardFragment);

        menu.setOnClickListener(View -> openDrawer(drawerLayout));
        dashboard.setOnClickListener(this::onClick);
        users.setOnClickListener(this::onClick);
        products.setOnClickListener(this::onClick);
        orders.setOnClickListener(this::onClick);
        theme.setOnClickListener(this::onClick);
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
                changeFragment(adminDashboardFragment);
                break;
            case R.id.users:
                changeFragment(adminUsersFragment);
                break;
            case R.id.products:
                changeFragment(adminProductsFragment);
                break;
            case R.id.orders:
                changeFragment(adminOrdersFragment);
                break;
            case R.id.theme:
                changeFragment(adminTheme);
                break;

        }
    }

    public void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer,fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
