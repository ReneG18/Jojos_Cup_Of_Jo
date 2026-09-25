package com.example.jojos_cup_of_jo.data;

import com.example.jojos_cup_of_jo.model.CartItem;
import com.example.jojos_cup_of_jo.model.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory cart state shared across Menu/Merch/Cart screens for this UI-shell pass. Has no
 * android.* dependency so it (and Product/CartItem) can be unit tested directly.
 *
 * App code reaches this through {@link #getInstance()}; tests should construct their own
 * instance via {@code new CartRepository()} to avoid state bleeding between tests.
 */
public class CartRepository {

    public interface CartListener {
        void onCartChanged();
    }

    private static final CartRepository INSTANCE = new CartRepository();

    public static CartRepository getInstance() {
        return INSTANCE;
    }

    private static final double TAX_RATE = 0.0825;

    private final Map<String, CartItem> itemsByProductId = new LinkedHashMap<>();
    private final List<CartListener> listeners = new ArrayList<>();
    private int nextOrderNumber = 1001;

    public CartRepository() {
    }

    public void addItem(Product product) {
        CartItem existing = itemsByProductId.get(product.getId());
        if (existing == null) {
            itemsByProductId.put(product.getId(), new CartItem(product, 1));
        } else {
            existing.setQuantity(existing.getQuantity() + 1);
        }
        notifyListeners();
    }

    public void removeOneItem(String productId) {
        CartItem existing = itemsByProductId.get(productId);
        if (existing == null) {
            return;
        }
        if (existing.getQuantity() <= 1) {
            itemsByProductId.remove(productId);
        } else {
            existing.setQuantity(existing.getQuantity() - 1);
        }
        notifyListeners();
    }

    public void removeItemCompletely(String productId) {
        if (itemsByProductId.remove(productId) != null) {
            notifyListeners();
        }
    }

    public void clear() {
        itemsByProductId.clear();
        notifyListeners();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(itemsByProductId.values());
    }

    public int getTotalItemCount() {
        int total = 0;
        for (CartItem item : itemsByProductId.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getSubtotal() {
        double total = 0;
        for (CartItem item : itemsByProductId.values()) {
            total += item.getLineTotal();
        }
        return total;
    }

    public double getTax() {
        return getSubtotal() * TAX_RATE;
    }

    public double getTotal() {
        return getSubtotal() + getTax();
    }

    public int confirmOrderAndGetOrderNumber() {
        int orderNumber = nextOrderNumber++;
        clear();
        return orderNumber;
    }

    public void addListener(CartListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CartListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (CartListener listener : new ArrayList<>(listeners)) {
            listener.onCartChanged();
        }
    }
}
