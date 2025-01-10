package hello.example.porthub.config.util;

import java.util.Map;

public class CategoryConstants {
    public static final int DEVELOPMENT = 2;
    public static final int MUSIC = 3;
    public static final int DESIGN = 4;
    public static final int EDITING = 5;
    public static final int FILM = 6;
    public static final int MARKETING = 7;
    public static final int OTHER = 8;
    public static final int DEFAULT = 0; // 기본값 추가

    private static final Map<String, Integer> CATEGORY_MAP = Map.of(
            "Development", DEVELOPMENT,
            "Music", MUSIC,
            "Design", DESIGN,
            "Editing", EDITING,
            "Film", FILM,
            "Marketing", MARKETING,
            "Other", OTHER
    );

    public static int getCheckNum(String categoryName) {
        return CATEGORY_MAP.getOrDefault(categoryName, DEFAULT); // 기본값 반환
    }
}
