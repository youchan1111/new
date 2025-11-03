package hiqwe;

public class qwer {
	package mealplanner;


	import mealplanner.dao.RecipeDAO;
	import mealplanner.ui.RecipeListPanel;

	import javax.swing.*;
	import java.awt.*;

	public class RecipeApp {

		public static void main(String[] args){
	        SwingUtilities.invokeLater(() -> {
	            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
	            JFrame f = new JFrame("레시피 관리");
	            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            f.setLayout(new BorderLayout());
	            f.add(new RecipeListPanel(new RecipeDAO()), BorderLayout.CENTER);
	            f.setSize(900, 600);
	            f.setLocationRelativeTo(null);
	            f.setVisible(true);
	        });

	}
	}


}
