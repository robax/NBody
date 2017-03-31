// Robert Walters
// CSC 422
// Program 2

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Barrier {
	AtomicInteger count = new AtomicInteger(0);
	int numProc;
	Semaphore mutex;
	Semaphore[] barrier;

	public Barrier(int numProc){
		this.numProc = numProc;
		barrier = initSemaphores(0);
		mutex = new Semaphore(1);
	}
	
	// This is the barrier method
	// My implementation is NOT *exactly* correct, as each process is essentially
	// assigned a partner that it wakes up each time, which is the process in 
	// front of it. So there's only one "stage", but it works wonderfully!
	// And it's simple!
	
	// The count variable could also be replaced by a synchronized int,
	// which would also allow for the removal of the mutex. But if it ain't
	// broke...
	public void sync(int me){
		// this flag ensures we only wake up a single thread each iteration
		boolean flag = false;
		
		// increment count, indicating that we have reached the barrier
		count.incrementAndGet();
		
		// the last thread to reach the barrier will wake up the next process,
		// which begins the cascade that permits all the processes to move
		// beyond the barrier
		if(count.intValue()==numProc){
			barrier[(me+1)%numProc].release();
			count.set(0);;
			flag = true;
		}
		
		try {
			barrier[me].acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!flag)
			barrier[(me+1)%numProc].release();
	}
	
	// Just an array of semaphores, where num is the number of permits
	// per semaphore
	private Semaphore[] initSemaphores(int num) {
		Semaphore arrive[] = new Semaphore[numProc];
		for(int i=0; i<numProc; i++){
			arrive[i] = new Semaphore(num, true);
		}
		return arrive;
	}
	
}