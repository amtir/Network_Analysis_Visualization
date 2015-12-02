package profilCorePeriph;


import gr.forth.ics.graph.Graph;
import gr.forth.ics.graph.Node;
import gr.forth.ics.graph.PrimaryGraph;
import gr.forth.ics.util.ExtendedListIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import kcores_algorithm.KeyFinder;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * This class implements the Profiling Core-periphery network structure by random walkers.
 * 
 * http://www.nature.com/srep/2013/130319/srep01467/full/srep01467.html
 *     Scientific Reports  3, Article number: 1467  doi:10.1038/srep01467 Received  07 January 2013 
 *     Accepted 19 February 2013 -   Published 19 March 2013 
 * 
 * @see <a href="http://www.nature.com/srep/2013/130319/srep01467/full/srep01467.html">Profiling core-periphery network structure by random walkers, Rossa, Dercole, Piccardi, 02/2013</a>
 * 
 * 
 * @author MSC Student ID: 1402590 axm1064
 * @since 08/09/2014
 */
public class ProfilCorePeriph {

	
	private String jsonText;
	private HashMap<String,pcpNode> node = new HashMap<String,pcpNode>();	
	private HashMap<pcpNode, String> node_key = new HashMap<pcpNode, String>();	
	
	private HashMap<String,pcpNode> g_Shrink = new HashMap<String,pcpNode>();	
	private HashMap<pcpNode, String>  g_Shrink_key = new HashMap<pcpNode, String>();	
	
	
	private Graph g = new PrimaryGraph(); 
	private Graph sGrow = new PrimaryGraph();
	
	private int nbrNodes; 
	private int nbrEdges;

	private int nbrPromNodes;

/**
 * Constructor with two parameters or arguments
 * @param strInput String type. The data received from the client/user. Network Graph data input in String format JSON.
 * @param nbrPromNodes of the integer type. For future use. If the user want to select the n best prominent nodes.
 * For now the server sends the whole profile to the user.
 */
	public ProfilCorePeriph(String strInput, int nbrPromNodes ){
		this.jsonText = strInput;
		parseJSONtext();
		//this.nbrPromNodes = nbrPromNodes;
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
			  
			  pcpNode clspcpNode = new pcpNode(n);
					  
			  this.node.put( temp1.get(i) , clspcpNode ); 
			  this.node_key.put(  clspcpNode  ,  temp1.get(i)   ); // Need to override redefine the equals methods.
			  
			  
			  this.g_Shrink.put( temp1.get(i) , clspcpNode ); 
			  this.g_Shrink_key.put(  clspcpNode  ,  temp1.get(i)   ); 
  
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
		  for(pcpNode n : this.node_key.keySet()){
			  
			  this.node.get(this.node_key.get(n)).setStrenght(g.degree(n.getNode()));
			  
		  }
		  
		  	
	}
	
	
/**
 * the method printSortedNodes simply prints to the console 
 * all the nodes ordered with their rank, Profiling Core-periphery profile, and Strength.
 */
	public void printSortedNodes() {
		
		Collection<pcpNode>	colnodes =   node.values();
		Object[] arrNode =  colnodes.toArray();
		
		Arrays.sort(arrNode);
		
		System.out.println("Printing .. ." + this.nbrPromNodes);
		
		for (int i=0; i<this.nbrPromNodes; i++)
			System.out.println(i + "  CorePeriphProf: " + ((pcpNode) arrNode[i]).getCorePeriphProf()
					+ "  Strenght: " + ((pcpNode) arrNode[i]).getStrenght()
					+  ", node: " +  ((pcpNode) arrNode[i]).getNode() );
					

	}


