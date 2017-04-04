/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Planet.java
 *---------------------------------------------------
 * Represents a single body of the N-Body problem
 *---------------------------------------------------*/

public class Planet {
	
	public Point p, f, v;
	public double m;
	public String color;
	
	public Planet(Point p, Point v, Point f, double m){
		this.p = p;
		this.f = f;
		this.v = v;
		this.m = m;
		// color = random color
	}
	
}
