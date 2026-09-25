package com.example.jojos_cup_of_jo.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jojos_cup_of_jo.R;
import com.example.jojos_cup_of_jo.data.SampleDataProvider;
import com.example.jojos_cup_of_jo.databinding.FragmentHomeBinding;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.model.StoreInfo;
import com.example.jojos_cup_of_jo.ui.TabHost;
import com.example.jojos_cup_of_jo.ui.util.ProductArt;

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

        bindSeasonalDrink(SampleDataProvider.getSeasonalDrink());
        bindSeasonalMerchItem(SampleDataProvider.getSeasonalMerchItem());
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

    private void bindSeasonalDrink(@Nullable Product product) {
        if (product == null) {
            binding.sectionSeasonalDrink.setVisibility(View.GONE);
            return;
        }
        binding.seasonalDrinkName.setText(product.getName());
        binding.seasonalDrinkDescription.setText(product.getDescription());
        binding.seasonalDrinkImage.setImageResource(ProductArt.photoRes(product.getId()));
        binding.seasonalDrinkCard.setOnClickListener(v -> requestTab(R.id.nav_menu));
    }

    private void bindSeasonalMerchItem(@Nullable Product product) {
        if (product == null) {
            binding.sectionSeasonalMerch.setVisibility(View.GONE);
            return;
        }
        binding.seasonalMerchName.setText(product.getName());
        binding.seasonalMerchDescription.setText(product.getDescription());
        binding.seasonalMerchImage.setImageResource(ProductArt.photoRes(product.getId()));
        binding.seasonalMerchCard.setOnClickListener(v -> requestTab(R.id.nav_merch));
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
