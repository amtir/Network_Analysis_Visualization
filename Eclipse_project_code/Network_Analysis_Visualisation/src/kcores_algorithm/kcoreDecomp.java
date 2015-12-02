package kcores_algorithm;

import gr.forth.ics.graph.Graph;
import gr.forth.ics.graph.Node;
import gr.forth.ics.graph.PrimaryGraph;
import gr.forth.ics.util.ExtendedListIterable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




/**
 * The kcoreDecomp class 
 * 
 * @see <a href="http://en.wikipedia.org/wiki/K-core">K-Core article on Wikipedia</a>
 * @see "S. B. Seidman, 1983, Network structure and minimum degree, Social Networks 5:269-287"
 * @author Andreou Dimitris, email: jim.andreou (at) gmail.com
 * 
 * @author MSC Student ID: 1402590 axm1064
 * @since 08/09/2014
 */
public class kcoreDecomp {
	
	private String jsonText;
	private HashMap<String,Node> node = new HashMap<String,Node>();	
	private HashMap<Node, String> node_key = new HashMap<Node, String>();	
	private Graph g = new PrimaryGraph(); 

	/**
	 * Constructor
	 * @param strInput JSON string object received from the used/client.
	 */
	public kcoreDecomp(String strInput){
		this.jsonText = strInput;
		parseJSONtext();

	}
	
	/**
	 * The parseJSONtext() method parses the string data received 
	 * and creates the graph network g. // called from the constructor.
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
			  this.node.put( temp1.get(i) , n ); 
			  this.node_key.put(  n  ,  temp1.get(i)   );
		  }

		  // Similarly, for the edges of the graph, we store the source and destination in the graph g
		  temp1 =  find(jsonText, "source");
		  temp2 =  find(jsonText, "target");
		  for(int i=0;  i < temp1.size(); i++){
			  this.g.newEdge(    node.get(   temp1.get(i)   )		, node.get(   temp2.get(i)   )	);
		  }
		
	}
	
/**
 * Setter to set the JSON data received from the user
 * Not used
 * @param jsonText
 */
	public void setNewGraphData(String jsonText){
		this.jsonText = jsonText;	
		parseJSONtext();
		
	}
	
	/**
	 * getter to get the  KCoreDecomposition
	 * @return KCoreDecomposition
	 */
	public KCoreDecomposition get_KCore(){			 
			   KCoreDecomposition kcore =  KCoreDecomposition.execute(g);
			 return kcore;		
		}

	/**
	 * The kcoreToJson method constructs the JSON String object
	 * @param k_core is a SortedMap<Integer, Set<Node>> where the Integer is the core number and the value the set of node of that core.
	 * @return String JSON with the node name and it k-core. 
	 */
	public String kcoreToJson(SortedMap<Integer, Set<Node>>  k_core){
		
		//###################################################################################################
		// In the following bunch of code, we would like to determine the number of edges between cores
		Graph gc = new PrimaryGraph(); gc = g; // we work on a copy and we'll remove the edges checked to boost the search
		//int edgesCores[][] = new int[k_core.keySet().size()][k_core.keySet().size()];
		int dim = k_core.keySet().size() + 1;
	
		
		Object[] dimm = k_core.keySet().toArray();
		//System.out.println("max value: " + (Integer)dimm[dimm.length -1]);
		int maxdim =(Integer)dimm[dimm.length -1];
		
		int edgesCores[][] = new int[maxdim+1][maxdim+1];
			
		   for(Integer i : k_core.keySet()){ // For each core
			   			  
			   Set<Node> set_Nodes = k_core.get(i);  // set of nodes that compose the core
			   		   
		   		for(Node n : set_Nodes){  // For each node in the set or core
		   			
		   				 ExtendedListIterable<Node>  neighbors = gc.adjacentNodes(n);  // get the neighbours of the visited node
		   				 
		   				 for(Node nn : neighbors) { // For each neighbours
		   					 
				   			   for(Integer j : k_core.keySet()){ // loop through each core
				   				  
				   				   if (j>i){  // half of the matrix, the upper triangle				   				
				   					   Set<Node> setNodes = k_core.get(j); 
					   					   if(setNodes.contains(nn)){
					   						   // increment the matrix of core edges 
					   						   		edgesCores[i][j] +=1;			   						   
					   					   }
					   					   
				   				   }
				   			   }
				   			   
		   				 }				   				    				 
		   		 
		   		}	   			
		   			
		   }	
		    
//###################################################################################################
		 
		 StringBuilder str = new StringBuilder("{\"kcores\": {");
			 
		   for(Integer i : k_core.keySet()){ // loop through each core
			   Set<Node> setNodes = k_core.get(i);  // set of nodes that compose the core
			   		   //System.out.println("core: " + i);
			   str.append("\"kcore" + i + "\":[ " );
		   		for(Node n : setNodes)
		   			str.append( "{\"data\":{\"id\":\"" + node_key.get(n) + "\",\"name\":\"" + n + "\"}},");
		   		
		   		str.replace(str.length() -1, str.length(), "],");   			
		   }
//*****************************************************************************	   
		  if (k_core.keySet().size() > 1){
			   
			   str.append("\"edges\":[") ;
			   
			   // Next we add the edges between the cores
				   for(Integer i : k_core.keySet()){ // again we loop through the number of core found 
					   	   					
						for(int j=i+1; j<= maxdim; j++){ 		
							if(edgesCores[i][j] != 0){
							   str.append("{\"data\":{\"source\":\"kcore" +  i  
									   	+ "\",\"target\":\"kcore" +  j 
									   	+ "\",\"int\":" +   edgesCores[i][j]
									   	+ "}},"	);	
							}
						}
					   
				   }
				   
				   if (str.substring(str.length() - ("\"edges\":[").length(), str.length()).equals("\"edges\":[") )
					   str.replace(str.length() - ("\"edges\":[").length(), str.length(), "");
				   else 
					   str.replace(str.length() -1, str.length(), "],"); 		   
		   }
		   
//*****************************************************************************
		   
		   str.replace(str.length() -1, str.length(), "");
		   str.append( "   }} ");
		   
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