	/**
	 * The runProfilCorePeriph method implements the Profiling core-network structure algorithm.
	 * 
	 * The algorithm may have some randomicity (in the initial node and when, at step k, many node with the same strength attain the same CPC .
	 * 
	 * CPC: Core Periphery Profile coefficient 
	 */
	public void runProfilCorePeriph(){ // approximation ~ O(N2) quadratic time efficiency 
	 
		// The core-periphery profile of node is the report between two sums.
		// To avoid repeating the calculation, and obtain better performance, 
		// we keep track of these sums in the following local variables
		double denomCorePeriphProf = 0.0;
		double numerCorePeriphProf = 0.0;
		int index = 0;
		
	//*************************************	
	// Step1: initialisation 
	//	Priming
	//*************************************
		// Select at random a node among those with minimal strength (degree of a node in the case of unweighted and undirected graph)
		Collection<pcpNode>	colnodes =   node.values();
		Object[] arrNode =  colnodes.toArray();		
		Arrays.sort(arrNode); // shorting the array based on the  degree (special case of the undirected non-weighted graph)
		
		// We retrieve the first node randomly;
		// Arbitrary choice 
		index = firstRandNode(arrNode);
		Node n = ((pcpNode) arrNode[index]).getNode();
		System.out.println("first random index: " + index);
		
		 pcpNode clspcpNode = new pcpNode(n);
		 // I add this first node to the expanding growing subgraph in other words into the subset of nodes S_Grow
		//this.S_Grow.put(n.toString(), clspcpNode);
		//this.S_Grow_key.put(clspcpNode,n.toString());
		
		// In the case of unweighted and undirected network, 
		// the denominator of the CPC coefficient is simply the sum of the degrees of the nodes in the subset, including the last node added.
		denomCorePeriphProf += g.degree(n);		
		this.sGrow.newNode( n );	// we insert our first node 
		
		// Remove these node from the g_Shrink graph. So by g_Shrink is the complementary of sGrow. 
		// sGrow U g_Shrink = g, where g is the whole graph
		this.g_Shrink.remove(n.toString());
		this.g_Shrink_key.remove(clspcpNode);

		
	//**********************************	
	// Step k = 2,3,4,5, ..., N
	//**********************************
		// Selecting the node attaining the minimum in the core-periphery profile (CPC) and inserted into the subset.
		// Principle of the algorithm: 
		// For each node, I calculate the core-periphery profile and store it in the data structure.
		// The data structure is then sorted, and the node with the minimum CPC is retrieved and inserted..

	
		while(g_Shrink.keySet().size()!=0){ // ~ O(N) 

			// The ttNode ArrayList is a collection of tempNode instances. 
			ArrayList<tempNode> ttNode = new ArrayList<tempNode>();
			// This collection of nodes and their core-periphery profile is sorted, 
			// and then the node whith the minimal CPC is retrieved and added to the expanding growing subset S_Grow ...
			
			// For each node remaining in g_Shrink, I calculate the CPC and store it to sorted later.
			for(pcpNode cpcn : g_Shrink.values() ){   // ~ O(N) 
				
				Node nod  =  cpcn.getNode(); // 

				// For this particular node, we calculate the CPC 
				double k1 = g.degree(nod);
				denomCorePeriphProf += k1; 
				
				double k2 =0.0;
				// From the main graph g we retrieve all the neighbours of this particular node.
				  ExtendedListIterable<Node> itNode = g.adjacentNodes(nod);
				  for (Node itN : itNode ) // Loop through each neighbour
					  for(Node nnt:sGrow.nodes()) // 
						  if (nnt.toString().equals(itN.toString())){ // Does this neighbour node belongs to the subset sGrow?						
						  k2 +=1+1; // wij = wji = 1
					  }	
				  // then we add the weight edge effect 
				  numerCorePeriphProf +=k2;
			
				// I add this node and its CPC and strength to the collection.  
				ttNode.add(new tempNode(nod , numerCorePeriphProf/denomCorePeriphProf , k1, k2));
				
				// Remove the effect of the last node added to calculate the CPC for the next node.
				denomCorePeriphProf -= k1;
				numerCorePeriphProf -= k2;
	
			}
		
			// The collection is ordered and sorted.
			Collections.sort(ttNode);

			// For debugging
/*			System.out.println("After Sorting: ");
			for(tempNode tt : ttNode ){
		//	for(int l =0; l <ttNode.size(); l++) {
				System.out.println("Key node: " + tt.getNode().toString() + " score node: " + tt.getCorePeriphProf());
				//System.out.println(" score node: " + ttNode[l].getCorePeriphProf());
			}*/
			
			// Retrieve the first element in the array, the node with the minimum core-periphery profile
			index = retrieveRandNode(ttNode);
			Node newNode = ttNode.get(index).getNode() ;  
			pcpNode newpcpNode = new pcpNode(newNode);
			
			// I add this node to the subset
			Node new_node = this.sGrow.newNode( newNode );
			
			// Add the effect of this new member in the subset onto the CPC.
			denomCorePeriphProf += ttNode.get(index).getDeg();
			numerCorePeriphProf +=  ttNode.get(index).getNum();
			
/*
 //  I can add the edges to the sub-graph to have a complete subset (node and edges)
 // but this degrades the performance.
			  ExtendedListIterable<Node> itNode = g.adjacentNodes(newNode);
			  for (Node itN : itNode )
				  for(Node nnt:sGrow.nodes())
					  if (nnt.toString().equals(itN.toString())){ 
						  // if (sGrow.containsNode(itN)){
						   //sGrow.aNode()
					  this.sGrow.newEdge(nnt, new_node);		
				  }	
		*/	
		  
		//	this.S_Grow.put(newNode.toString(), newpcpNode); // Add this new member to the growing subset S
		//	this.S_Grow_key.put(newpcpNode,newNode.toString());

			// Remove that node from the Shrinking initial graph 
			this.g_Shrink.remove(newNode.toString()); 
			this.g_Shrink_key.remove(newpcpNode);
			
			//String strKey = this.node_key.ge
/*			System.out.println("After Sorting: ");
			for(int l =0; l <ttNode.size(); l++) {
				System.out.println("Key node: " + ttNode.get(l).getNode().toString() + " score node: " + ttNode.get(l).getCorePeriphProf());
				//System.out.println(" score node: " + ttNode[l].getCorePeriphProf());
			}*/
			
			// Quite convoluted to set the CPC coefficient of the new member, the last node added to the subset.
			(this.node.get(
					ttNode.get(index).getNode().toString())
								).setCorePeriphProf(
										ttNode.get(index).getCorePeriphProf()
								); // we store the Core-periphery profile in our global collection 
			// By this means I can order all the vertices based on their CPC.
			
		}	// End of the while loop
			
								
	}
	
