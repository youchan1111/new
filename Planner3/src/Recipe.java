public class Recipe {
    private String name;        // 메뉴 이름
    private String ingredients; // 재료 (여러 줄)
    private String method;      // 조리방법 (여러 줄)
    private int servings;       // 인분 수
    private int cookTime;       // 조리시간(분)
    private String imagePath;   // 이미지 파일 경로

    public Recipe(String name, String ingredients, String method,
                  int servings, int cookTime, String imagePath) {
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.servings = servings;
        this.cookTime = cookTime;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getMethod() {
        return method;
    }

    public int getServings() {
        return servings;
    }

    public int getCookTime() {
        return cookTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        // JList에 아이템을 표시할 때 사용
        return name;
    }
}
