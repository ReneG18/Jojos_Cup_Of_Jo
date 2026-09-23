package com.example.jojos_cup_of_jo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jojos_cup_of_jo.data.CartRepository;
import com.example.jojos_cup_of_jo.databinding.ActivityMainBinding;
import com.example.jojos_cup_of_jo.model.ProductType;
import com.example.jojos_cup_of_jo.ui.TabHost;
import com.example.jojos_cup_of_jo.ui.cart.CartFragment;
import com.example.jojos_cup_of_jo.ui.home.HomeFragment;
import com.example.jojos_cup_of_jo.ui.product.ProductListFragment;
import com.example.jojos_cup_of_jo.ui.team.TeamFragment;

import com.google.android.material.badge.BadgeDrawable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity implements TabHost {

    /** One entry per bottom-nav tab; order here is also the fragment-add order. */
    private static final Map<Integer, Supplier<Fragment>> TAB_FRAGMENT_FACTORIES = new LinkedHashMap<>();

    static {
        TAB_FRAGMENT_FACTORIES.put(R.id.nav_home, HomeFragment::new);
        TAB_FRAGMENT_FACTORIES.put(R.id.nav_menu, () -> ProductListFragment.newInstance(ProductType.MENU));
        TAB_FRAGMENT_FACTORIES.put(R.id.nav_merch, () -> ProductListFragment.newInstance(ProductType.MERCH));
        TAB_FRAGMENT_FACTORIES.put(R.id.nav_team, TeamFragment::new);
        TAB_FRAGMENT_FACTORIES.put(R.id.nav_cart, CartFragment::new);
    }

    private ActivityMainBinding binding;
    private final Map<Integer, Fragment> tabFragments = new LinkedHashMap<>();
    private Fragment activeFragment;

    private final CartRepository.CartListener cartListener = this::updateCartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpFragments(savedInstanceState);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment target = tabFragments.get(item.getItemId());
            if (target != null) {
                showFragment(target);
                return true;
            }
            return false;
        });

        CartRepository.getInstance().addListener(cartListener);
        updateCartBadge();
    }

    private void setUpFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction =
                savedInstanceState == null ? fragmentManager.beginTransaction() : null;

        for (Map.Entry<Integer, Supplier<Fragment>> entry : TAB_FRAGMENT_FACTORIES.entrySet()) {
            int tabId = entry.getKey();
            String tag = String.valueOf(tabId);

            Fragment fragment = savedInstanceState != null
                    ? fragmentManager.findFragmentByTag(tag)
                    : entry.getValue().get();
            tabFragments.put(tabId, fragment);

            if (transaction != null) {
                transaction.add(R.id.fragment_container, fragment, tag);
                if (tabId != R.id.nav_home) {
                    transaction.hide(fragment);
                }
            }
        }

        if (transaction != null) {
            transaction.commit();
        }

        activeFragment = savedInstanceState == null
                ? tabFragments.get(R.id.nav_home)
                : findVisibleFragment();
    }

    private Fragment findVisibleFragment() {
        for (Fragment fragment : tabFragments.values()) {
            if (!fragment.isHidden()) {
                return fragment;
            }
        }
        return tabFragments.get(R.id.nav_home);
    }

    private void showFragment(Fragment target) {
        if (target == activeFragment) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .hide(activeFragment)
                .show(target)
                .commit();
        activeFragment = target;
    }

    /** Lets a Fragment (Home's dropdown, Cart's empty-state CTA) switch tabs without duplicating nav logic. */
    @Override
    public void selectTab(int tabId) {
        binding.bottomNavigation.setSelectedItemId(tabId);
    }

    private void updateCartBadge() {
        int count = CartRepository.getInstance().getTotalItemCount();
        BadgeDrawable badge = binding.bottomNavigation.getOrCreateBadge(R.id.nav_cart);
        if (count == 0) {
            badge.setVisible(false);
        } else {
            badge.setVisible(true);
            badge.setNumber(count);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CartRepository.getInstance().removeListener(cartListener);
    }
}