	/**
	 * The firstRandNode() method initialise the algorithm by selecting the first node in the periphery.
	 * We retrieve the first node randomly from the set with the lowest pcp and deg score/coefficient.
	 * @param arrNode
	 * @return int 
	 */
	private int firstRandNode( Object[] arrNode ){
		
		Random rand = new Random();
		
	//	 ((pcpNode) arrNode[0]).getNode();
		
		
		ArrayList<pcpNode> temp = new ArrayList<pcpNode> ();
		int s = arrNode.length;
		int index = 0;
		
		double cpc = ((pcpNode) arrNode[index]).getCorePeriphProf();//col.get(index).getCorePeriphProf();
		double deg = ((pcpNode) arrNode[index]).getStrenght();
		
		temp.add((pcpNode) arrNode[index]);
		index++;
		
		while(index < s){
			// Constraint : we are interested only in the node with the same CPC and Strength
			if( ((pcpNode) arrNode[index]).getCorePeriphProf() == cpc  &&  ((pcpNode) arrNode[index]).getStrenght() == deg  ){
				
					temp.add((pcpNode) arrNode[index]);
					index++;
					
			}else{
				break; //leave, break out this Loop		
			}
			
		}
		
		// I reuse the int s variable to hold this time the size of the temp collection
		s = temp.size();
		//The method call returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and s (exclusive). 
		index = rand.nextInt(s);
		
		
		
		return index;
	}
	
	/**
	 * Some Randomicity in this section ... for the Step k = 2,3,...n of the algorithm.
	 * The method retrieveRandNode returns the index of the element of the element to retrieve from the already ordered collection.
	 * @param col the ordered collection to work on
	 * @return the index of the element of the element to retrieve from the already ordered collection.
	 */
	private int retrieveRandNode(ArrayList<tempNode> col){
		
		Random rand = new Random();
		
		ArrayList<tempNode> temp = new ArrayList<tempNode> ();
		int s = col.size();
		int index = 0;
		
		double cpc = col.get(index).getCorePeriphProf();
		double deg = col.get(index).getDeg();
		
		temp.add(col.get(index));
		index++;
		
		while(index < s){
			// Constraint : we are interested only in the node with the same CPC and Strength
			if(  col.get(index).getCorePeriphProf()==cpc  &&  col.get(index).getDeg()==deg  ){
				
					temp.add(col.get(index));
					index++;
					
			}else{
				break; //leave, break out this Loop		
			}
			
		}
		
		// I reuse the int s variable to hold this time the size of the temp collection
		s = temp.size();
		//The method call returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and s (exclusive). 
		index = rand.nextInt(s);
		
		
		
		return index;
	}
	
