package com.prashant.zomatov2.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.prashant.zomatov2.R;
import com.prashant.zomatov2.databinding.ActivityDashboardBinding;
import com.prashant.zomatov2.ui.fragment.AccountFragment;
import com.prashant.zomatov2.ui.fragment.HistoryFragment;
import com.prashant.zomatov2.ui.fragment.HomeFragment;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == bind.bottomNavigation.getMenu().findItem(R.id.menu_home).getItemId()) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == bind.bottomNavigation.getMenu().findItem(R.id.menu_history).getItemId()) {
                selectedFragment = new HistoryFragment();
            } else if (item.getItemId() == bind.bottomNavigation.getMenu().findItem(R.id.menu_account).getItemId()) {
                selectedFragment = new AccountFragment();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

            if (currentFragment != null && selectedFragment != null && currentFragment.getClass().equals(selectedFragment.getClass())) {
                return true;
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (selectedFragment != null) {
                transaction.replace(R.id.fragment_container, selectedFragment);
            } else {
                transaction.replace(R.id.fragment_container, new HomeFragment());
            }
            transaction.commitAllowingStateLoss();


            return true;
        });

        bind.bottomNavigation.setSelectedItemId(R.id.menu_home);
    }

}
