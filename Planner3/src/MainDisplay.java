import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainDisplay extends JFrame {

    // 화면 전환용 레이아웃
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // 화면들(패널)
    private RecipeSearchPanel searchPanel;
    private RecipeDetailPanel detailPanel;

    public MainDisplay() {
        setTitle("레시피 관리 시스템");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 카드 레이아웃으로 여러 화면 관리
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 🔹 검색 화면 생성 (this = MainDisplay 넘겨줌)
        searchPanel = new RecipeSearchPanel(this);
        cardPanel.add(searchPanel, "search");    // 이름: "search"

        // 기본으로 검색 화면 보이게
        cardLayout.show(cardPanel, "search");

        // 프레임에 카드 패널 붙이기
        add(cardPanel, BorderLayout.CENTER);
    }

    /**
     * 상세 화면 보여주기
     * 검색 화면에서 레시피 선택하면 이 메서드를 호출해주면 됨
     */
    public void showDetail(Recipe recipe) {
        // 기존 상세 패널 있으면 제거
        if (detailPanel != null) {
            cardPanel.remove(detailPanel);
        }

        // 새 레시피 기준으로 상세 패널 다시 생성
        detailPanel = new RecipeDetailPanel(recipe, this);
        cardPanel.add(detailPanel, "detail");

        // "detail" 화면으로 전환
        cardLayout.show(cardPanel, "detail");
    }

    /**
     * 다시 검색 화면으로 돌아가기
     * 상세 화면의 "뒤로가기" 버튼에서 호출하면 됨
     */
    public void showSearch() {
        cardLayout.show(cardPanel, "search");
    }

    }
