import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RecipeDataset {

    /**
     * category 예:
     *  - "side_dishes"      (밑반찬)
     *  - "main_side_dishes" (메인반찬)
     *  - "soups"            (국·찌개)
     *  - "rice_dishes"      (밥)
     *
     * datasets/texts/<category>.txt 를 읽어서
     * 한 줄당 Recipe 하나로 만들어 List<Recipe> 로 반환.
     */
    public static List<Recipe> loadCategory(String category) {
        List<Recipe> list = new ArrayList<>();

        String textPath   = "datasets/texts/" + category + ".txt";
        String imgBaseDir = "datasets/imgs/" + category + "/";

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(textPath), "UTF-8"))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // 빈 줄 건너뛰기

                Recipe r = RecipeParser.parseLine(line, imgBaseDir);
                if (r != null) {
                    list.add(r);
                    System.out.println("[" + category + "] 읽은 레시피: " + r.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("[" + category + "] 총 레시피 개수: " + list.size());
        return list;
    }
}
