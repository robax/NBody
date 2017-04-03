import java.awt.Point;

public class Planet {
	private Point p, f, v;
	private double m;
	private String color;
	private Planet[] sys;
	
	public final static double G = 0.0000000000667;
	
	public Planet(){
		
	}
	
	public void tick(Planet[] system){
		
	}
	
	public void draw(){
		
	}
	
	public void calculateForces(){
		double distance, magnitude;
		Point direction;
		
		//for(int i=0; i<){
		//	for(int j
		distance = Math.sqrt( Math.pow(sys[i].p.x - sys[j].p.x) +
							  Math.pow(sys[i].p.y - sys[j].p.y));
		magnitude = (G * sys[i].m.x)
	}
	
}
