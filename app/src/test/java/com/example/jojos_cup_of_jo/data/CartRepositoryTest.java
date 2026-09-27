package com.example.jojos_cup_of_jo.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.example.jojos_cup_of_jo.model.CartItem;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.model.ProductCategory;
import com.example.jojos_cup_of_jo.model.ProductType;
import com.example.jojos_cup_of_jo.ui.util.Money;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class CartRepositoryTest {

    private Product product(String id, double price) {
        return new Product(id, "Test Product " + id, "description", price,
                ProductType.MENU, ProductCategory.COFFEE, 0);
    }

    @Test
    public void addItem_newProduct_addsSingleRowWithQuantityOne() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 4.50);

        repository.addItem(product);

        List<CartItem> items = repository.getItems();
        assertEquals(1, items.size());
        assertEquals(1, repository.getTotalItemCount());
    }

    @Test
    public void addItem_sameProductTwice_incrementsQuantityInsteadOfDuplicatingRow() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 4.50);

        repository.addItem(product);
        repository.addItem(product);

        List<CartItem> items = repository.getItems();
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());
        assertEquals(2, repository.getTotalItemCount());
    }

    @Test
    public void getSubtotal_sumsLineTotalsAcrossDistinctProducts() {
        CartRepository repository = new CartRepository();
        Product productA = product("a", 4.50);
        Product productB = product("b", 3.25);

        repository.addItem(productA);
        repository.addItem(productB);
        repository.addItem(productB);

        assertEquals(new BigDecimal("11.00"), repository.getSubtotal());
    }

    @Test
    public void getTax_roundsToCentsHalfUp() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 10.00);

        repository.addItem(product);

        // 10.00 * 0.0825 = 0.825 exactly, which HALF_UP takes to 0.83.
        assertEquals(new BigDecimal("0.83"), repository.getTax());
    }

    @Test
    public void getTotal_isSubtotalPlusRoundedTax() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 10.00);

        repository.addItem(product);

        assertEquals(new BigDecimal("10.83"), repository.getTotal());
    }

    /**
     * Every amount leaving the repository is committed to whole cents. This is the property that
     * makes the figures safe to compare, store and add up across orders: an amount still carrying
     * a sub-cent fraction drifts once a day's takings are summed. Scale 2 is the assertion — a
     * dropped {@code setScale} would leave 0.309375 here instead of 0.31.
     */
    @Test
    public void moneyGetters_areCommittedToWholeCents() {
        CartRepository repository = new CartRepository();
        repository.addItem(product("a", 3.75));

        assertEquals(2, repository.getSubtotal().scale());
        assertEquals(2, repository.getTax().scale());
        assertEquals(2, repository.getTotal().scale());
        assertEquals(new BigDecimal("0.31"), repository.getTax());
    }

    /**
     * Guards the {@code BigDecimal.valueOf} in Product's constructor against
     * {@code new BigDecimal(price)}, which would store 3.99 as 3.99000000000000021316...
     *
     * Note the price here is deliberately not a multiple of $0.25. Every price in today's catalog
     * is — and quarters are exactly representable in binary — so a test using 4.50 passes either
     * way and guards nothing. The first odd-cent price added to the menu is what would expose it.
     */
    @Test
    public void productPrice_isTheExactDecimalLiteral_notItsBinaryExpansion() {
        assertEquals(new BigDecimal("3.99"), product("a", 3.99).getPrice());
    }

    @Test
    public void moneyFormat_printsTwoDecimalPlaces() {
        assertEquals("$4.50", Money.format(new BigDecimal("4.5")));
        assertEquals("$10.83", Money.format(new BigDecimal("10.83")));
    }

    @Test
    public void removeOneItem_atQuantityOne_removesRowEntirely() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 4.50);
        repository.addItem(product);

        repository.removeOneItem(product.getId());

        assertEquals(0, repository.getItems().size());
        assertEquals(0, repository.getTotalItemCount());
    }

    @Test
    public void removeOneItem_atQuantityGreaterThanOne_decrementsWithoutRemovingRow() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 4.50);
        repository.addItem(product);
        repository.addItem(product);

        repository.removeOneItem(product.getId());

        List<CartItem> items = repository.getItems();
        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getQuantity());
    }

    @Test
    public void removeItemCompletely_removesRowRegardlessOfQuantity() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 4.50);
        repository.addItem(product);
        repository.addItem(product);
        repository.addItem(product);

        repository.removeItemCompletely(product.getId());

        assertEquals(0, repository.getItems().size());
        assertEquals(0, repository.getTotalItemCount());
    }

    @Test
    public void clear_removesAllItemsRegardlessOfCount() {
        CartRepository repository = new CartRepository();
        repository.addItem(product("a", 4.50));
        repository.addItem(product("b", 3.25));

        repository.clear();

        assertEquals(0, repository.getItems().size());
        assertEquals(0, repository.getTotalItemCount());
    }

    @Test
    public void confirmOrderAndGetOrderNumber_startsAt1001AndIncrementsPerCall() {
        CartRepository repository = new CartRepository();

        int first = repository.confirmOrderAndGetOrderNumber();
        int second = repository.confirmOrderAndGetOrderNumber();

        assertEquals(1001, first);
        assertEquals(1002, second);
    }

    @Test
    public void confirmOrderAndGetOrderNumber_clearsTheCartAsASideEffect() {
        CartRepository repository = new CartRepository();
        repository.addItem(product("a", 4.50));

        repository.confirmOrderAndGetOrderNumber();

        assertEquals(0, repository.getItems().size());
    }

    @Test
    public void addListener_notifiesOnCartChange() {
        CartRepository repository = new CartRepository();
        int[] notificationCount = {0};
        repository.addListener(() -> notificationCount[0]++);

        repository.addItem(product("a", 4.50));

        assertEquals(1, notificationCount[0]);
    }

    @Test
    public void removeListener_stopsReceivingNotifications() {
        CartRepository repository = new CartRepository();
        int[] notificationCount = {0};
        CartRepository.CartListener listener = () -> notificationCount[0]++;
        repository.addListener(listener);
        repository.addItem(product("a", 4.50));

        repository.removeListener(listener);
        repository.addItem(product("b", 3.25));

        assertEquals(1, notificationCount[0]);
    }

    @Test
    public void getInstance_alwaysReturnsSameInstance() {
        assertSame(CartRepository.getInstance(), CartRepository.getInstance());
    }

    @Test
    public void getTotalItemCount_sumsQuantitiesAcrossDistinctProducts() {
        CartRepository repository = new CartRepository();
        Product productA = product("a", 4.50);
        Product productB = product("b", 3.25);

        repository.addItem(productA);
        repository.addItem(productB);
        repository.addItem(productB);

        assertEquals(3, repository.getTotalItemCount());
    }
}
