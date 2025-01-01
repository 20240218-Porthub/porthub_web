package hello.example.porthub.config.util;

import java.util.Map;

public class CategoryConstants {
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String DEFAULT_ORDER = "NewestOrder";
    public static final Map<String, Integer> CATEGORY_MAP = Map.of(
            "Development", 2,
            "Music", 3,
            "Design", 4,
            "Editing", 5,
            "Film", 6,
            "Marketing", 7,
            "Other", 8
    );
}
