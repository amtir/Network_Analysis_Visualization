
package biasRandomWalkAlgo;

import gr.forth.ics.graph.Graph;
import gr.forth.ics.graph.Node;
import gr.forth.ics.graph.PrimaryGraph;
import gr.forth.ics.util.ExtendedListIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import kcores_algorithm.KeyFinder;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
* The class biasRandomWalk
* implements the Biased Random Walks to identify 
* Prominent Actor in Online Social Networks
* Based on the article: "Frank Takes, Walter Kosters, Leiden University, The Netherlands"
* @see <a href="http://www.liacs.nl/~ftakes/pdf/prominent.pdf">Identifying Prominent Actors in Online Social Networks using Biased Random Walks, Frank Takes, Walter Kosters, Leiden University, The Netherlands</a>
*  
* @author axm1064  ID: 1402590
* @since 08/09/2014
* 
**/

public class biasRandomWalk{

	
	private String jsonText;
	private HashMap<String,brwNode> node = new HashMap<String,brwNode>();	
	private HashMap<brwNode, String> node_key = new HashMap<brwNode, String>();	
	private String idNodes[];
	private Graph g = new PrimaryGraph(); 
	private int nbrNodes; 
	private int nbrEdges;
	
	private int nbrIter;
	private int nbrPromNodes;
	private double prob= 0.15;
	private double alpha = 0.5;

	/**
	 * Constructor
	 * @param strInput String JSON Object / Networks Graph input
	 * @param nbrIter Number of iteration N >= 10 or 100 x number of nodes
	 * @param nbrPromNodes int Number of Prominent nodes to retrieve
	 * @param prob double Probability p jump probability to a completely random node.  (1-p) probability of selecting a node in the neighbourhood with the bias function.
	 * @param alpha double used to define the focus on either the neighbourhood density or the degree centrality.	
	 */
	public biasRandomWalk(String strInput, int nbrIter, int nbrPromNodes,  double prob, double alpha ){
		this.jsonText = strInput;
		parseJSONtext();
		this.prob = prob;
		this.alpha = alpha;
		this.nbrIter = nbrIter;
		this.nbrPromNodes = nbrPromNodes;

	}
	
	
	
/**
 * The printSortedNodes() method is a useful print function used for debug and testing.
 * Basically, It prints the list of nodes, in order.
 */
	public void printSortedNodes() {
		
		Collection<brwNode>	colnodes =   node.values();
		Object[] arrNode =  colnodes.toArray();
		
		Arrays.sort(arrNode);
		
		System.out.println("Printing .. ." + this.nbrPromNodes);
		
		for (int i=0; i<this.nbrPromNodes; i++)
			System.out.println(i + "  importance: " + ((brwNode) arrNode[i]).getImport() +  ", node: " +  ((brwNode) arrNode[i]).getNode() );

	}


	/**
	 * 
	 * The method neighboHoodDensity returns a normalised result (0 -> 1) 
	 * of the number of common connections for a specific node with it neighbourhood.
	 * @param n is a specific Node in the graph.
	 * @return a double value corresponding to the neighboHoodDensity
	 */
	private double neighboHoodDensity(Node n){
		
		double neighHoodDens=1.0; 
		double degNode = 0.0;
		double degNeighNode = 0.0;
		
			
		// Returns the degree of the specified node.
		degNode = g.degree(n);
		
		// Get all the neighbour of the Vertex n
		ExtendedListIterable<Node> neighbours = g.adjacentNodes(n) ;
		
		
				
		for(Node nod : neighbours){	// let's look for its key so next iteration, the importance counter will be incremented				
			
			// Returns the degree of the specified node.
			degNeighNode = g.degree(nod);
			double denominator = (degNeighNode-1)*degNode;
			double nominator = 0.0; // number of common connections
			
			// Get the neighbour of the current neighbour 
			ExtendedListIterable<Node> neigh2bours = g.adjacentNodes(nod) ;
			
				
				for(Node nn : neigh2bours){ // Loop through all the node of the neighbour
					if( g.areAdjacent(nn, nod) )  // Returns whether there exists an edge connecting the specified nodes.
						nominator++;
					
				}
				
			
			
			neighHoodDens -= nominator/denominator;
			
		}
	
		return neighHoodDens;
	}
	
	
	
	/**
	 * The probPromMeas() method calculates the probability of a specific node being selected 
	 * based on the node's neighbourhood density concept (see article).
	 * @param n a specific node in the graph network
	 * @return probability of selecting the node n.
	 */
	
