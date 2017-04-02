/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Point.java
 *---------------------------------------------------
 * Similar Point class to the java.awt.Point class
 * but uses double precision instead of int.
 *---------------------------------------------------*/

public class Point {

	private double x;
	private double y;

	/*---------------------------------------------------
 	 * Constructor
 	 *---------------------------------------------------*/
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/*---------------------------------------------------
	 * double getX()
	 *---------------------------------------------------
	 * Returns x with double precision.
	 *---------------------------------------------------*/
	public double getX() {
		return x;
	}

	/*---------------------------------------------------
	 * double getY()
	 *---------------------------------------------------
	 * Returns y with double precision.
	 *---------------------------------------------------*/
	public double getY() {
		return y;
	}

	/*---------------------------------------------------
	 * void setX(double)
	 *---------------------------------------------------
	 * Sets x to the value provided.
	 *---------------------------------------------------*/
	public void setX(double x) {
		this.x = x;
	}

	/*---------------------------------------------------
	 * void setY(double)
	 *---------------------------------------------------
	 * Sets y to the value provided.
	 *---------------------------------------------------*/
	public void setY(double y) {
		this.y = y;
	}

	/*---------------------------------------------------
	 * void move(double,double)
	 *---------------------------------------------------
	 * Sets both x and y to the values provided.
	 *---------------------------------------------------*/
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/*---------------------------------------------------
	 * @Override toString()
	 *---------------------------------------------------
	 * Returns a string version of the Point.
	 *---------------------------------------------------*/
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}