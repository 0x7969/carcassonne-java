package fop.view;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class DraggablePanel extends JPanel {
	Point anchorPoint;

	public DraggablePanel() {
		addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent event) {
				anchorPoint = event.getPoint();
			}

			@Override
			public void mouseDragged(MouseEvent event) {
				int anchorX = anchorPoint.x;
				int anchorY = anchorPoint.y;

				Point parentOnScreen = getParent().getLocationOnScreen();
				Point mouseOnScreen = event.getLocationOnScreen();
				Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX,
						mouseOnScreen.y - parentOnScreen.y - anchorY);
				setLocation(position);
			}
		});
	}
}
