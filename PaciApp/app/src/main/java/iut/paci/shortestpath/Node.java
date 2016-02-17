package iut.paci.shortestpath;

import org.mapsforge.core.model.LatLong;



public class Node extends LatLong {

	public long id;
	public String name;
	public int level;

	public Node(long id, String name, double longitude, double latitude,
			int level) {
		super(latitude,longitude);
		this.id = id;
		this.name = name;
		this.level = level;
	}
	 
	  
	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    Node other = (Node) obj;
	    if (id != other.id)
	      return false;
	    return true;
	  }

//	  @Override
//	  public String toString() {
//	    return name;
//	  }
	
}