	/**
	 * The method pcpToJson construct the JSON String text object to send through the network to the usr.
	 * @return String the JSON object to send back to the user.
	 */
	public String pcpToJson(){
		
		// Retrieve all the pcpNode object and sort, order them
		Collection<pcpNode>	colnodes =   node.values();
		Object[] arrNode =  colnodes.toArray();
		
		Arrays.sort(arrNode);
		

		// First I add the header, the algorithm name
		 StringBuilder str = new StringBuilder("{\"procoreperif\": {");
			 // Next I add the nodes
			   str.append("\"promNode" + "\":[ " );
			   for (int i=0; i<this.nbrPromNodes; i++)
		   			str.append( "{\"data\":{\"id\":\"" +  
			   ((pcpNode) arrNode[i]).getNode()  + "\",\"name\":\"" +  ((pcpNode) arrNode[i]).getNode() 
			   +
			   "\",\"profCorePeriph\":\"" +  ((pcpNode) arrNode[i]).getCorePeriphProf()
			   + "\",\"strength\":\"" +  ((pcpNode) arrNode[i]).getStrenght() 
			   + "\"}},");
		   		
		   		str.replace(str.length() -1, str.length(), "],");   			
			//*****************************************************************************	  
		   	// then I add the edges	
		   	 str.append("\"edges\":[") ;
		   	 
		   	 int dim = str.length();
		   	// System.out.println(dim + "  " + str);
		   	 
		   	 for (int i=0; i<this.nbrPromNodes; i++)
		   		 for (int j=i+1; j<this.nbrPromNodes; j++){
		   			 Node node1 = ((pcpNode) arrNode[i]).getNode();
		   			 Node node2 = ((pcpNode) arrNode[j]).getNode() ;
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
 * 
 * The class tempNode implements the comparable interface. 
 * It is used to detect and select the next node, 
 * the one with the minimal CPC, and to insert it in the subset.
 *
 */
class tempNode implements Comparable<tempNode>{
	private Node node; // vertex
	private double corePeriphProf; // CPC coefficient between 0 and 1.
	private double deg; // Strength of a node in the network (degree of a node in an unweighted and undirected network)
	private double num; // 
	
	// Constructor
	tempNode(Node n, double cpc, double deg, double num){
		this.node = n;
		this.corePeriphProf = cpc;
		this.deg = deg;
		this.num = num;
	}
	
	// getters 
	public double getCorePeriphProf(){
		return this.corePeriphProf;
	}
	
	public double getDeg(){
		return this.deg;
	}
	
	public double getNum(){
		return this.num;
	}
	
	public Node getNode(){
		return this.node;
	}

	
	@Override
	public int compareTo(tempNode o) {
		
		double dd =this.getCorePeriphProf() - o.getCorePeriphProf();
		
		if(dd < 0)
			return -1;
		else if (dd > 0)
			return 1;
		else { // case where the two node have the same Core-periphery profile CPC coefficient
			// Then we distinguish them by their degree or in other words their strength (special case of the unweighted and undirected network)
			double deltDeg = this.getDeg() - ((tempNode)o).getDeg();
			if (deltDeg<0)
				return -1;
			else if(deltDeg>0)
				return 1;
			else
				return 0;
		}
		
		// Quite dense and Compact way
		//	return (this.getCorePeriphProf() < o.getCorePeriphProf())? -1:(this.getCorePeriphProf() < o.getCorePeriphProf())? 1:0;
	}
	
}

/**
 * Each node in the graph is associated to an instance of this class 
 * This allows us to keep track of its core-periphery profile coefficient and strength.
 *
 */
class pcpNode implements Comparable<pcpNode>, Comparator<pcpNode>{
	

	private Node node; // vertex
	private double corePeriphProf;
	private double strenght;
		
	// constructor where we set the node and initiate the counter to zero.
	pcpNode(Node n){
		this.node = n;
		this.corePeriphProf = 0.0;
		this.strenght = 0.0;
	}
	
	pcpNode(){
		
	}

	// Some getters and setters
	public double getStrenght(){
		return this.strenght;
	}
	
	public void setStrenght(double dblStrenght){
		 this.strenght = dblStrenght ;
	}
	
	public double getCorePeriphProf(){
		return this.corePeriphProf;
	}
	
	public void setCorePeriphProf(double dblProb){
		 this.corePeriphProf = dblProb ;
	}
	
	public Node getNode(){
		return this.node;
	}

	// I implement the Comparable interface. The method compareTo allows me to order and short the pcpNode instances.
	@Override
	public int compareTo(pcpNode o) {
		
		pcpNode pcpnode = o;
		// Notice that I reversed the < and > to obtain an increasing sorted array
		if ((this.getCorePeriphProf() - pcpnode.getCorePeriphProf()) != 0 ) // First check if CPC coefficients are different.
			// First I order by the CPC coefficient
			return (this.getCorePeriphProf() < pcpnode.getCorePeriphProf())? -1:(this.getCorePeriphProf() > pcpnode.getCorePeriphProf())? 1:0;
		else
			// If the CPC coefficients are the same, I order them by strength
			return ((int)this.getStrenght() < (int)o.getStrenght())? -1:((int)this.getStrenght() > (int)o.getStrenght())? 1:0;		
	}
	
	

	// Not used in case I want to shorted in another way.
	@Override
	public int compare(pcpNode arg0, pcpNode arg1) {
		
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
		pcpNode other = (pcpNode) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	
}
