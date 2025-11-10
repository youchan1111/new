import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            MainDisplay mainDisplay = new MainDisplay();
            mainDisplay.setVisible(true);
        });
    }
}
