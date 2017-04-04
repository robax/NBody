/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * PlanetThread.java
 *---------------------------------------------------
 * Basic thread in charge of some portion of the planets
 *---------------------------------------------------*/

public class PlanetThread extends Thread{
	
	private final static double G = 0.0000000000667;

	private int me, chunkStart, chunkEnd;
	private Barrier barrier;
	private int dt, numSteps;
	private Planet[] planets;
	
	/*---------------------------------------------------
	 * PlanetThread()
	 *---------------------------------------------------
	 * Thread constructor
	 *---------------------------------------------------*/
	public PlanetThread(int numThreads, int me, Barrier barrier, int dt, int numSteps, Planet[] planets){
		this.me = me;
		this.barrier = barrier;
		this.dt = dt;
		this.numSteps = numSteps;
		this.planets = planets;
		
		int chunkSize = planets.length / numThreads;
		chunkStart = chunkSize * me;
		chunkEnd = chunkSize * (me+1);
		
		// this check is necessary to avoid off-by-remainder errors
		if(me==numThreads-1){
			chunkEnd = planets.length;
		}
		System.out.println("Thread " + me + " assigned chunk " + chunkStart + "-" + chunkEnd);
	}

	/*---------------------------------------------------
	 * void run()
	 *---------------------------------------------------
	 * Function that each thread runs
	 *---------------------------------------------------*/
	public void run() {
		for (int time = 0; time < numSteps*dt; time++) {
			//System.out.println("Thread " + me + " at time " + time);
			calculateForces();
			barrier.sync(me);
			moveBodies();
			barrier.sync(me);
		}
	}

	/*---------------------------------------------------
	 * void calculateForces()
	 *---------------------------------------------------
	 * Calculates total force for every pair of bodies.
	 *---------------------------------------------------*/
	private void calculateForces() {
		double distance, magnitude;
		Point direction;
		for (int i = chunkStart; i < chunkEnd; i++) {
			for (int j = i+1; j < planets.length; j++) {
				distance = Math.sqrt(Math.pow(planets[i].p.getX()-planets[j].p.getX(),2) +
						 Math.pow(planets[i].p.getY()-planets[j].p.getY(),2));
				magnitude = (G*planets[i].m*planets[j].m) / Math.pow(distance,2);
				direction = new Point(planets[j].p.getX()-planets[i].p.getX(), planets[j].p.getY()-planets[i].p.getY());
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
	private void moveBodies() {
		Point deltav, // dv = f/m * DT
			  deltap; // dp = (v + dv/2) * DT
		Point force = new Point(0.0,0.0);
		for (int i = chunkStart; i < chunkEnd; i++) {
			// sum the forces on body i and reset f[*,i]
			for (int k = 1; k < planets.length; k++) {
				force.setX(force.getX()+planets[i].f.getX());
				planets[i].f.setX(0.0);
				force.setY(force.getY()+planets[i].f.getY());
				planets[i].f.setY(0.0);
			}
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
	@SuppressWarnings("unused")
	private static void error (String message) {
		System.err.println("Error: " + message);
		System.exit(1);
	}
	
}
