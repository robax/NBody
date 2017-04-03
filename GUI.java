/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * GUI.java
 *---------------------------------------------------
 * GUI used for Seqnbody.java
 *---------------------------------------------------*/

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	public static final int width = 600;
	public static final int height = 600;
	private GraphicView view;
	private String title;
	private Point[] p;
	private int size;

	/*---------------------------------------------------
	 * Constructor
	 *---------------------------------------------------*/
	public GUI(String title, Point[] p, int size) {
		this.title = title;
		this.p = p;
		this.size = size;
		initializeFrame();
		view = new GraphicView(this.p, this.size);
		this.add(view);
	}

	/*---------------------------------------------------
	 * void initializeFrame()
	 *---------------------------------------------------
	 * Initializes JFrame attributes
	 *---------------------------------------------------*/
	private void initializeFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(width,height);
		this.setLocationRelativeTo(null);
		this.setTitle(title);
	}

	/*---------------------------------------------------
	 * void update(Point[])
	 *---------------------------------------------------
	 * Calls the updatePosition method from the Jpanel
	 * in order to update the Jpanel with the new 
	 * positions.
	 *---------------------------------------------------*/
	public void update(Point[] p) {
		view.updatePosition(p);
	}
}