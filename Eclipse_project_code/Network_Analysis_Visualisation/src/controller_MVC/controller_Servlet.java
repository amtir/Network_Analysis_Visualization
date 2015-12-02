package controller_MVC;

import gr.forth.ics.graph.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kcores_algorithm.KCoreDecomposition;
import kcores_algorithm.KeyFinder;
import kcores_algorithm.kcoreDecomp;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import degreeSelect.DegreeSelect;
import biasRandomWalkAlgo.biasRandomWalk;
import profilCorePeriph.ProfilCorePeriph;
import statsMetrics.metrics;


/**
 *
 * The controller_Servlet class implements the Controller in our MVC Model View Controller
 * It receives the user request and dispatches it to the adequate algorithm servlet class/bean 
 * to perform the calculation, and finally returns the results to the user in 
 * a JSON object format (AJAX behind the scene, handled on the client/user browser side).
 * 
 * This class extends the abstract class HttpServlet and implements/overrides the  doPost() method
 * to handle the POST request. The HTTP POST method allows the client to send data of unlimited length to the Web server.
 * 
 * Information/data/message between the client/user and the server are exchanged in the JSON Object format.
 * 
* @author axm1064  ID: 1402590
* @since 08/09/2014
 */
//@WebServlet("/process_NetGraph")
public class controller_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public controller_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * The doPost method receives the request+data/network from the user in JSON object format.
	 * It deserialise the data object, reads the request, dispatches it to the adequate algorithm servlet class/bean 
	 * to perform the calculation, and finally returns the results to the user in 
	 * a JSON object format (AJAX on the client/user browser side).
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		// this servlet acts as a controller 
		// First we retrieve the algorithm type in the header of the message received.
		// Next we call the corresponding adequate bean Model.
		
		response.setContentType("text/json");
		
		PrintWriter out = response.getWriter();		
		String str_json = request.getParameter("obj_JSON");
				
		// The following LinkedList are buffers, used to retrieve the algorithm
		  LinkedList< String> strAlgo = new LinkedList<String>();
		
		  // From the text loaded, we extract the algorithm names the user is interested in.
		  strAlgo =  find(str_json, "algorithm"); 
		  String algoType = strAlgo.get(0);

	//#################################################################################
		  if (algoType.equals("metrics")) { // metrics algorithm  
	//#################################################################################			  
		  
			  metrics stat = new metrics(str_json);
			  String strResult = stat.metricsToJson();
			  out.println( strResult);
				 
				
			out.flush(); // force to send the result to the caller user client
			out.close(); // close the resource 
		  
		  
		  }
	//#################################################################################
		  else if (algoType.equals("kcore")) { // kcore algorithm 
	//#################################################################################			  
				
			  kcoreDecomp kc = new kcoreDecomp(str_json);		
				 KCoreDecomposition KCoreDec =  kc.get_KCore();
				 			 
				 SortedMap<Integer, Set<Node>>  k_core = KCoreDec.getCores();
							 
				 String strResult =  kc.kcoreToJson(k_core);
				
				   out.println( strResult);
			 
		
				out.flush(); // force to send the result to the caller user client
				out.close(); // close the resource 
				
		  }
	//#################################################################################
		  else if(algoType.equals("brandomwalk")){ // biais random walk
	//#################################################################################		  
	// Identifying Prominent Actors in Online Social Network using Biaised Random Walks, Frank W. Takes, Walter A. kosters, Leiden University, Netherlands.
			
			  // The following LinkedList are buffers, used to retrieve the algorithm
			  LinkedList< String> numbIter = new LinkedList<String>();
			  LinkedList< String> numbNodes = new LinkedList<String>();
			  LinkedList< String> proba = new LinkedList<String>();
			  LinkedList< String> alpha = new LinkedList<String>();
			
			  // From the text loaded, we extract the parameter of the algorithm, namely number of iteration, probability, and alpha value.
			  numbIter =  find(str_json, "numbIter"); 
			  String nmbIter = numbIter.get(0);
			  
			  numbIter =  find(str_json, "numbNodes"); 
			  String nmbNodes = numbIter.get(0);
			  
			  proba =  find(str_json, "prob"); 
			  String prob = proba.get(0);
			  alpha =  find(str_json, "alpha"); 
			  String alph = alpha.get(0);
			  
			  System.out.println("nmbNodes " + nmbNodes + " " + Integer.parseInt(nmbNodes));
			  biasRandomWalk brwObj = new biasRandomWalk(str_json,  Integer.parseInt(nmbIter), Integer.parseInt(nmbNodes),  Double.parseDouble(prob),  Double.parseDouble(alph) );
			  
			  brwObj.runBiasRandWalk();
			  
			  String strResult =  brwObj.brwToJson();
			  
			  out.println( strResult);
				 
				
			out.flush(); // force to send the result to the caller user client
			out.close(); // close the resource 
			  
			  
		  }
	//#################################################################################
		  else if(algoType.equals("procoreperif")){ 
	//#################################################################################		  
	
			  ProfilCorePeriph pcp =  new ProfilCorePeriph(str_json, 11 );
			
			  pcp.runProfilCorePeriph();
			  
			  pcp.printSortedNodes();
			  
			  String strResult =  pcp.pcpToJson();
			  System.out.println(strResult);
			  out.println( strResult);
				 
				
			out.flush(); // force to send the result to the caller user client
			out.close(); // close the resource 
			  
		  }
	//#################################################################################
		  else if(algoType.equals("degreeProfile")){ 
	//#################################################################################		  
	
			  DegreeSelect deg =  new DegreeSelect(str_json );
			
			  
			  String strResult =  deg.degToJson();
			  
			  
			  System.out.println(strResult);
			  out.println( strResult);
				 
				
			out.flush(); // force to send the result to the caller user client
			out.close(); // close the resource 
			  
		  }
       		
		
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
		