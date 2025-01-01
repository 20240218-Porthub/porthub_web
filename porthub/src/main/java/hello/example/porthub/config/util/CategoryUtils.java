package hello.example.porthub.config.util;

public class CategoryUtils {
    public static int getCategoryCheckNum(String categoryName) {
        return CategoryConstants.CATEGORY_MAP.getOrDefault(categoryName, 0);
    }
}
