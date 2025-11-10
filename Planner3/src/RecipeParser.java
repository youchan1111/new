import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeParser {

    public static Recipe parseLine(String line, String imgBaseDir) {
        // 1. 이름 : 첫 번째 '[' 앞까지
        int idx1 = line.indexOf('[');
        if (idx1 == -1) return null;
        String name = line.substring(0, idx1).trim();

        // 2. 재료 : 첫 번째 [ ... ] 안
        int idx2 = line.indexOf(']', idx1 + 1);
        if (idx2 == -1) return null;
        String ingredientsRaw = line.substring(idx1 + 1, idx2).trim();

        // 3. 조리방법 : 두 번째 [ ... ] 안
        int idx3 = line.indexOf('[', idx2 + 1);
        int idx4 = line.indexOf(']', idx3 + 1);
        if (idx3 == -1 || idx4 == -1) return null;
        String methodRaw = line.substring(idx3 + 1, idx4).trim();

        // 4. 나머지 : "4인분 30분 이내" 같은 부분
        String tail = line.substring(idx4 + 1).trim();

        // 첫 숫자 → 인분, 마지막 숫자 → 시간(분)
        int servings = extractFirstNumber(tail);
        int cookTime = extractLastNumber(tail);

        // 보기 좋게 쉼표 기준 줄바꿈
        String ingredients = ingredientsRaw.replaceAll("\\s*,\\s*", "\n");
        String method = methodRaw.replaceAll("\\s*,\\s*", "\n");

        // 이미지 경로 : 폴더 + 이름 + ".jpg"
        String imagePath = imgBaseDir + name + ".jpg";

        return new Recipe(name, ingredients, method, servings, cookTime, imagePath);
    }

    // 문자열에서 첫 번째 숫자 찾기
    private static int extractFirstNumber(String text) {
        Matcher m = Pattern.compile("(\\d+)").matcher(text);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    // 문자열에서 마지막 숫자 찾기
    private static int extractLastNumber(String text) {
        Matcher m = Pattern.compile("(\\d+)").matcher(text);
        int last = 0;
        while (m.find()) {
            last = Integer.parseInt(m.group(1));
        }
        return last;
    }
}
