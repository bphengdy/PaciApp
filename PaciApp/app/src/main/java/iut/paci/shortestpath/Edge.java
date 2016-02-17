package iut.paci.shortestpath;

public class Edge {

	
	private  String id; 
	
	public Node src;
	public Node dest;
	
	public double distance;
	
	
	
	
	public Edge(Node src, Node dest, double distance) {
		this.src = src;
		this.dest = dest;
		this.distance = distance;
	}





	public static  double getDistance(Node src, Node dest)
	{
		
		return Math.sqrt(Math.pow(src.longitude-dest.longitude,2)+Math.pow(src.latitude-dest.latitude,2));
	}
	
	
}
