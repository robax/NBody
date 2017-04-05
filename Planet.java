import java.awt.Color;

/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Planet.java
 *---------------------------------------------------
 * Represents a single body of the N-Body problem
 *---------------------------------------------------*/

public class Planet {
	
	public static final Color[] colors = new Color[]{
		Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, 
		Color.ORANGE, Color.CYAN, Color.WHITE, Color.GRAY, Color.MAGENTA
	};
	
	public Point p, f, v;
	public double m;
	public int radius;
	public Color color;
	
	/*---------------------------------------------------
	 * Constructor
	 *---------------------------------------------------*/
	public Planet(){
		int vxneg, vyneg, fxneg, fyneg;
		vxneg = (int)(Math.random()*2);
		vyneg = (int)(Math.random()*2);
		fxneg = (int)(Math.random()*2);
		fyneg = (int)(Math.random()*2);
		p = new Point((Math.random()*1200),(Math.random()*600));
		v = new Point(vxneg == 1 ? Math.random()*-10 : Math.random()*10,
					  vyneg == 1 ? Math.random()*-10 : Math.random()*10);
		f = new Point(fxneg == 1 ? Math.random()*-10 : Math.random()*10,
					  fyneg == 1 ? Math.random()*-10 : Math.random()*10);
		m = Math.random()*100000;
		this.color = colors[(int)(Math.random()*colors.length)];
		this.radius = (int) Math.log(m);
	}
	
}
