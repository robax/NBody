/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Seqnbody.java
 *---------------------------------------------------
 * Sequential version of the n-body problem.
 *---------------------------------------------------*/

public class Seqnbody {

	public static int workers, n, size, numSteps,dt;
	public static Planet[] planets;
	public static final double g = Math.pow(6.67,-11);

	/*---------------------------------------------------
 	 * Main
 	 *---------------------------------------------------*/
	public static void main (String[] args) {
		if (args.length < 4){
			// default settings so i dont have to type as much
			//error("java <# workers> <# bodies> <size body> <# time steps>");
			workers = 1;
			n = 100;
			size = 10;
			numSteps = 10000;
			dt = 1;	
		}
		else{
			// Helpful in case you forget the input
			System.out.println("java <# workers> <# bodies> <size body> <# time steps>");

			workers = Integer.valueOf(args[0]);
			n = Integer.valueOf(args[1]);
			size = Integer.valueOf(args[2]);
			numSteps = Integer.valueOf(args[3]);
			dt = 1;
		}

		planets = initPlanets(n);
		GUI gui = new GUI("NBody Problem", planets);
		gui.setVisible(true);

		for (int time = 0; time < numSteps*dt; time++) {
			calculateForces();
			moveBodies();
			gui.update();
			// Sleep if you want to see the current parameters more slowly
			try {Thread.sleep(100);}catch(InterruptedException e){}
		}
	}

	/*---------------------------------------------------
	 * void initPFVM(int, Point[], Point[], Point[],
	 * double])
	 *---------------------------------------------------
	 * Initializes the position, velocity, force, and
	 * mass arrays.
	 * 
	 * Change the initial conditions for different
	 * results.
	 *---------------------------------------------------*/
	public static Planet[] initPlanets(int n) {
		Planet[] out = new Planet[n];
		for (int i = 0; i < n; i++) {
			out[i] = new Planet();
		}
		return out;
	}

	/*---------------------------------------------------
	 * void calculateForces()
	 *---------------------------------------------------
	 * Calculates total force for every pair of bodies.
	 *---------------------------------------------------*/
	public static void calculateForces() {
		double distance, magnitude;
		Point direction;
		for (int i = 1; i < n-1; i++) {
			for (int j = i+1; j < n; j++) {
				distance = Math.sqrt(Math.pow(planets[i].p.getX()-planets[j].p.getX(),2) +
									 Math.pow(planets[i].p.getY()-planets[j].p.getY(),2));
				magnitude = (g*planets[i].m*planets[j].m) / Math.pow(distance,2);
				direction = new Point(planets[j].p.getX()-planets[i].p.getX(), planets[i].p.getY()-planets[i].p.getY());
				planets[i].f.setX(planets[i].f.getX()+magnitude*direction.getX()/distance);
				planets[j].f.setX(planets[j].f.getX()-magnitude*direction.getX()/distance);
				planets[i].f.setY(planets[i].f.getY()+magnitude*direction.getY()/distance);
				planets[j].f.setY(planets[j].f.getY()-magnitude*direction.getY()/distance);
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
			deltav = new Point(planets[i].f.getX()/planets[i].m*dt,planets[i].f.getY()/planets[i].m*dt);
			deltap = new Point((planets[i].v.getX()+deltav.getX()/2)*dt,
							   (planets[i].v.getY()+deltav.getY()/2)*dt);
			planets[i].v.setX(planets[i].v.getX()+deltav.getX());
			planets[i].v.setY(planets[i].v.getY()+deltav.getY());
			planets[i].p.setX(planets[i].p.getX()+deltap.getX());
			planets[i].p.setY(planets[i].p.getY()+deltap.getY());
			planets[i].f.move(0.0,0.0); // reset force vector
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