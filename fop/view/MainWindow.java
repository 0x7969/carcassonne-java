package fop.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {

	GameBoardPanel gameboardPanel;
	JPanel gameboardPanelWrapper;
	ToolbarPanel toolbarPanel;
	TileStackPanel tileStackPanel;

	public MainWindow(String title) {
		super(title);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}