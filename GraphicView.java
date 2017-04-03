/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * GraphicView.java
 *---------------------------------------------------
 * GraphicView, which extends JPanel, used for
 * drawing on the JPanel.
 *---------------------------------------------------*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphicView extends JPanel {

	private Point[] p;
	private int size;

	/*---------------------------------------------------
	 * Constructor
	 *---------------------------------------------------*/
	public GraphicView(Point[] p, int size) {
		this.p = p;
		this.size = size;
		//this.setBackground(Color.BLUE);
		repaint();
	}

	/*---------------------------------------------------
	 * void updatePosition(Point[])
	 *---------------------------------------------------
	 * Sets the position array to the new position array
	 * provided and calls the repaint method. 
	 *---------------------------------------------------*/
	public void updatePosition(Point[] p) {
		this.p = p;
		repaint();
	}

	/*---------------------------------------------------
	 * void paintComponent(Graphics)
	 *---------------------------------------------------
	 * Paint component used for drawing on the Jpanel.
	 *---------------------------------------------------*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		for (Point point : p) {
			g2.fillOval((int)point.getX(), (int)point.getY(), size, size);
		}
	}
}