	private double probPromMeas(Node n ){
		
		double C1 = this.alpha;
		double C2 = 1-this.alpha;
		double probability = 0.0;
		double den = 0.0;
		double num = C1 * g.degree(n) + C2*neighboHoodDensity(n);
					
			// Get the neighbour of the current neighbour 
			ExtendedListIterable<Node> neighbours = g.adjacentNodes(n) ;
			
			for(Node nn : neighbours){ 
				den += C1*g.degree(nn) + C2*neighboHoodDensity(nn);
			}
			
			probability = num / den;
		
		return probability;
	}
	

	/**
	 * The parseJSONtext() method simply parses the JSON Object received, 
	 * creates the graph -g which correspond to the network loaded by the user.
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
			  
			  brwNode clsBrwNode = new brwNode(n);
			  clsBrwNode.setprobProm( probPromMeas(n) );
			  
			  this.node.put( temp1.get(i) , clsBrwNode ); 
			  this.node_key.put(  clsBrwNode  ,  temp1.get(i)   ); // Need to override redefine the equals methods.
			 
			  
		  }
		  
		  this.nbrEdges = this.g.edgeCount();
		  this.nbrNodes = this.g.nodeCount();
		  
		  this.idNodes = new String[this.nbrNodes];
		  for(int i=0; i < temp2.size(); i++){ 
			  this.idNodes[i] = temp1.get(i);
		  }
		 
		  // Similarly, for the edges of the graph, we store the source and destination in the graph g
		  temp1 =  find(jsonText, "source");
		  temp2 =  find(jsonText, "target");
		  for(int i=0;  i < temp1.size(); i++){
			  this.g.newEdge(    node.get( temp1.get(i) ).getNode()		, node.get( temp2.get(i) ).getNode()	);
		  }
		  

		
	}
	
/**
 * Setter
 * The method setNewGraphData sets a new JSON Text Object (Network) 
 * @param jsonText
 */
	
