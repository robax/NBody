import java.awt.Color;
import java.util.Random;

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
	public Color color;
	
	public Planet(Point p, Point v, Point f, double m){
		this.p = p;
		this.f = f;
		this.v = v;
		this.m = m;
		this.color = colors[randInt(0, colors.length-1)];
	}
	
	private static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
}
