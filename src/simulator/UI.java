package simulator;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;

public class UI {
	UIPanel ui;
	void initUI() {
		ui = new UIPanel();
		
		ui.requestFocus();
		ui.setFocusable(true);
//		ui.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				ui.requestFocus();
//				ui.setFocusable(true);
//			}
//		});
		
		
	}
}
