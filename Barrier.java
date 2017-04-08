/*---------------------------------------------------
 * Tanner Bernth
 * Robert Walters
 * Barrier.java
 *---------------------------------------------------
 * Barrier to synchronize the threads
 *---------------------------------------------------*/

import java.util.concurrent.Semaphore;

public class Barrier {
	
	int count = 0;
	int numProc;
	Semaphore mutex;
	Semaphore[] barrier;

	/*---------------------------------------------------
	 * Barrier()
	 *---------------------------------------------------
	 * Barrier constructor
	 *---------------------------------------------------*/
	public Barrier(int numProc){
		this.numProc = numProc;
		barrier = initSemaphores(0);
		mutex = new Semaphore(1);
	}
	
	/*---------------------------------------------------
	 * void sync(int me)
	 *---------------------------------------------------
	 * Basic dissmination barrier.
	 *---------------------------------------------------*/
	public void sync(int me){
		// this flag ensures we only wake up a single thread each iteration
		boolean flag = false;
		
		// increment count, indicating that we have reached the barrier
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		count++;
		
		// the last thread to reach the barrier will wake up the next process,
		// which begins the cascade that permits all the processes to move
		// beyond the barrier
		if(count==numProc){
			barrier[(me+1)%numProc].release();
			count = 0;
			flag = true;
		}
		mutex.release();
		
		try {
			barrier[me].acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!flag){
			barrier[(me+1)%numProc].release();
		}
	}
	
	/*---------------------------------------------------
	 * Semaphore[] initSemaphores(int num)
	 *---------------------------------------------------
	 * Creates an array of semaphores numProc long, with
	 * starting value num 
	 *---------------------------------------------------*/
	private Semaphore[] initSemaphores(int num) {
		Semaphore arrive[] = new Semaphore[numProc];
		for(int i=0; i<numProc; i++){
			arrive[i] = new Semaphore(num, true);
		}
		return arrive;
	}
	
}