/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Seqnbody.java
 *---------------------------------------------------
 * Sequential version of the n-body problem.
 *---------------------------------------------------*/

import java.lang.System;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Seqnbody {

	public static int workers, n, size, numSteps, dt, collisions, displayGui;
	public static Planet[] planets;
	public static final double g = Math.pow(6.67,-11);

	/*---------------------------------------------------
 	 * Main
 	 *---------------------------------------------------*/
	public static void main (String[] args) {
		if (args.length < 4){
			// default settings so i dont have to type as much
			//error("java <# workers> <# bodies> <size body> <# time steps> <use graphics>a");
			workers = 1;
			n = 100;
			size = 10;
			numSteps = 10000;
			dt = 1;	
			displayGui = 1;
		}
		else{
			// Helpful in case you forget the input
			//System.out.println("java <# workers> <# bodies> <size body> <# time steps> <display gui>");

			workers = Integer.valueOf(args[0]);
			n = Integer.valueOf(args[1]);
			size = Integer.valueOf(args[2]);
			numSteps = Integer.valueOf(args[3]);
			dt = 1;
			if (args.length == 5) {
				displayGui = Integer.valueOf(args[4]);
			} else {
				displayGui = 1;
			}
		}

		planets = initPlanets(n);

		/*---------------------------------------------------
	 	 * Start Timer
		 *---------------------------------------------------*/
		long start = System.nanoTime();
		if (displayGui == 1) {
			runWithGUI(numSteps, dt, planets);
		} else {
			runNoGUI(numSteps, dt, planets);
		}
		long end = System.nanoTime();
		/*---------------------------------------------------
	 	 * End Timer
	 	 *---------------------------------------------------*/
		long seconds = (end-start)/1000000000, 
			 microseconds = ((end-start)%1000000000)/1000000;

		System.out.printf("Computation time: %d seconds %d milliseconds\n",seconds,microseconds);
		System.out.printf("Number of collisions detected %d\n",collisions);
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
		System.exit(0);
	}

	private static void runWithGUI(int numSteps, int dt, Planet[] planets){
		GUI gui = new GUI("NBody Problem", planets);
		gui.setVisible(true);
		for (int time = 0; time < numSteps*dt; time++) {
			calculateForces();
			moveBodies();
			detectCollisions();
			gui.update();
			// Sleep if you want to see the current parameters more slowly
			//try {Thread.sleep(100);}catch(InterruptedException e){}
		}		
	}
	
	private static void runNoGUI(int numSteps, int dt, Planet[] planets){
		for (int time = 0; time < numSteps*dt; time++) {
			calculateForces();
			moveBodies();
			detectCollisions();
			// Sleep if you want to see the current parameters more slowly
			//try {Thread.sleep(100);}catch(InterruptedException e){}
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
			//planets[i].v.move(planets[i].v.getX()+deltav.getX(),planets[i].v.getY()+deltav.getY());
			planets[i].p.setX(planets[i].p.getX()+deltap.getX());
			planets[i].p.setY(planets[i].p.getY()+deltap.getY());
			//planets[i].p.move(planets[i].p.getX()+deltap.getX(),planets[i].p.getY()+deltap.getY());
			planets[i].f.move(0.0,0.0); // reset force vector
		}
	}

	/*---------------------------------------------------
	 * void detectCollisions()
	 *---------------------------------------------------
	 * Detect collisions and let another function handle it. 
	 *---------------------------------------------------*/
	public static void detectCollisions(){
		for (int i = 0; i < n-1; i++) {
			for (int k = i+1; k < n; k++) {
				double dx = Math.abs(planets[i].p.getX() - planets[k].p.getX());
				double dy = Math.abs(planets[i].p.getY() - planets[k].p.getY());
				double distance = Math.sqrt(dx*dx+dy*dy);
				
				if(distance < planets[i].radius + planets[k].radius){
					//System.out.println("Planets " + i + " and " + k + " collided!");
					collisions++;
					handleCollisions(planets[i], planets[k]);
				}
			}
		}
	}

	/*---------------------------------------------------
	 * void handleCollisions(Planet one, Planet two)
	 *---------------------------------------------------
	 * Calculate new velocity and position for collisions.
	 *---------------------------------------------------*/
	public static void handleCollisions(Planet one, Planet two){
		double x2 = two.p.getX(),
		 	   y2 = two.p.getY(),   
			   x1 = one.p.getX(),
			   y1 = one.p.getY(), 
			   v2x = two.v.getX(),
			   v2y = two.v.getY(),
			   v1x = one.v.getX(),
			   v1y = one.v.getY();
		one.v.setX(((v2x*Math.pow(x2-x1,2))+
				  (v2y*(x2-x1)*(y2-y1))+
				  (v1x*Math.pow(y2-y1,2))-
				  (v1y*(x2-x1)*(y2-y1)))/
				  (Math.pow(x2-x1,2)+
				   Math.pow(y2-y1,2))
				);
		one.v.setY(((v2x*(x2-x1)*(y2-y1))+
				    (v2y*Math.pow(y2-y1,2))-
				    (v1x*(y2-y1)*(x2-x1))+
				    (v1y*Math.pow(x2-x1,2)))/
					(Math.pow(x2-x1,2)+
					 Math.pow(y1-y2,2))
				);
		two.v.setX((v1x*Math.pow(x2-x1,2)+
				   (v1y*(x2-x1)*(y2-y1))+
				   (v2x*Math.pow(y2-y1,2))-
				   (v2y*(x2-x1)*(y2-y1)))/
				   (Math.pow(x2-x1,2)+
				   	Math.pow(y2-y1,2))
				);
		two.v.setY(((v1x*(x2-x1)*(y2-y1))+
				   (v1y*Math.pow(y2-y1,2))-
				   (v2x*(y2-y1)*(x2-x1))+
				   (v2y*Math.pow(x2-x1,2)))/
				   (Math.pow(x2-x1,2)+
				   	Math.pow(y2-y1,2))
			);
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