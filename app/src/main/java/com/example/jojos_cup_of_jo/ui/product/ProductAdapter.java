package com.example.jojos_cup_of_jo.ui.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jojos_cup_of_jo.databinding.ItemProductBinding;
import com.example.jojos_cup_of_jo.databinding.ItemProductSectionHeaderBinding;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.ui.util.Money;
import com.example.jojos_cup_of_jo.ui.util.ProductArt;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }

    private final List<ProductListRow> rows;
    private final OnAddToCartListener listener;

    public ProductAdapter(List<ProductListRow> rows, OnAddToCartListener listener) {
        this.rows = rows;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ProductListRow.TYPE_HEADER) {
            return new HeaderViewHolder(
                    ItemProductSectionHeaderBinding.inflate(inflater, parent, false));
        }
        return new ProductViewHolder(ItemProductBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductListRow row = rows.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(row.getHeaderRes());
        } else {
            ((ProductViewHolder) holder).bind(row.getProduct(), listener);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductSectionHeaderBinding binding;

        HeaderViewHolder(ItemProductSectionHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int headerRes) {
            binding.sectionHeader.setText(headerRes);
        }
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
            binding.productPrice.setText(Money.format(product.getPrice()));
            binding.productImage.setImageResource(ProductArt.photoRes(product.getId()));

            binding.addToCartButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCart(product);
                }
            });
        }
    }
}
