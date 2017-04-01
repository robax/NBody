/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Seqnbody.java
 *---------------------------------------------------
 * Sequential version of the n-body problem.
 *---------------------------------------------------*/

public class Seqnbody {

	public int workers, n, size, dt;
	public Point[] p, v, f;
	public double[] m = new double[n];
	public static final double G = Math.pow(6.67,-11);

	/*---------------------------------------------------
 	 * Main
 	 *---------------------------------------------------*/
	public static void main (String[] args) {
		if (args.length < 4)
			error("java <# workers> <# bodies> <size body> <# time steps>");
		workers = Integer.valueOf(args[0]);
		n = Integer.valueOf(args[1]);
		size = Integer.valueOf(args[2]);
		dt = Integer.valueOf(args[3]);

		p new Point[n];
		v = new Point[n];
		f = new Point[n];
		initPVFM();
		System.out.println("TODO moveBodies line 75");

	}

	/*---------------------------------------------------
	 * void calculateForces()
	 *---------------------------------------------------
	 * Calculates total force for every pair of bodies.
	 *---------------------------------------------------*/
	public static void calculateForces() {
		double distance, magnitude.
		Point direction;
		for (int i = 1; i < n-1; i++) {
			for (int j = i+1; j < n; j++) {
				distance = Math.sqrt(Math.pow(p[i].getX()-p[j].getX(),2) +
									 Math.pow(p[i].getY()-p[j].getY(),2));
				direction = new Point(p[j].getX()-p[i].getX, p[j].getY()-p[i].getY());
				f[i].setX(f[i].getX()+magnitude*direction.getX()/distance);
				f[j].setX(f[j].getX()+magnitude*direction.getX()/distance);
				f[i].setY(f[i].getY()+magnitude*direction.getY()/distance);
				f[j].setY(f[j].getY()+magnitude*direction.getY()/distance);
			}
		}
	}

	/*---------------------------------------------------
	 * void moveBodies
	 *---------------------------------------------------
	 * Calculate new velocity and position for each body.
	 *---------------------------------------------------*/
	public static void moveBodies() {
		Point deltav, // dv = f/m * DT
			  deltap; // dp = (v + dv/2) * DT
		for (int i = 1; i < n; i++) {
			deltav = new Point(f[i].getX()/m[i]*dt,f[i].getY()/m[i]*dt);
			deltap = new Point((v[i].getX()+deltav.getX()/2)*dt,
							   (v[i].getY()+deltav.getY()/2)*dt);
			v[i].setX(v[i].getX()+deltav.getX());
			v[i].setY(v[i].getY()+deltav.getY());
			p[i].setX(p[i].getX()+deltap.getX());
			p[i].setY(v[i].getY()+deltap.getY());
			f[i].move(0.0,0.0); // reset force vector
		}

		// TODO not completely sure on start finish times yet
		/*
		for [time = start to finish by DT] {
			calculateForces();
			moveBodies();
		}
		*/
	}

	/*---------------------------------------------------
	 * void initPFVM(int, Point[], Point[], Point[],
	 * double])
	 *---------------------------------------------------
	 * Initializes the position, velocity, force, and
	 * mass arrays.
	 *---------------------------------------------------*/
	public static void initPVFM() {
		for (int i = 0; i < n; i++) {
			positions[i] = new Point(1,2);
			velocities[i] = new Point(2,2);
			forces[i] = new Point(3,3);
			masses[i] = 1.0;
		}
	}

	/*---------------------------------------------------
	 * void error(String)
	 *---------------------------------------------------
	 * Prints the provided error message and exits the 
	 * program with an error code of 1.
	 *---------------------------------------------------*/
	public static void error (String message) {
		System.err.println("Error: " + message);
		System.exit(1);
	}
}