package simulator;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
