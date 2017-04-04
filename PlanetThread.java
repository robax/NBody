/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * PlanetThread.java
 *---------------------------------------------------
 * Basic thread in charge of some portion of the planets
 *---------------------------------------------------*/

public class PlanetThread extends Thread{
	
	private int me;
	private Barrier barrier;
	private final static double G = 0.0000000000667;
	private static Point[] p, v, f;
	private static double[] m;
	private static int n, dt, numSteps;
	private static int chunkStart, chunkEnd;
	
	/*---------------------------------------------------
	 * PlanetThread()
	 *---------------------------------------------------
	 * Thread constructor
	 *---------------------------------------------------*/
	@SuppressWarnings("static-access")
	public PlanetThread(int numThreads, int me, Barrier barrier, Point[] p, Point[] v, 
						Point[] f, double[] m, int n, int dt, int numSteps){
		this.me = me;
		this.barrier = barrier;
		this.p = p;
		this.v = v;
		this.f = f;
		this.m = m;
		this.n = n;
		this.dt = dt;
		this.numSteps = numSteps;
		
		int chunkSize = p.length / numThreads;
		chunkStart = chunkSize * me;
		chunkEnd = chunkSize * (me+1);
		
		// Pretty sure this check is necessary to avoid off-by-remainder errors
		// Note we ignore the last element for some reason. Per book pseudocode.
		if(me==numThreads-1){
			chunkEnd = n-1;
		}
		
		// Because we start at 1 for some reason?
		if(me==0){
			chunkStart = 1;
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
			System.out.println("Thread " + me + " at time " + time);
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
	private static void calculateForces() {
		double distance, magnitude;
		Point direction;
		for (int i = chunkStart; i < chunkEnd; i++) {
			for (int j = i+1; j < n; j++) {
				distance = Math.sqrt(Math.pow(p[i].getX()-p[j].getX(),2) +
									 Math.pow(p[i].getY()-p[j].getY(),2));
				magnitude = (G*m[i]*m[j]) / Math.pow(distance,2);
				direction = new Point(p[j].getX()-p[i].getX(), p[j].getY()-p[i].getY());
				f[i].setX(f[i].getX()+magnitude*direction.getX()/distance);
				f[j].setX(f[j].getX()-magnitude*direction.getX()/distance);
				f[i].setY(f[i].getY()+magnitude*direction.getY()/distance);
				f[j].setY(f[j].getY()-magnitude*direction.getY()/distance);
			}
		}
	}

	/*---------------------------------------------------
	 * void moveBodies
	 *---------------------------------------------------
	 * Calculate new velocity and position for each body.
	 *---------------------------------------------------*/
	private static void moveBodies() {
		Point deltav, // dv = f/m * DT
			  deltap; // dp = (v + dv/2) * DT
		
		// This function isnt parallelized yet.
		// The loop should look like this but it doesnt work:
		for (int i = chunkStart; i < chunkEnd; i++) {
		//for (int i = 0; i < n; i++) {
			deltav = new Point(f[i].getX()/m[i]*dt,f[i].getY()/m[i]*dt);
			deltap = new Point((v[i].getX()+deltav.getX()/2)*dt,
							   (v[i].getY()+deltav.getY()/2)*dt);
			v[i].setX(v[i].getX()+deltav.getX());
			v[i].setY(v[i].getY()+deltav.getY());
			p[i].setX(p[i].getX()+deltap.getX());
			p[i].setY(p[i].getY()+deltap.getY());
			f[i].move(0.0,0.0); // reset force vector
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
