
public class PlanetThread implements Runnable{
	Thread runner;
	int me;
	Barrier barrier;
	
	public PlanetThread(int me, Barrier barrier){
		runner = new Thread(this);
		this.me = me;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		
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
	

	
}
