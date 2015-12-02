package degreeSelect;

import gr.forth.ics.graph.Graph;
import gr.forth.ics.graph.Node;
import gr.forth.ics.graph.PrimaryGraph;
import gr.forth.ics.graph.metrics.BrandesMetrics;
import gr.forth.ics.graph.metrics.NodeMetric;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import kcores_algorithm.KeyFinder;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
* The class biasRandomWalk implements the baseline algorithm. 
* In this very basic and simple algorithm we select the prominent nodes based on two criterion:
* Closeness measure
* Degree measure
* 
* The performance/results of this algorithm are used to quantify any added value of the more complex algorithms.
*  
* @author axm1064  ID: 1402590
* @since 08/09/2014
 */

public class DegreeSelect {
	
	private String jsonText;
	private HashMap<String,degNode> node = new HashMap<String,degNode>();	
	private HashMap<degNode, String> node_key = new HashMap<degNode, String>();	
	
	
	private Graph g = new PrimaryGraph(); 
	
	private int nbrNodes; 
	private int nbrEdges;

	private int nbrPromNodes;

/**
 * Constructor with two parameters or arguments
 * @param strInput is of the String type. The data received from the client or user. Simply the data input in String format JSON.
 * @param nbrPromNodes of the integer type. For future use. If the user want to select the n best prominent nodes.
 * For now the server sends the whole profile to the user.
 */
	public DegreeSelect(String strInput ){
		this.jsonText = strInput;
		parseJSONtext();
		this.nbrPromNodes = g.nodeCount();
	}
	
	/**
	 * This private method parseJSONtext is called by the constructor to set certain private parameters 
	 * and construct the graph and other data Structures.
	 */
	private void parseJSONtext(){
		
		// The following LinkedList are buffers, used to retrieve the data elements that we are interested in.
		  LinkedList< String> temp1 = new LinkedList<String>();
		  LinkedList< String> temp2 = new LinkedList<String>();
	
		  // From the text loaded, we extract the vertices, their ids and names.
		  temp1 =  find(jsonText, "id");
		  temp2 =  find(jsonText, "name");
		  // Next we store them in the node HashMap data structure
		  for(int i=0; i < temp2.size(); i++){ 
			  Node n = g.newNode(temp2.get(i));
			  
			  degNode clsdegNode = new degNode(n);
					  
			  this.node.put( temp1.get(i) , clsdegNode ); 
			  this.node_key.put(  clsdegNode  ,  temp1.get(i)   ); // Need to override redefine the equals methods.
			  
		  }
		  
		  // Set the number of edges in the network loaded.
		  this.nbrEdges = this.g.edgeCount();
		// Set the number of vertices in the network loaded
		  this.nbrNodes = this.g.nodeCount();
		  		 
		  // Similarly, for the edges of the graph, we store the source and destination in the graph g
		  temp1 =  find(jsonText, "source");
		  temp2 =  find(jsonText, "target");
		  for(int i=0;  i < temp1.size(); i++){
			  this.g.newEdge(    node.get( temp1.get(i) ).getNode()		, node.get( temp2.get(i) ).getNode()	);
		  }
		  
		  // Once I added the all the nodes and edges, then I can question the graph to retrieve the degree of each node (Strength)
		  // and store it in node data structure.
		  for(degNode n : this.node_key.keySet()){
			  
			  this.node.get(this.node_key.get(n)).setStrenght(g.degree(n.getNode()));
			  
			  this.node.get( 
					  this.node_key.get(n)).setCloseness(
							  BrandesMetrics.execute(g, false).getCloseness()
							  );
				  
		  }
		  
		  	
	}
	
		
	/**
	 * The method degToJson construct the JSON String text object to send through the network to the user.
	 * @return String the JSON object to send back to the user.
	 */
	public String degToJson(){
		
		// Retrieve all the degNode object and sort, order them
		Collection<degNode>	colnodes =   node.values();
		Object[] arrNode =  colnodes.toArray();
		
		Arrays.sort(arrNode);
		

		// First I add the header, the algorithm name
		 StringBuilder str = new StringBuilder("{\"degreeProfile\": {");
			 // Next I add the nodes
			   str.append("\"promNode" + "\":[ " );
			   for (int i=0; i<this.nbrPromNodes; i++)
		   			str.append( "{\"data\":{\"id\":\"" +  
			   ((degNode) arrNode[i]).getNode()  + "\",\"name\":\"" +  ((degNode) arrNode[i]).getNode() 
			  // +
			  // "\",\"profCorePeriph\":\"" +  ((degNode) arrNode[i]).getCorePeriphProf()
			   + "\",\"strength\":\"" +  ((degNode) arrNode[i]).getStrenght() 
			   
			   	   + "\",\"closeness\":\"" +  ((degNode) arrNode[i]).getCloseness().getValue( ((degNode) arrNode[i]).getNode()) 

			   + "\"}},");
		   		
		   		str.replace(str.length() -1, str.length(), "],");   			
			//*****************************************************************************	  
		   	// then I add the edges	
		   	 str.append("\"edges\":[") ;
		   	 
		   	 int dim = str.length();
		   	// System.out.println(dim + "  " + str);
		   	 
		   	 for (int i=0; i<this.nbrPromNodes; i++)
		   		 for (int j=i+1; j<this.nbrPromNodes; j++){
		   			 Node node1 = ((degNode) arrNode[i]).getNode();
		   			 Node node2 = ((degNode) arrNode[j]).getNode() ;
		   			 if (  g.areAdjacent(  node1 , node2 ) ) {
		   				 
		   			  str.append("{\"data\":{\"source\":\"" +  node1 
							   	+ "\",\"target\":\"" +  node2
							   	+ "\"}},"	);	
		   				 
		   			 }
		   		 }
		  // 	 System.out.println(str); 
		   	if (dim==str.length())
		   		str.replace(str.length() - ("],\"edges\":[").length(), str.length(), "],");
		   	else
		   		str.replace(str.length() -1, str.length(), "],");
		   	//System.out.println(str.length() + " " + str); 	
		   			   
		   //*****************************************************************************
		   
		   str.replace(str.length() -1, str.length(), "");
		   str.append( "}} ");
				
		return str.toString();
	}


	
public LinkedList<String> find(String str, String key){
	
	LinkedList<String> retList = new LinkedList();
	
	  JSONParser parserr = new JSONParser();
	  KeyFinder finder = new KeyFinder();
	  finder.setMatchKey(key); // source, name, id, target
	  try{
	    while(!finder.isEnd()){
	      parserr.parse(str, finder, true);
	      if(finder.isFound()){
	        finder.setFound(false);
	        retList.add(finder.getValue().toString());
	      }
	    }           
	  }
	  catch(ParseException pe){
	    pe.printStackTrace();
	  }
 
	  return retList;		
}


}