	public void setNewGraphData(String jsonText){
		this.jsonText = jsonText;	
		parseJSONtext();
		
	}
	
	
	/**
	 * The runBiasRandWalk() method simply implements the BRW algorithm with the parameter of the user (N, P, Alpha) 
	 * Each node is assigned a score ( a kind of counter incremented by 1/NumberOfNodes once the node is hit)
	 */
	public void runBiasRandWalk(){
		
		 Random random = new Random();
		
		// Initialisation of the algorithm: select a random number between 0 to the number of nodes exclusive.
		int randomInteger = random.nextInt(this.nbrNodes);
		String strIdNode = idNodes[randomInteger]; // array access Cte time access O(1)
		 
		for(int i=0; i<this.nbrIter; i++){ // N times, Number of steps in the random walk, N should be >> number of nodes or vertices
			
				// Get that node and increment its importance counter
				node.get(strIdNode).increment(1.0/this.nbrNodes); // Hash table fast access to the node 
				
				if(random.nextDouble()>this.prob){  // Visit the neighbour with the highest probability?
	// java.util.Random class provides method nextDouble() which can return uniformly distributed pseudo random double values between 0.0 and 1.0.

					ExtendedListIterable<Node> neighbours = g.adjacentNodes(node.get(strIdNode).getNode()) ;
					
					// Knowing all the neighbours of the visited node, we should jump to the neighbour with the highest likelihood, 
					// probability dependent on different function values of the prominence measures. 
					
					
					double probProm=0.0; Node rnn; // random neighbour node
					for(Node n : neighbours){	// let's look for its key so next iteration, the importance counter will be incremented				
						

							rnn=n; // current neighbour we are visiting
							String strIdVert = node_key.get(new brwNode(rnn)); // its corresponding key-map
							
							if ( probProm < node.get(strIdVert).getprobProm()){ // higher likelihood? 
								
								probProm = node.get(strIdVert).getprobProm(); // Keep track of the highest probability
								strIdNode = node_key.get(new brwNode(rnn));  // set the next vertex to visit.	
								
							}
							
					}
					
					
					
				}
				else{ // jump to and visit a completely random node
		
					// select a completely random node/number between 0 to the number of nodes
					randomInteger = random.nextInt(this.nbrNodes);
					strIdNode = idNodes[randomInteger]; // array access Cte time access O(1)
				
				}

			
		}
		
				
		
	}
	
/**
 * Not really used/ wanted to implement a pure Random Walk
 * Which could be retrieved by simply changing the value of alpha. 
 * Pure Random walk without any bias
 */
	public void runRandWalk(){
		
		 Random random = new Random();
		
		// Initialisation of the algorithm: select a random number between 0 to the number of nodes exclusive.
		int randomInteger = random.nextInt(this.nbrNodes);
		String strIdNode = idNodes[randomInteger]; // array access Cte time access O(1)
		 
		for(int i=0; i<this.nbrIter; i++){ // N times, Number of steps in the random walk, N should be >> number of nodes or vertices
			
				// Get that node and increment its importance counter
				node.get(strIdNode).increment(1.0/this.nbrNodes); // Hash table fast access to the node 
				
				if(random.nextDouble()>this.prob){ 
	// java.util.Random class provides method nextDouble() which can return uniformly distributed pseudo random double values between 0.0 and 1.0.

					ExtendedListIterable<Node> neighbours = g.adjacentNodes(node.get(strIdNode).getNode()) ;
					
					// Pure random walk: without bias: Randomly choose a neighbour.
					randomInteger = random.nextInt( neighbours.size()); 
					
					int count=0; Node rnn; // random neighbour node
					for(Node n : neighbours){	// let's look for its key so next iteration, the importance counter will be incremented				
						
						if (count==randomInteger) {
							rnn=n;
							strIdNode = node_key.get(new brwNode(rnn)); // key founded
							break;
						}
						count++;
					}
					
					
					
				}
				else{
		
					// select a completely random node/number between 0 to the number of nodes
					randomInteger = random.nextInt(this.nbrNodes);
					strIdNode = idNodes[randomInteger]; // array access Cte time access O(1)
				
				}

			
		}
		
				
		
	}
	
	
/**
 * 	The brwToJson() method creates a String JSON Object containing the information requested by the user/client (results of the algorithm).
 * @return String JSON object to be exported through the network to the user. Object type exchanged between the user and the server.
 */
	public String brwToJson(){
		
		Collection<brwNode>	colnodes =   node.values();
		Object[] arrNode =  colnodes.toArray();
		
		Arrays.sort(arrNode);
		
		for (int i=0; i<this.nbrPromNodes; i++)
			System.out.println(i + " importance: " + ((brwNode) arrNode[i]).getImport() +  ", node: " +  ((brwNode) arrNode[i]).getNode() );

		
		//###################################################################################################
		 
		 StringBuilder str = new StringBuilder("{\"randBiasWalk\": {");
			 
			   str.append("\"promNode" + "\":[ " );
			   for (int i=0; i<this.nbrPromNodes; i++)
		   			str.append( "{\"data\":{\"id\":\"" +  ((brwNode) arrNode[i]).getNode()  + "\",\"name\":\"" +  ((brwNode) arrNode[i]).getNode() + "\",\"importance\":\"" +  ((brwNode) arrNode[i]).getImport() + "\"}},");
		   		
		   		str.replace(str.length() -1, str.length(), "],");   			
		//   }
//*****************************************************************************	  
		   		
		   	 str.append("\"edges\":[") ;
		   	 
		   	 int dim = str.length();
		   	 
		   	 for (int i=0; i<this.nbrPromNodes; i++)
		   		 for (int j=i+1; j<this.nbrPromNodes; j++){
		   			 Node node1 = ((brwNode) arrNode[i]).getNode();
		   			 Node node2 = ((brwNode) arrNode[j]).getNode() ;
		   			 if (  g.areAdjacent(  node1 , node2 ) ) {
		   				 
		   			  str.append("{\"data\":{\"source\":\"" +  node1 
							   	+ "\",\"target\":\"" +  node2
							   	+ "\"}},"	);	
		   				 
		   			 }
		   		 }
		   	 
		   	
		   	
		   	
		   	if (dim==str.length())
		   		str.replace(str.length() - ("],\"edges\":[").length(), str.length(), "],");
		   	else
		   		str.replace(str.length() -1, str.length(), "],");
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

/**
 * Each node in the graph is associated to an instance of this class 
 * This allows us to keep track of its importance and the number of visits in the random walk.
 *
 */
class brwNode implements Comparable{
	

	private double importance;
	private Node node; // vertex
	private double probProm;
		
	// constructor where we set the node and initiate the counter to zero.
	brwNode(Node n){
		this.node = n;
		this.importance = 0.0;
		this.probProm = 0.0;
	}
	
	// method to increment the counter of the importance of the node visited
	public void increment(double ratio){	
		importance += ratio;		
	}
	
	// getter of the importance of the node
	public double getImport(){
		return this.importance;
	}
	
	public double getprobProm(){
		return this.probProm;
	}
	
	public void setprobProm(double dblProb){
		 this.probProm = dblProb ;
	}
	
	public Node getNode(){
		return this.node;
	}

	@Override
	public int compareTo(Object o) {
		
		brwNode brwnode = (brwNode) o;
		
		return (this.getImport() > brwnode.getImport())? -1:(this.getImport() < brwnode.getImport())? 1:0;
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
		brwNode other = (brwNode) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}
	

	
}
