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

	private Planet[] planets;
	private int size;

	/*---------------------------------------------------
	 * Constructor
	 *---------------------------------------------------*/
	public GraphicView(Planet[] planets, int size) {
		this.planets = planets;
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
	public void updatePosition() {
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
		for (Planet planet : planets) {
			g2.fillOval((int)planet.p.getX(), (int)planet.p.getY(), size, size);
		}
	}
}