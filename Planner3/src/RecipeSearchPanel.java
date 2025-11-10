import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTextField;

public class RecipeSearchPanel extends JPanel {

    // 메인 프레임(MainDisplay) 참조
    private MainDisplay mainDisplay;

    // 실제 데이터 폴더/파일 이름
    private static final String CAT_SIDE  = "side_dishes";       // 밑반찬
    private static final String CAT_MAIN  = "main_side_dishes";  // 메인반찬
    private static final String CAT_SOUP  = "soups";             // 국·찌개
    private static final String CAT_RICE  = "rice_dishes";       // 밥
    private static final String CAT_FAVORITES = "favorites";     // 즐겨찾기(가상)

    // 카테고리별 레시피 목록
    private Map<String, List<Recipe>> categoryMap = new HashMap<>();
    // 즐겨찾기 목록
    private List<Recipe> favorites = new ArrayList<>();
    // 현재 선택된 카테고리 기준 리스트
    private List<Recipe> currentBaseList = new ArrayList<>();

    // UI
    private JToggleButton btnSide;
    private JToggleButton btnMain;
    private JToggleButton btnSoup;
    private JToggleButton btnRice;
    private JToggleButton btnFav;

    private JTextField searchField;
    private DefaultListModel<Recipe> listModel;
    private JList<Recipe> resultList;

    // 🔹 MainDisplay에서 new RecipeSearchPanel(this) 로 생성할 예정
    public RecipeSearchPanel(MainDisplay mainDisplay) {
        this.mainDisplay = mainDisplay;

        // 패널 레이아웃 설정 (이제 JFrame 아니니까 setTitle/setSize 같은 건 없음)
        setLayout(new BorderLayout(5, 5));

        // 데이터 로딩
        loadCategories();

        // 기본 카테고리: 메인반찬
        currentBaseList = categoryMap.getOrDefault(CAT_MAIN, new ArrayList<>());

        // 상단 탭 + 검색창
        add(createTopPanel(), BorderLayout.NORTH);

        // 중앙 리스트
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        add(new JScrollPane(resultList), BorderLayout.CENTER);

        // 하단 버튼
        add(createBottomPanel(), BorderLayout.SOUTH);

        attachEventHandlers();

        // 초기 리스트 표시
        updateList(currentBaseList);
    }

    // ===== 상단 영역(카테고리 탭 + 검색창) =====
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        // 카테고리 탭
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnSide = new JToggleButton("밑반찬");
        btnMain = new JToggleButton("메인반찬");
        btnSoup = new JToggleButton("국·찌개");
        btnRice = new JToggleButton("밥");
        btnFav  = new JToggleButton("즐겨찾기");

        ButtonGroup group = new ButtonGroup();
        group.add(btnSide);
        group.add(btnMain);
        group.add(btnSoup);
        group.add(btnRice);
        group.add(btnFav);

        categoryPanel.add(btnSide);
        categoryPanel.add(btnMain);
        categoryPanel.add(btnSoup);
        categoryPanel.add(btnRice);
        categoryPanel.add(btnFav);

        btnMain.setSelected(true); // 기본 선택

        // 검색창
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        JButton searchButton = new JButton("검색");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // 검색 이벤트
        searchButton.addActionListener(e -> doSearch());
        searchField.addActionListener(e -> doSearch());

        topPanel.add(categoryPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        return topPanel;
    }

    // ===== 하단 버튼 영역(즐겨찾기/자세히 보기) =====
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton favButton = new JButton("즐겨찾기 추가");
        JButton openButton = new JButton("자세히 보기");
        bottomPanel.add(favButton);
        bottomPanel.add(openButton);

        favButton.addActionListener(e -> addSelectedToFavorites());
        openButton.addActionListener(e -> openSelectedRecipe());

        return bottomPanel;
    }

    // ===== 버튼/리스트 이벤트 연결 =====
    private void attachEventHandlers() {
        btnSide.addActionListener(e -> changeCategory(CAT_SIDE));
        btnMain.addActionListener(e -> changeCategory(CAT_MAIN));
        btnSoup.addActionListener(e -> changeCategory(CAT_SOUP));
        btnRice.addActionListener(e -> changeCategory(CAT_RICE));
        btnFav.addActionListener(e -> changeCategory(CAT_FAVORITES));

        // 리스트 더블클릭 → 상세 보기
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openSelectedRecipe();
                }
            }
        });
    }

    // ===== 데이터 로딩 =====
    private void loadCategories() {
        categoryMap.put(CAT_SIDE, RecipeDataset.loadCategory(CAT_SIDE));
        categoryMap.put(CAT_MAIN, RecipeDataset.loadCategory(CAT_MAIN));
        categoryMap.put(CAT_SOUP, RecipeDataset.loadCategory(CAT_SOUP));
        categoryMap.put(CAT_RICE, RecipeDataset.loadCategory(CAT_RICE));
    }

    // ===== 카테고리 변경 =====
    private void changeCategory(String code) {
        if (CAT_FAVORITES.equals(code)) {
            currentBaseList = favorites;
        } else {
            currentBaseList = categoryMap.getOrDefault(code, new ArrayList<>());
        }

        if (searchField.getText().trim().isEmpty()) {
            updateList(currentBaseList);
        } else {
            doSearch();
        }
    }

    // ===== 검색 실행 =====
    private void doSearch() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            updateList(currentBaseList);
            return;
        }

        List<Recipe> filtered = currentBaseList.stream()
                .filter(r -> r.getName().contains(keyword))
                .collect(Collectors.toList());

        updateList(filtered);
    }

    // ===== 리스트 갱신 =====
    private void updateList(List<Recipe> data) {
        listModel.clear();
        for (Recipe r : data) {
            listModel.addElement(r);
        }
    }

    // ===== 즐겨찾기 추가 =====
    private void addSelectedToFavorites() {
        Recipe selected = resultList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "즐겨찾기에 추가할 레시피를 선택하세요.");
            return;
        }
        if (!favorites.contains(selected)) {
            favorites.add(selected);
            JOptionPane.showMessageDialog(this, "즐겨찾기에 추가되었습니다.");
        } else {
            JOptionPane.showMessageDialog(this, "이미 즐겨찾기에 있습니다.");
        }

        if (btnFav.isSelected()) {
            updateList(favorites);
        }
    }

    // ===== 상세 보기 띄우기 =====
    private void openSelectedRecipe() {
        Recipe selected = resultList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "레시피를 선택하세요.");
            return;
        }

        // 🔹 예전: new RecipeDetailFrame(selected).setVisible(true);
        // 🔹 지금: MainDisplay에게 "상세화면 보여줘" 요청
        if (mainDisplay != null) {
            mainDisplay.showDetail(selected);
        }
    }
}
