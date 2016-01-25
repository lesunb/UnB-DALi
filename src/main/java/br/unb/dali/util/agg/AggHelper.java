package br.unb.dali.util.agg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import agg.xt_basis.Arc;
import agg.xt_basis.BaseFactory;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import agg.xt_basis.TypeException;

/**
 * This class provides a method repository to help deal with agg structures
 * 
 * @author abiliooliveira
 */
public final class AggHelper {
	
	/**
	 * Loads a new graph grammar from a file
	 * @param fileName
	 * @return the respective Graph Grammar
	 */
	public static GraGra loadGraGra(String resourceName) {
		if (resourceName.endsWith(".ggx")) {
			InputStream xml = Thread.currentThread().getClass().getResourceAsStream(resourceName);
	
			CustomXMLHelper h = new CustomXMLHelper();
			h.read_from_xml(xml);
	
			// create a gragra
			GraGra gra = (GraGra) h.getTopObject(BaseFactory.theFactory().createGraGra());
			return gra;
		}
		return null;
	}
	
	/**
	 * Put together the forests present in the Graph g via DFS 
	 * 
	 * @param g the full graph
	 * @return all disconnected sub graphs
	 * @throws TypeException when the types of the Graph g are inconsistent with its underlying type graph
	 */
	public static List<Graph> getForests(Graph g) throws TypeException {
		List<Graph> toReturn = new ArrayList<Graph>(); // the forest list
		HashSet<Node> parsedNodes = new HashSet<Node>(); // indicates when a node already belongs to a forest
		Set<Arc> visitedArcs = new HashSet<Arc>(); // indicates the incoming arcs that have already been visited
		
		Iterator<Node> nodeItr = g.getNodesCollection().iterator(); // to iterate over the nodes
		while (nodeItr.hasNext()) {
			Node n = nodeItr.next();
			
			 // only nodes not belonging to any discovered subgraph can become a DFS starting point 
			if (!parsedNodes.contains(n)) {
				Graph subgraph = getForest(n, g, visitedArcs); 
				toReturn.add(subgraph);
				parsedNodes.addAll(subgraph.getNodesSet());
			}
		}
		
		return toReturn;
	}

	/**
	 * Recover a graph forest of the Graph g, via depth first searching from a Node n
	 * 
	 * @param n the starting point node
	 * @param g the whole graph
	 * @param visitedArcs the arcs that have already been visited by the algorithm
	 * @return the connected subgraph from n
	 * @throws TypeException 
	 */
	public static Graph getForest(Node n, Graph g, Set<Arc> visitedArcs) throws TypeException {
		Graph toReturn = new Graph(g.getTypeSet());
		toReturn.addNode(n);
		
		Iterator<Arc> incoming = n.getIncomingArcs();
		while (incoming.hasNext()) {
			Arc arc = incoming.next();
			
			// the arcs gets "passed" if it has already been visited
			if (!visitedArcs.contains(arc)) { 
				visitedArcs.add(arc);
				Node source = (Node) arc.getSource();
				if (!source.equals(n)) // to prevent duplicate nodes when looping
					toReturn.addCopyOfGraph(getForest(source, g, visitedArcs), false);
				toReturn.addArc(arc);
			}
		}
		
		Iterator<Arc> outgoing = n.getOutgoingArcs();
		while (outgoing.hasNext()) {
			Arc arc = outgoing.next();
			
			// the arcs gets "passed" if it has already been visited
			if (visitedArcs.contains(arc)) {
				visitedArcs.add(arc);
				Node target = (Node) arc.getTarget();
				if (!target.equals(n)) // to prevent duplicate nodes when looping
					toReturn.addCopyOfGraph(getForest(target, g, visitedArcs), false);
				toReturn.addArc(arc);
			}
		}
		
		return toReturn;
	}

	/**
	 * Gets all the source nodes of this incomingArcs iterator
	 * 
	 * @param incomingArcs
	 * @return an ArrayList of source nodes
	 */
	public static ArrayList<Node> getSourceNodes(Iterator<Arc> incomingArcs) {
		ArrayList<Node> toReturn = new ArrayList<Node>();
		
		while (incomingArcs.hasNext()) {
			toReturn.add((Node)incomingArcs.next().getSource());
		}
		
		return toReturn;
	}

	/**
	 * Gets all the target nodes of this outgoingArcs iterator
	 * 
	 * @param outgoingArcs
	 * @return an ArrayList of target nodes
	 */
	public static ArrayList<Node> getTargetNodes(Iterator<Arc> outgoingArcs) {
		ArrayList<Node> toReturn = new ArrayList<Node>();
		
		while (outgoingArcs.hasNext()) {
			toReturn.add((Node)outgoingArcs.next().getTarget());
		}
		
		return toReturn;
	}
	
}
