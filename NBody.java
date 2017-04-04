/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Seqnbody.java
 *---------------------------------------------------
 * Parallel version of the n-body problem.
 *---------------------------------------------------*/

public class NBody {

	/*---------------------------------------------------
 	 * Main
 	 *---------------------------------------------------*/
	public static void main (String[] args) {
		
		// Parse console input
		int numThreads, numPlanets, size, numSteps, dt = 1;
		if (args.length < 4){
			// default settings so i dont have to type as much
			//error("java <# workers> <# bodies> <size body> <# time steps>");
			numThreads = 10;
			numPlanets = 100;
			size = 10;
			numSteps = 10000;
		}
		else{
			// Helpful in case you forget the input
			System.out.println("java <# workers> <# bodies> <size body> <# time steps>");
			numThreads = Integer.valueOf(args[0]);
			numPlanets = Integer.valueOf(args[1]);
			size = Integer.valueOf(args[2]);
			numSteps = Integer.valueOf(args[3]);
		}
		
		// Initialize the bodies, threads, and gui
		Planet[] planets = initPlanets(numPlanets);
		Barrier bar = new Barrier(numThreads+1);
		GUI gui = new GUI("NBody Problem", planets, size);
		gui.setVisible(true);
		PlanetThread[] threads = initThreads(numThreads, bar, dt, numSteps, planets);

		// start threads
		for(PlanetThread thread : threads){
			thread.start();
		}
		
		// This thread is in charge of the gui, while worker threads do the math
		for (int time = 0; time < numSteps*dt; time++) {
			//System.out.println("Thread main at time " + time);
			// wait for workers to calc forces
			bar.sync(numThreads);
			// wait for workers to move bodies
			bar.sync(numThreads);
			gui.update();
			// Sleep if you want to see the current parameters more slowly
			try {Thread.sleep(600);}catch(InterruptedException e){}
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
	 * Planet[] initPlanets(int n)
	 *---------------------------------------------------
	 * Initializes the planet array.
	 *---------------------------------------------------*/
	private static Planet[] initPlanets(int n) {
		Planet[] out = new Planet[n];
		int vxneg, vyneg, fxneg, fyneg;
		for (int i = 0; i < n; i++) {
			// TODO: Rob: all of this should be moved to the planet constructor
			vxneg = (int)(Math.random()*2);
			vyneg = (int)(Math.random()*2);
			fxneg = (int)(Math.random()*2);
			fyneg = (int)(Math.random()*2);
			out[i] = new Planet(new Point((Math.random()*1200),(Math.random()*600)),
								new Point(vxneg == 1 ? Math.random()*-10 : Math.random()*10,
										  vyneg == 1 ? Math.random()*-10 : Math.random()*10),
								new Point(fxneg == 1 ? Math.random()*-10 : Math.random()*10,
										  fyneg == 1 ? Math.random()*-10 : Math.random()*10),
								Math.random()*100000);
		}
		return out;
	}
	
	/*---------------------------------------------------
	 * PlanetThread[] initThreads()
	 *---------------------------------------------------
	 * Creates an array of threads
	 *---------------------------------------------------*/
	private static PlanetThread[] initThreads(int numThreads, Barrier bar, int dt, int numSteps, Planet[] planets){
		PlanetThread[] out = new PlanetThread[numThreads];
		for(int i=0; i<numThreads; i++){
			out[i] = new PlanetThread(numThreads, i, bar, dt, numSteps, planets);
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