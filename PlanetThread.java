public class PlanetThread implements Runnable{
	Thread runner;
	int me;
	Barrier barrier;
	
	public final static double G = 0.0000000000667;
	public static Point[] p, v, f;
	public static double[] m;
	public static int n, dt, numThreads, numSteps;
	private int iteration = 0;
	public static GUI gui;
	
	// inclusive
	private static int chunkStart;
	// exclusive
	private static int chunkEnd;
	
	@SuppressWarnings("static-access")
	public PlanetThread(int numThreads, int me, Barrier barrier, Point[] p, Point[] v, 
						Point[] f, double[] m, int n, int dt, GUI gui, int numSteps){
		runner = new Thread(this);
		this.me = me;
		this.barrier = barrier;
		this.p = p;
		this.v = v;
		this.f= f;
		this.m = m;
		this.n= n;
		this.dt = dt;
		this.gui = gui;
		this.numThreads = numThreads;
		this.numSteps = numSteps;
		
		int chunkSize = p.length / numThreads;
		chunkStart = chunkSize * me;
		chunkEnd = chunkSize * (me+1);
		
		// Pretty sure this check is necessary to avoid off-by-remainder errors
		if(me==numThreads-1){
			chunkEnd = p.length;
		}
	}

	@Override
	public void run() {
		for (int time = 0; time < numSteps*dt; time++) {
			System.out.println("Thread " + me + " on iteration " + iteration);
			calculateForces();
			barrier.sync(me);
			moveBodies();
			barrier.sync(me);
			iteration++;
		}
	}

	public void start(){
		runner.start();
	}
	
	public void join(){
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	public static void error (String message) {
		System.err.println("Error: " + message);
		System.exit(1);
	}
	
}
