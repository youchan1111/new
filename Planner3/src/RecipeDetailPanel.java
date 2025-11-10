import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class RecipeDetailPanel extends JPanel {

    private MainDisplay mainDisplay;
    private Recipe recipe;

    public RecipeDetailPanel(Recipe recipe, MainDisplay mainDisplay) {
        this.recipe = recipe;
        this.mainDisplay = mainDisplay;

        setLayout(new BorderLayout(10, 10));

        // ===== 상단 버튼 영역 (뒤로가기 / 추가하기) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("뒤로가기");
        JButton addButton = new JButton("추가하기");

        // 🔙 뒤로가기: 메인디스플레이에게 "검색 화면 보여줘" 요청
        backButton.addActionListener(e -> {
            if (mainDisplay != null) {
                mainDisplay.showSearch();
            }
        });

        // ⚠️ addButton은 아직 기능 없으면 나중에 구현해도 됨
        // addButton.addActionListener(e -> { ... });

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ===== 실제 내용 패널(centerPanel, 세로로 쌓기) =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1) 이미지
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        if (recipe.getImagePath() != null) {
            try {
                ImageIcon icon = new ImageIcon(recipe.getImagePath());
                Image img = icon.getImage()
                                .getScaledInstance(320, 220, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
                imageLabel.setIcon(icon);
            } catch (Exception e) {
                imageLabel.setText("이미지를 불러올 수 없습니다.");
            }
        } else {
            imageLabel.setText("이미지 없음");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(320, 220));
        centerPanel.add(imageLabel);

        centerPanel.add(Box.createVerticalStrut(10));

        // 2) 이름
        JLabel nameLabel = new JLabel(recipe.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(nameLabel);

        centerPanel.add(Box.createVerticalStrut(5));

        // 3) 인분 + 시간
        JLabel infoLabel = new JLabel(
                recipe.getServings() + "인분, 조리시간 " + recipe.getCookTime() + "분");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(infoLabel);

        centerPanel.add(Box.createVerticalStrut(15));

        // 4) 재료
        JLabel ingLabel = new JLabel("재료");
        ingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(ingLabel);

        JTextArea ingArea = new JTextArea(recipe.getIngredients());
        ingArea.setLineWrap(true);
        ingArea.setWrapStyleWord(true);
        ingArea.setEditable(false);
        ingArea.setCaretPosition(0);

        JScrollPane ingScroll = new JScrollPane(
                ingArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ingScroll.setPreferredSize(new Dimension(320, 90));
        ingScroll.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
        ingScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(ingScroll);

        centerPanel.add(Box.createVerticalStrut(15));

        // 5) 조리방법
        JLabel methodLabel = new JLabel("조리방법");
        methodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        methodLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(methodLabel);

        JTextArea methodArea = new JTextArea(recipe.getMethod());
        methodArea.setLineWrap(true);
        methodArea.setWrapStyleWord(true);
        methodArea.setEditable(false);
        methodArea.setCaretPosition(0);

        JScrollPane methodScroll = new JScrollPane(
                methodArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        methodScroll.setPreferredSize(new Dimension(320, 260));
        methodScroll.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
        methodScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(methodScroll);

        // ===== 중앙 정렬을 위한 겉패널 =====
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.add(centerPanel);

        JScrollPane mainScroll = new JScrollPane(
                outerPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(mainScroll, BorderLayout.CENTER);
    }
}
