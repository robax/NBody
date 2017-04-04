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

	/*---------------------------------------------------
	 * Constructor
	 *---------------------------------------------------*/
	public GraphicView(Planet[] planets) {
		this.planets = planets;
		this.setBackground(Color.BLACK);
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
		for (Planet planet : planets) {
			g2.setColor(planet.color);
			g2.fillOval((int)planet.p.getX(), (int)planet.p.getY(), planet.radius, planet.radius);
		}
	}
	
}