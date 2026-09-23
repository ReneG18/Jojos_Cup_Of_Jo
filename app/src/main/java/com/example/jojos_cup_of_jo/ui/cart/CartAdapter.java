package com.example.jojos_cup_of_jo.ui.cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jojos_cup_of_jo.databinding.ItemCartProductBinding;
import com.example.jojos_cup_of_jo.model.CartItem;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartRowActionListener {
        void onIncrement(CartItem item);

        void onDecrement(CartItem item);

        void onRemove(CartItem item);
    }

    private final List<CartItem> items;
    private final OnCartRowActionListener listener;

    public CartAdapter(List<CartItem> items, OnCartRowActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartProductBinding binding = ItemCartProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        private final ItemCartProductBinding binding;

        CartViewHolder(ItemCartProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CartItem item, OnCartRowActionListener listener) {
            binding.cartItemName.setText(item.getProduct().getName());
            binding.cartItemUnitPrice.setText(
                    String.format(Locale.US, "$%.2f each", item.getProduct().getPrice()));
            binding.cartItemQuantity.setText(String.valueOf(item.getQuantity()));
            binding.cartItemLineTotal.setText(
                    String.format(Locale.US, "$%.2f", item.getLineTotal()));

            binding.cartItemIncrement.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIncrement(item);
                }
            });
            binding.cartItemDecrement.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDecrement(item);
                }
            });
            binding.cartItemDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemove(item);
                }
            });
        }
    }
}
