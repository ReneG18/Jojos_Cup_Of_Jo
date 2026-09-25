package com.example.jojos_cup_of_jo.data;

import static org.junit.Assert.assertEquals;

import com.example.jojos_cup_of_jo.model.CartItem;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.model.ProductCategory;
import com.example.jojos_cup_of_jo.model.ProductType;

import org.junit.Test;

import java.util.List;

public class CartRepositoryTest {

    private static final double DELTA = 0.001;

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

        assertEquals(11.00, repository.getSubtotal(), DELTA);
    }

    @Test
    public void getTax_isSubtotalTimesTaxRate() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 10.00);

        repository.addItem(product);

        assertEquals(0.825, repository.getTax(), DELTA);
    }

    @Test
    public void getTotal_isSubtotalPlusTax() {
        CartRepository repository = new CartRepository();
        Product product = product("a", 10.00);

        repository.addItem(product);

        assertEquals(10.825, repository.getTotal(), DELTA);
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
}
