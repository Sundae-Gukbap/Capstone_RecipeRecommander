package com.sundaegukbap.banchango.recipe.domain;

public enum RecipeCategory {
    전체(0, "전체"),
    밑반찬(1, "밑반찬"),
    메인반찬(1, "메인반찬"),
    국_탕(1, "국/탕"),
    찌개(1, "찌개"),
    디저트(1, "디저트"),
    면_만두(1, "면/만두"),
    밥_죽_떡(1, "밥/죽/떡"),
    퓨전(1, "퓨전"),
    김치_젓갈_장류(1, "김치/젓갈/장류"),
    양념_소스_잼(1, "양념/소스/잼"),
    양식(1, "양식"),
    샐러드(1, "샐러드"),
    스프(1, "스프"),
    빵_과자(1, "빵/과자"),
    과자_간식(1, "과자/간식"),
    차_음료_술(1, "차/음료/술"),
    일상(2, "일상"),
    초스피드(2, "초스피드"),
    손님접대(2, "손님접대"),
    술안주(2, "술안주"),
    다이어트(2, "다이어트"),
    도시락(2, "도시락"),
    영양식(2, "영양식"),
    간식(2, "간식"),
    야식(2, "야식"),
    푸드스타일링(2, "푸드스타일링"),
    해장(2, "해장"),
    명절음식(2, "명절음식"),
    이유식(2, "이유식"),
    기타(2, "기타");

    private final int topCategory;
    private final String subCategory;

    RecipeCategory(int topCategory, String subCategory) {
        this.topCategory = topCategory;
        this.subCategory = subCategory;
    }

    public int getTopCategory() {
        return topCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }
}
