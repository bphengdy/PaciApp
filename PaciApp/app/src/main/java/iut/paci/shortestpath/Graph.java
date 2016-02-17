package iut.paci.shortestpath;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

public class Graph {

	public ArrayList<Node> Nodes;
	public ArrayList<Edge> Edges;

	public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
		Nodes = nodes;
		Edges = edges;
	}

	public ArrayList<Node> shorterPath(Node A, Node B) {

		
		Algorithm dijkstra = new Algorithm(this);
		
		dijkstra.execute(A);
	    LinkedList<Node> path = dijkstra.getPath(B);
	    ArrayList<Node> result = new ArrayList<Node>();
	    if(path==null)
	    	return null;
	    for (Node node : path) {
			result.add(node);
		}
		return result;
	}

	public Node getNodeByName(String name) {
		for (Node node : Nodes) {
			if (node.name.equals(name))
				return node;
		}
		return null;
	}

	public Node getNodeById(long id) {
		for (Node node : Nodes) {
			if (node.id == id)
				return node;

		}
		return null;
	}

	public static Graph getGraphFromOsm(String path) {

		long id;
		double longitude, latitude;
		String name;
		try {

			Graph graph = new Graph(new ArrayList<Node>(),
					new ArrayList<Edge>());

			Document doc = parseXML(new File(path));
			Element racine = doc.getRootElement();
			// traitement des nodes

			Iterator it = racine.elementIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.getName().equals("node")) {
					Iterator itNode = elem.elementIterator();
					if (itNode.hasNext()) {
						Element elemTag = (Element) itNode.next();
						// System.out.println(elemTag.attribute(new
						// QName("k")).getValue());
						if (elemTag.getName().equals("tag")
								&& elemTag.attribute(new QName("k")).getValue()
										.equals("SmartNode")) {
//										.equals("smartcampus")) {

							id = Long.parseLong(elem.attribute(
									new QName("id")).getValue());
							longitude = Double.parseDouble(elem.attribute(
									new QName("lon")).getValue());
							latitude = Double.parseDouble(elem.attribute(
									new QName("lat")).getValue());
							name = elemTag.attribute(new QName("v")).getValue();
							graph.Nodes.add(new Node(id, name, longitude,
									latitude, 0));

						}

					}

				}

				// traitement des ways
				if (elem.getName().equals("way")) {
					Iterator itWay = elem.elementIterator();
					while (itWay.hasNext()) {
						Element elemTag = (Element) itWay.next();
						if (elemTag.getName().equals("nd")) {
							long tempid = Long.parseLong(elemTag.attribute(
									new QName("ref")).getValue());
							if (graph.getNodeById(tempid) != null) {
								Node src = graph.getNodeById(tempid);

								while (itWay.hasNext()) {
									Element elemTagNd = (Element) itWay.next();
									if (elemTagNd.getName().equals("nd")) {
										Node dest = graph.getNodeById(Integer.parseInt(elemTagNd.attribute(	new QName("ref")).getValue()));
										if (dest == null)
											break;

										Edge edge = new Edge(src, dest,Edge.getDistance(src, dest));
										graph.Edges.add(edge);
//										System.out.println(edge.src.id + ", " + edge.dest.id + ", "+ edge.distance);
										src = dest;
									}
								}

							}
						}

					}
				}

				// System.out.println(elem.getName());
			}

			// for (Node node : graph.Nodes) {
			// System.out.println(node.name+", "+node.id+", "+node.longitude+", "+node.latitude);
			// }

			System.out.println(graph.Nodes.size()+  ", " + graph.Edges.size());
//			for (Edge edge : graph.Edges) {
//				System.out.println(edge.src.id + ", " + edge.dest.id + ", "+ edge.distance);
//			}

			return graph;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;// TODO: handle exception
		}

	}

	public static Document parseXML(File url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

}
