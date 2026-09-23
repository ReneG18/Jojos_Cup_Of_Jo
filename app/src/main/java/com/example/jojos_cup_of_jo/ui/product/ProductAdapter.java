package com.example.jojos_cup_of_jo.ui.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jojos_cup_of_jo.databinding.ItemProductBinding;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.ui.util.PlaceholderStyle;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }

    private final List<Product> products;
    private final OnAddToCartListener listener;

    public ProductAdapter(List<Product> products, OnAddToCartListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductBinding binding;

        ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product, OnAddToCartListener listener) {
            binding.productName.setText(product.getName());
            binding.productDescription.setText(product.getDescription());
            binding.productPrice.setText(
                    String.format(Locale.US, "$%.2f", product.getPrice()));
            binding.productInitials.setText(PlaceholderStyle.initialsFor(product.getName()));
            PlaceholderStyle.applySwatchTint(binding.productSwatch, product.getSwatchIndex());

            binding.addToCartButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCart(product);
                }
            });
        }
    }
}
