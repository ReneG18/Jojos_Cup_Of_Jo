package com.example.jojos_cup_of_jo.data;

import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.model.ProductCategory;
import com.example.jojos_cup_of_jo.model.ProductType;
import com.example.jojos_cup_of_jo.model.StoreInfo;
import com.example.jojos_cup_of_jo.model.TeamMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hardcoded placeholder content for the UI shell. Swap this out for a real data source later
 * without touching any of the screens that consume it.
 */
public final class SampleDataProvider {

    private SampleDataProvider() {
    }

    public static List<Product> getMenuProducts() {
        return new ArrayList<>(Arrays.asList(
                new Product("menu_pumpkin_spice_latte", "Pumpkin Spice Latte",
                        "Espresso, steamed milk, real pumpkin & warm autumn spice", 5.25,
                        ProductType.MENU, ProductCategory.COFFEE, 0, true),
                new Product("menu_vanilla_latte", "Vanilla Latte",
                        "Espresso, steamed milk, house vanilla syrup", 4.50,
                        ProductType.MENU, ProductCategory.COFFEE, 0),
                new Product("menu_cappuccino", "Cappuccino",
                        "Espresso with a deep layer of milk foam", 4.00,
                        ProductType.MENU, ProductCategory.COFFEE, 1),
                new Product("menu_drip_coffee", "Drip Coffee",
                        "Our daily single-origin, brewed fresh", 2.75,
                        ProductType.MENU, ProductCategory.COFFEE, 2),
                new Product("menu_cold_brew", "Cold Brew",
                        "Steeped 18 hours for a smooth, bold finish", 4.25,
                        ProductType.MENU, ProductCategory.COFFEE, 3),
                new Product("menu_chai_latte", "Chai Tea Latte",
                        "Spiced black tea with steamed milk", 4.75,
                        ProductType.MENU, ProductCategory.TEA, 4),
                new Product("menu_green_tea", "Iced Green Tea",
                        "Lightly sweetened, brewed over ice", 3.50,
                        ProductType.MENU, ProductCategory.TEA, 5),
                new Product("menu_croissant", "Butter Croissant",
                        "Baked fresh every morning", 3.25,
                        ProductType.MENU, ProductCategory.PASTRY, 0),
                new Product("menu_muffin", "Blueberry Muffin",
                        "Loaded with wild blueberries", 3.50,
                        ProductType.MENU, ProductCategory.PASTRY, 1),
                new Product("menu_avocado_toast", "Avocado Toast",
                        "Sourdough, smashed avocado, chili flake", 6.50,
                        ProductType.MENU, ProductCategory.PASTRY, 2)
        ));
    }

    public static List<Product> getMerchProducts() {
        return new ArrayList<>(Arrays.asList(
                new Product("merch_autumn_scarf", "Autumn Knit Scarf",
                        "Soft knit scarf in our signature sunflower gold", 28.00,
                        ProductType.MERCH, ProductCategory.APPAREL, 4, true),
                new Product("merch_logo_mug", "Jojo's Logo Mug",
                        "Ceramic, 12oz, dishwasher safe", 14.00,
                        ProductType.MERCH, ProductCategory.DRINKWARE, 2),
                new Product("merch_travel_tumbler", "Ceramic Travel Tumbler",
                        "Double-walled, keeps drinks hot for hours", 22.00,
                        ProductType.MERCH, ProductCategory.DRINKWARE, 3),
                new Product("merch_hoodie", "Cozy Hoodie",
                        "Heavyweight fleece, embroidered logo", 38.00,
                        ProductType.MERCH, ProductCategory.APPAREL, 4),
                new Product("merch_cropped_tee", "Cropped Tee",
                        "Soft cotton, screen-printed sunflower", 24.00,
                        ProductType.MERCH, ProductCategory.APPAREL, 5),
                new Product("merch_tote_bag", "Canvas Tote Bag",
                        "Sturdy canvas, fits a laptop and a latte", 18.00,
                        ProductType.MERCH, ProductCategory.ACCESSORY, 0),
                new Product("merch_enamel_pin", "Sunflower Enamel Pin",
                        "Hard enamel, gold plated backing", 6.00,
                        ProductType.MERCH, ProductCategory.ACCESSORY, 1)
        ));
    }

    /** The drink Home features, or null if nothing on the menu is currently marked seasonal. */
    public static Product getSeasonalDrink() {
        return firstSeasonal(getMenuProducts());
    }

    /** The merch item Home features, or null if nothing in the merch list is marked seasonal. */
    public static Product getSeasonalMerchItem() {
        return firstSeasonal(getMerchProducts());
    }

    private static Product firstSeasonal(List<Product> products) {
        for (Product product : products) {
            if (product.isSeasonal()) {
                return product;
            }
        }
        return null;
    }

    public static List<TeamMember> getTeamMembers() {
        return new ArrayList<>(Arrays.asList(
                new TeamMember("Jordan Eagle Gonzalez", "Founder & Head Roaster",
                        "“Coffee is just an excuse to slow down.”", 0),
                new TeamMember("Navi Gonzalez", "Head Barista",
                        "“Latte art is the first thing I ever got applause for.”", 1),
                new TeamMember("Rene Gonzalez", "Pastry Chef",
                        "“A good croissant should shatter, not bend.”", 2),
                new TeamMember("Autumn Gonzalez", "Shift Lead",
                        "“I know every regular's order by heart.”", 3),
                new TeamMember("Andrea Ann Gonzalez", "Roastery Assistant",
                        "“The smell of fresh beans never gets old.”", 4)
        ));
    }

    public static StoreInfo getStoreInfo() {
        return new StoreInfo(
                "123 Sunflower Lane, Riverside, CA 92501",
                "(951) 555-0182",
                Arrays.asList(
                        "Mon–Fri: 6:30 AM – 7:00 PM",
                        "Sat: 7:00 AM – 7:00 PM",
                        "Sun: 7:00 AM – 3:00 PM"
                ),
                "Jojo's Cup Of Jo started as a single cart at the farmer's market and grew into "
                        + "a neighborhood spot where regulars and strangers end up at the same "
                        + "table. We roast in small batches and bake every morning.",
                "Warm wood tables, big south-facing windows, and a wall of hand-painted "
                        + "sunflowers greet you at the door. Grab a window seat, or the big "
                        + "communal table if you're in the mood to chat.",
                "Thank you for stopping by Jojo's Cup Of Jo — we mean it when we say every "
                        + "cup is made just for you. See you again soon!",
                Arrays.asList(
                        "Jojo's Cup Of Jo — Sunflower Lane",
                        "Jojo's Cup Of Jo — Downtown Riverside"
                )
        );
    }
}
