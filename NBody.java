/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * NBody.java
 *---------------------------------------------------
 * Parallel version of the n-body problem.
 *---------------------------------------------------*/

import java.lang.System;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NBody {

	/*---------------------------------------------------
 	 * Main
 	 *---------------------------------------------------*/
	public static void main (String[] args) {
		
		// Parse console input
		int numThreads, numPlanets, size, numSteps, dt = 1;
		boolean graphicsOn = true;
		GUI gui;
		if (args.length < 5){
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
			if(args[4]=="0"){
				graphicsOn = false;
			}
		}
		
		// Initialize the bodies, threads, and gui
		Planet[] planets = initPlanets(numPlanets);
		Barrier bar = new Barrier(numThreads+1);
			gui = new GUI("NBody Problem", planets);
		if(graphicsOn){
			gui.setVisible(true);
		}
		PlanetThread[] threads = initThreads(numThreads, bar, dt, numSteps, planets);

		/*---------------------------------------------------
	 	 * Start Timer
		 *---------------------------------------------------*/
		long start = System.nanoTime();

		// start threads
		for(PlanetThread thread : threads){
			thread.start();
		}
		
		// This thread is in charge of the gui, while worker threads do the math
		for (int time = 0; time < numSteps*dt; time++) {
			//System.out.println("Thread main at time " + time);
			// wait for workers to detect collisions
			bar.sync(numThreads);
			// wait for workers to calc forces
			bar.sync(numThreads);
			// wait for workers to move bodies
			bar.sync(numThreads);
			if(graphicsOn){
				gui.update();
			}
			// Sleep if you want to see the current parameters more slowly
			//try {Thread.sleep(600);}catch(InterruptedException e){}
		}
	
		// join threads
		int collisions = 0;
		for(PlanetThread thread : threads){
			collisions += thread.getCollisions();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long end = System.nanoTime();
		/*---------------------------------------------------
	 	 * End Timer
	 	 *---------------------------------------------------*/
		long seconds = (end-start)/1000000000, 
			 microseconds = ((end-start)%1000000000)/1000000;

		System.out.printf("Computation time: %d seconds %d milliseconds\n",seconds,microseconds);
		System.out.printf("Number of collisions detected: " + collisions + "\n");
		try {
			FileWriter file = new FileWriter("results.txt");
			BufferedWriter br = new BufferedWriter(file);
			int i = 1;
			for (Planet planet : planets) {
				if (i == 1) br.write("----------------------------------------------------\n");
				br.write("Planet " + i + "\n");
				br.write("Position: " + planet.p.toString() + "\n");
				br.write("Velocity: " + planet.v.toString() + "\n");
				br.write("----------------------------------------------------\n");
				i++;
			}
			br.close();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/*---------------------------------------------------
	 * Planet[] initPlanets(int n)
	 *---------------------------------------------------
	 * Initializes the planet array.
	 *---------------------------------------------------*/
	private static Planet[] initPlanets(int n) {
		Planet[] out = new Planet[n];
		for (int i = 0; i < n; i++) {
			out[i] = new Planet();
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