/**
 * Each node in the graph is associated to an instance of this class 
 * This allows us to keep track of its degree or strength.
 *
 */
class degNode implements Comparable<degNode>, Comparator<degNode>{
	

	private Node node; // vertex
	private double strenght;
	private NodeMetric closeness;
		
	// constructor where we set the node and initiate the counter to zero.
	degNode(Node n){
		this.node = n;
		this.strenght = 0.0;
	}
	
	// Some getters and setters
	public double getStrenght(){
		return this.strenght;
	}
	
	public void setStrenght(double dblStrenght){
		 this.strenght = dblStrenght ;
	}
	
	public NodeMetric getCloseness(){
		return this.closeness;
	}
	
	public void setCloseness(NodeMetric cln){
		 this.closeness = cln ;
	}
		
	public Node getNode(){
		return this.node;
	}

	// I implement the Comparable interface. The method compareTo allows me to order and short the degNode instances.
	@Override
	public int compareTo(degNode o) {
		
	return ( this.getCloseness().getValue(this.getNode())  >    o.getCloseness().getValue(o.getNode()) ) ? -1 : 
		
		(	this.getCloseness().getValue(this.getNode())  <   o.getCloseness().getValue(o.getNode()) ) ? 1 :
			
			((int)this.getStrenght() > (int)o.getStrenght())? -1:((int)this.getStrenght() < (int)o.getStrenght())? 1:0;		
	
	}
	
	

	// Not used in case I want to shorted in another way.
	@Override
	public int compare(degNode arg0, degNode arg1) {
		
		return ( (int)arg0.getStrenght() - (int)arg1.getStrenght());
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		degNode other = (degNode) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	
}
