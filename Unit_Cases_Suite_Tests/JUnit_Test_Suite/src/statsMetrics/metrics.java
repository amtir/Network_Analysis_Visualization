package statsMetrics;
import gr.forth.ics.graph.Edge;
import gr.forth.ics.graph.Graph;
import gr.forth.ics.graph.Node;
import gr.forth.ics.graph.PrimaryGraph;
import gr.forth.ics.graph.metrics.BrandesMetrics;
import gr.forth.ics.util.ExtendedListIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * The class metrics allows  
 * the user to collect and gather statistics and metrics about their network
 * such as number of node/edges, average path, density, centralisation measures ...
 * about its network.
 * 
 * 
 * This class is based on the flexigraph library
 * 
 * https://code.google.com/p/flexigraph/s
 * A Java graph algorithms library, mainly developed in 2006-7.
 * 
 * @author axm1064  ID: 1402590
 * @since 08/09/2014
 *
 */

public class metrics {
	
	
	private String jsonText;
	private HashMap<String,statsNode> node = new HashMap<String,statsNode>();	
	private HashMap<statsNode, String> node_key = new HashMap<statsNode, String>();	
	
	
	private Graph g = new PrimaryGraph(); 
	
	private int nbrNodes; 
	private int nbrEdges;
	private double density=0.0;

	private int nbrPromNodes;
	
	public metrics(String strInput ){
		this.jsonText = strInput;
		parseJSONtext();

		this.nbrPromNodes = g.nodeCount();
	}
	
	/**
	 * 
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
			  
			  statsNode clspcpNode = new statsNode(n);
					  
			  this.node.put( temp1.get(i) , clspcpNode ); 
			  this.node_key.put(  clspcpNode  ,  temp1.get(i)   ); // Need to override redefine the equals methods.
			   
		  }
		  

		  		 
		  // Similarly, for the edges of the graph, we store the source and destination in the graph g
		  temp1 =  find(jsonText, "source");
		  temp2 =  find(jsonText, "target");
		  for(int i=0;  i < temp1.size(); i++){
			  this.g.newEdge(    node.get( temp1.get(i) ).getNode()		, node.get( temp2.get(i) ).getNode()	);
		  }
		  
		  
		  // Set the number of edges in the network loaded.
		  this.nbrEdges = this.g.edgeCount();
		// Set the number of vertices in the network loaded
		  this.nbrNodes = this.g.nodeCount();
		  
		  double denom = ( 
				  
				(  (double) this.nbrNodes ) *
				  
				  (  (double) ( this.nbrNodes -1 ) ) 
						  
						  /2 ) ;
		  
		  density = (double)this.nbrEdges;
		  density = density / denom;
		  		  	
	}
	
	
	/**
	 * 	The metricsToJson() method creates a String JSON Object containing the information requested by the user/client (results of the algorithms).
	 * @return String JSON object to be exported through the network to the user. Object type exchanged between the user and the server.
	 */
	public String metricsToJson(){
		
		  ExtendedListIterable<Node> nodes =  	g.nodes() ;
		  ExtendedListIterable<Edge> edges =  	g.edges() ;
		  
		// First I add the header, the algorithm name
		 StringBuilder str = new StringBuilder("{\"metrics\": {");
			 // Next I add the nodes
			   str.append("\"nodes" + "\":[ " );
			   
			   
			   for (Node n: nodes)
		   			str.append( "{\"data\":{\"id\":\"" +  n.toString()  
		   			+ "\",\"name\":\"" +   n.toString()
		   			+ "\",\"deg\":\"" + g.degree(n)  // add the degree of each node to plot the degree distribution
		   			+ "\"}},");
		   		
		   		str.replace(str.length() -1, str.length(), "],");   			
			//*****************************************************************************	  
		   	// then I add the edges	
		   	 str.append("\"edges\":[") ;
		   	 
		   	 int dim = str.length();
		   	// System.out.println(dim + "  " + str);
		   	 
		   	 for (Edge e : edges)
		   		 str.append("{\"data\":{\"source\":\"" +  ((e.n1()).toString()).trim() 
							   	+ "\",\"target\":\"" +   ((e.n2()).toString()).trim() 
							   	+ "\"}},"	);	

		  // 	 System.out.println(str); 
		   	if (dim==str.length())
		   		str.replace(str.length() - ("],\"edges\":[").length(), str.length(), "],");
		   	else
		   		str.replace(str.length() -1, str.length(), "],");
		   	//System.out.println(str.length() + " " + str); 	
		   	
		   	
	   str.append("\"stats" + "\":[ " );
			   		   		
					str.append( "{\"data\":{\"id\":\"" + "Number Of Nodes" 
				   			+ "\",\"val\":\"" +  g.nodeCount() + "\"}},");
					
					str.append( "{\"data\":{\"id\":\"" + "Number Of Edges" 
				   			+ "\",\"val\":\"" +  g.edgeCount() + "\"}},");
		   				   								
					
		   			str.append( "{\"data\":{\"id\":\"" + "Density" 
				   			+ "\",\"val\":\"" +  this.density + "\"}},");
		   					   		
		   			str.append( "{\"data\":{\"id\":\"" + "Characteristic Path Length" 
				   			+ "\",\"val\":\"" +  (BrandesMetrics.execute(g, false).getCharacteristicPathLength())/2 + "\"}},");
					
		   			str.append( "{\"data\":{\"id\":\"" + "Diameter" 
				   			+ "\",\"val\":\"" +  BrandesMetrics.execute(g, false).getDiameter() + "\"}},");
		   				   						   				
		   			str.append( "{\"data\":{\"id\":\"" + "Degree Centralization" 
				   			+ "\",\"val\":\"" +  BrandesMetrics.execute(g, false).getNetworkDegreeCentralization() + "\"}},");
		   	
		   			str.append( "{\"data\":{\"id\":\"" + "Closeness Centralization" 
				   			+ "\",\"val\":\"" +  BrandesMetrics.execute(g, false).getNetworkClosenessCentralization() + "\"}},");
		   			
		   			str.append( "{\"data\":{\"id\":\"" + "Edge Betweenness" 
				   			+ "\",\"val\":\"" +  BrandesMetrics.execute(g, false).getNetworkEdgeBetweenness() + "\"}},");
		   					   			
		   			str.append( "{\"data\":{\"id\":\"" + "Betweenness Centralization" 
				   			+ "\",\"val\":\"" + BrandesMetrics.execute(g, false).getNetworkNodeBetweennessCentralization()	+ "\"}}],"); 	
		   				   
		   //*****************************************************************************
		   
		   str.replace(str.length() -1, str.length(), "");
		   str.append( "}} ");
				
		return str.toString();
	}
	
	
	/**
	 * 	The find method retrieves all the value associated with the key parameter input. 
	 * @param str String JSON object  
	 * @param key to search in the input string, and retrieve the value associated. (key/value pair JavaScript Object notation)
	 * @return LinkedList<String>  object containing 
	 */	
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


class statsNode {
	

	private Node node; // vertex

	// constructor where we set the node and initiate the counter to zero.
	statsNode(Node n){
		this.node = n;
	}
	

	public Node getNode(){
		return this.node;
	}

	
}
