/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Seqnbody.java
 *---------------------------------------------------
 * Parallel version of the n-body problem.
 *---------------------------------------------------*/

public class NBody {

	public static int workers, n, size, numSteps,dt;
	public static Point[] p, v, f;
	public static double[] m = new double[n];
	public static final double g = Math.pow(6.67,-11);
	public static PlanetThread[] threads;
	public static GUI gui;
	public static Barrier bar;

	/*---------------------------------------------------
 	 * Main
 	 *---------------------------------------------------*/
	public static void main (String[] args) {
		
		
		// Parse console input
		if (args.length < 4){
			// default settings so i dont have to type as much
			//error("java <# workers> <# bodies> <size body> <# time steps>");
			workers = 10;
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
		
		// Initialize the bodies, threads, and gui
		p = new Point[n];
		v = new Point[n];
		f = new Point[n];
		m = new double[n];
		initPVFM();
		bar = new Barrier(workers+1);
		GUI gui = new GUI("NBody Problem", p, size);
		gui.setVisible(true);
		
		threads = initThreads();

		// start threads
		for(PlanetThread thread : threads){
			thread.start();
		}
		
		int iteration = 0;
		// This thread is in charge of the gui, worker threads do the math
		for (int time = 0; time < numSteps*dt; time++) {
			System.out.println("Thread main on iteration " + iteration);
			// wait for workers to calc forces
			bar.sync(workers);
			// wait for workers to move bodies
			bar.sync(workers);
			gui.update(p);
			// Sleep if you want to see the current parameters more slowly
			try {Thread.sleep(300);}catch(InterruptedException e){}
			iteration++;
		}
		
		for(PlanetThread thread : threads){
			thread.join();
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
	public static void initPVFM() {
		for (int i = 0; i < n; i++) {
			p[i] = new Point((Math.random()*20)+size,(Math.random()*20)+size);
			v[i] = new Point(i,i);
			f[i] = new Point(69,i*2);
			m[i] = i;
		}
	}
	
	public static PlanetThread[] initThreads(){
	
		PlanetThread[] out = new PlanetThread[workers];
		for(int i=0; i<workers; i++){
			// lol look at this constructor
			out[i] = new PlanetThread(workers, i, bar, p, v, f, m, n, dt, gui, numSteps);
		}
		return out;
	}
	
}