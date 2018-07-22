package adsforcharity.deanbangera.dmbangera.com.adsforcharity.Charities;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class Charity {

    /**
     * An array of sample (Charity) items.
     */
    public static final List<CharityItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (Charity) items, by ID.
     */
    static final SparseArray<CharityItem> ITEM_MAP = new SparseArray<>();

    static {
        // Add some sample items.
        initCharityItems();
    }

    private static void initCharityItems() {
        CharityItem item = new CharityItem(1,"name1", "description1", "PicLink", "Link");
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        CharityItem item2 = new CharityItem(2, "name2", "description2", "PicLink2", "Link2");
        ITEMS.add(item2);
        ITEM_MAP.put(item.id, item2);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class CharityItem {
        final int id;
        public final String name;
        public final String description;
        public final String picLink;
        public final String link;

        CharityItem(int id, String name, String description, String picLink, String link) {
            this.id = id;
            this.name =name;
            this.description = description;
            this.picLink = picLink;
            this.link = link;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
