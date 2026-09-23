package com.example.jojos_cup_of_jo.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jojos_cup_of_jo.R;
import com.example.jojos_cup_of_jo.data.SampleDataProvider;
import com.example.jojos_cup_of_jo.databinding.FragmentHomeBinding;
import com.example.jojos_cup_of_jo.databinding.ItemProductCompactBinding;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.model.StoreInfo;
import com.example.jojos_cup_of_jo.ui.TabHost;
import com.example.jojos_cup_of_jo.ui.util.PlaceholderStyle;

import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TabHost tabHost;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TabHost) {
            tabHost = (TabHost) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StoreInfo storeInfo = SampleDataProvider.getStoreInfo();
        bindAboutUsAndStory(storeInfo);
        bindHoursAndLocation(storeInfo);
        bindThankYouNote(storeInfo);

        populatePreviewStrip(binding.menuPreviewContainer, SampleDataProvider.getMenuProducts(),
                4, R.id.nav_menu);
        populatePreviewStrip(binding.merchPreviewContainer, SampleDataProvider.getMerchProducts(),
                4, R.id.nav_merch);

        binding.viewFullMenuButton.setOnClickListener(v -> requestTab(R.id.nav_menu));
        binding.shopMerchButton.setOnClickListener(v -> requestTab(R.id.nav_merch));

        binding.exploreButton.setOnClickListener(this::showExploreMenu);
    }

    private void bindAboutUsAndStory(StoreInfo storeInfo) {
        binding.aboutUsBlurb.setText(storeInfo.getAboutUsBlurb());
        binding.storeStoryBlurb.setText(storeInfo.getStoryBlurb());
    }

    private void bindHoursAndLocation(StoreInfo storeInfo) {
        binding.storeAddress.setText(storeInfo.getAddress());
        binding.storePhone.setText(storeInfo.getPhone());
        binding.storeHoursContainer.removeAllViews();
        for (String hoursLine : storeInfo.getHours()) {
            TextView row = new TextView(requireContext());
            row.setText(hoursLine);
            row.setTextAppearance(R.style.TextAppearance_Jojo_Body);
            binding.storeHoursContainer.addView(row);
        }
    }

    private void bindThankYouNote(StoreInfo storeInfo) {
        binding.thankYouNote.setText(storeInfo.getThankYouNote());
    }

    private void populatePreviewStrip(ViewGroup container, List<Product> products, int limit,
                                       int targetTabId) {
        container.removeAllViews();
        int count = Math.min(limit, products.size());
        for (int i = 0; i < count; i++) {
            Product product = products.get(i);
            ItemProductCompactBinding itemBinding = ItemProductCompactBinding.inflate(
                    LayoutInflater.from(requireContext()), container, false);

            itemBinding.compactName.setText(product.getName());
            itemBinding.compactPrice.setText(
                    String.format(Locale.US, "$%.2f", product.getPrice()));
            itemBinding.compactInitials.setText(PlaceholderStyle.initialsFor(product.getName()));
            PlaceholderStyle.applySwatchTint(itemBinding.compactSwatch, product.getSwatchIndex());

            itemBinding.getRoot().setOnClickListener(v -> requestTab(targetTabId));

            container.addView(itemBinding.getRoot());
        }
    }

    private void showExploreMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.inflate(R.menu.menu_home_dropdown);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.dropdown_about_us) {
                scrollToAboutUs();
                return true;
            } else if (itemId == R.id.dropdown_menu) {
                requestTab(R.id.nav_menu);
                return true;
            } else if (itemId == R.id.dropdown_team) {
                requestTab(R.id.nav_team);
                return true;
            } else if (itemId == R.id.dropdown_merch) {
                requestTab(R.id.nav_merch);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void scrollToAboutUs() {
        if (binding == null) {
            return;
        }
        binding.homeScrollView.post(() ->
                binding.homeScrollView.smoothScrollTo(0, binding.sectionAboutUs.getTop()));
    }

    private void requestTab(int tabId) {
        if (tabHost != null) {
            tabHost.selectTab(tabId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
