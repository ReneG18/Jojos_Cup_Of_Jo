package com.example.jojos_cup_of_jo.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jojos_cup_of_jo.R;
import com.example.jojos_cup_of_jo.data.CartRepository;
import com.example.jojos_cup_of_jo.data.SampleDataProvider;
import com.example.jojos_cup_of_jo.databinding.FragmentProductListBinding;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.model.ProductType;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/** Renders either the Menu or Merch product list, decided by {@link #newInstance}'s argument. */
public class ProductListFragment extends Fragment {

    private static final String ARG_PRODUCT_TYPE = "product_type";

    public static ProductListFragment newInstance(ProductType productType) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_TYPE, productType.name());
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentProductListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProductType productType = ProductType.valueOf(requireArguments().getString(ARG_PRODUCT_TYPE));
        List<Product> products = productType == ProductType.MENU
                ? SampleDataProvider.getMenuProducts()
                : SampleDataProvider.getMerchProducts();

        binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.productRecyclerView.setAdapter(new ProductAdapter(products, product -> {
            CartRepository.getInstance().addItem(product);
            Snackbar.make(binding.getRoot(),
                    getString(R.string.added_to_cart_format, product.getName()),
                    Snackbar.LENGTH_SHORT).show();
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
