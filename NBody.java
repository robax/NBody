/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Seqnbody.java
 *---------------------------------------------------
 * Parallel version of the n-body problem.
 *---------------------------------------------------*/

public class NBody {

	// Command line arguments (except dt
	public static int workers, n, size, numSteps, dt = 1;
	public static Point[] p, v, f;
	public static double[] m = new double[n];
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
		}
		else{
			// Helpful in case you forget the input
			System.out.println("java <# workers> <# bodies> <size body> <# time steps>");

			workers = Integer.valueOf(args[0]);
			n = Integer.valueOf(args[1]);
			size = Integer.valueOf(args[2]);
			numSteps = Integer.valueOf(args[3]);
		}
		
		// Initialize the bodies, threads, and gui
		p = new Point[n];
		v = new Point[n];
		f = new Point[n];
		m = new double[n];

		bar = new Barrier(workers+1);
		gui = new GUI("NBody Problem", p, size);
		gui.setVisible(true);

		initPVFM();
		gui.update(p);
		
		threads = initThreads();

		// start threads
		for(PlanetThread thread : threads){
			thread.start();
		}
		
		// This thread is in charge of the gui, worker threads do the math
		for (int time = 0; time < numSteps*dt; time++) {
			System.out.println("Thread main at time " + time);
			// wait for workers to calc forces
			bar.sync(workers);
			// wait for workers to move bodies
			bar.sync(workers);
			gui.update(p);
			// Sleep if you want to see the current parameters more slowly
			try {Thread.sleep(300);}catch(InterruptedException e){}
		}
	
		// join threads
		for(PlanetThread thread : threads){
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
	private static void initPVFM() {
		int vxneg, vyneg, fxneg, fyneg;
		for (int i = 0; i < n; i++) {
			vxneg = (int)(Math.random()*2);
			vyneg = (int)(Math.random()*2);
			fxneg = (int)(Math.random()*2);
			fyneg = (int)(Math.random()*2);
			p[i] = new Point((Math.random()*1200),(Math.random()*600));
			v[i] = new Point(vxneg == 1 ? Math.random()*-10 : Math.random()*10,
							 vyneg == 1 ? Math.random()*-10 : Math.random()*10);
			f[i] = new Point(fxneg == 1 ? Math.random()*-10 : Math.random()*10,
							 fyneg == 1 ? Math.random()*-10 : Math.random()*10);
			m[i] = Math.random()*100000;
		}
	}
	
	/*---------------------------------------------------
	 * PlanetThread[] initThreads()
	 *---------------------------------------------------
	 * Creates an array of threads
	 *---------------------------------------------------*/
	private static PlanetThread[] initThreads(){
		PlanetThread[] out = new PlanetThread[workers];
		for(int i=0; i<workers; i++){
			out[i] = new PlanetThread(workers, i, bar, p, v, f, m, n, dt, numSteps);
		}
		return out;
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