package com.example.jojos_cup_of_jo.ui.cart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jojos_cup_of_jo.R;
import com.example.jojos_cup_of_jo.data.CartRepository;
import com.example.jojos_cup_of_jo.data.SampleDataProvider;
import com.example.jojos_cup_of_jo.databinding.FragmentCartBinding;
import com.example.jojos_cup_of_jo.model.CartItem;
import com.example.jojos_cup_of_jo.model.StoreInfo;
import com.example.jojos_cup_of_jo.ui.TabHost;

import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private TabHost tabHost;
    private boolean showingConfirmation = false;

    private final CartRepository.CartListener cartListener = this::render;

    private final CartAdapter.OnCartRowActionListener rowActionListener =
            new CartAdapter.OnCartRowActionListener() {
                @Override
                public void onIncrement(CartItem item) {
                    CartRepository.getInstance().addItem(item.getProduct());
                }

                @Override
                public void onDecrement(CartItem item) {
                    CartRepository.getInstance().removeOneItem(item.getProduct().getId());
                }

                @Override
                public void onRemove(CartItem item) {
                    CartRepository.getInstance().removeItemCompletely(item.getProduct().getId());
                }
            };

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
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        StoreInfo storeInfo = SampleDataProvider.getStoreInfo();
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, storeInfo.getPickupLocations());
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cartPickupLocationSpinner.setAdapter(locationAdapter);

        binding.browseMenuButton.setOnClickListener(v -> {
            if (tabHost != null) {
                tabHost.selectTab(R.id.nav_menu);
            }
        });

        binding.confirmOrderButton.setOnClickListener(v -> {
            CartRepository repository = CartRepository.getInstance();
            int itemCount = repository.getTotalItemCount();
            String pickupTimeLabel = PickupTimeEstimator.estimateReadyTimeLabel();
            Object selectedLocation = binding.cartPickupLocationSpinner.getSelectedItem();
            String location = selectedLocation != null ? selectedLocation.toString() : "";

            showingConfirmation = true;
            int orderNumber = repository.confirmOrderAndGetOrderNumber();

            binding.confirmedOrderNumber.setText(getString(R.string.order_number_format, orderNumber));
            binding.confirmedRecap.setText(String.format(Locale.US, "%d item(s) • %s • %s",
                    itemCount, pickupTimeLabel, location));
            render();
        });

        binding.startNewOrderButton.setOnClickListener(v -> {
            showingConfirmation = false;
            render();
        });

        CartRepository.getInstance().addListener(cartListener);
        render();
    }

    private void render() {
        if (binding == null) {
            return;
        }
        if (showingConfirmation) {
            showOnly(binding.cartConfirmedGroup);
            return;
        }

        List<CartItem> items = CartRepository.getInstance().getItems();
        if (items.isEmpty()) {
            showOnly(binding.cartEmptyGroup);
            return;
        }

        showOnly(binding.cartContentGroup);
        binding.cartRecyclerView.setAdapter(new CartAdapter(items, rowActionListener));
        CartRepository repository = CartRepository.getInstance();
        binding.cartSubtotal.setText(String.format(Locale.US, "$%.2f", repository.getSubtotal()));
        binding.cartTax.setText(String.format(Locale.US, "$%.2f", repository.getTax()));
        binding.cartTotal.setText(String.format(Locale.US, "$%.2f", repository.getTotal()));
        binding.cartPickupTime.setText(PickupTimeEstimator.estimateReadyTimeLabel());
    }

    private void showOnly(View toShow) {
        binding.cartEmptyGroup.setVisibility(toShow == binding.cartEmptyGroup ? View.VISIBLE : View.GONE);
        binding.cartContentGroup.setVisibility(toShow == binding.cartContentGroup ? View.VISIBLE : View.GONE);
        binding.cartConfirmedGroup.setVisibility(toShow == binding.cartConfirmedGroup ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CartRepository.getInstance().removeListener(cartListener);
        binding = null;
    }
}
