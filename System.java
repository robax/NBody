
public class System {
	Planet[] system;
	PlanetThread[] threads;
	Barrier barrier;
	
	public System(int planetCount, int threadCount){
		system = initPlanets(planetCount);
		barrier = new Barrier(threadCount);
		threads = initThreads(threadCount, barrier);
	}
	
	public Planet[] initPlanets(int number){
		Planet[] out = new Planet[number];
		for(int i=0; i<number; i++){
			out[i] = new Planet();
		}
		return out;
	}
	
	public PlanetThread[] initThreads(int number, Barrier bar){
		PlanetThread[] out = new PlanetThread[number];
		for(int i=0; i<number; i++){
			out[i] = new PlanetThread(number, bar);
		}
		return out;
	}
	
}
