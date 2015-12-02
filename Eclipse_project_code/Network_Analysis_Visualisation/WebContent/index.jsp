<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<!-- ################################################################################################# -->
<!-- 
//##########################################
Programmer: axm1064 Akram M'Tir
MSc Student ID: 1402590 Stream 1
@date: 07/09/2014 
//##########################################

Master in Computer Science
Project Title: 
Network Analysis and Visualization. 
Identifying most prominent nodes.

//##########################################

 This JSP, main user interface (View MVC) offers five tabs, labelled by the name of the different functionalities and algorithms implemented .

The first tab simply allows the user to visualise and collect statistics about their networks prior to apply any algorithm.

The second tab implements the k-core decomposition algorithm.

The third tab offers the identification of prominent nodes by the Biased Random Walk algorithm.

The forth tab proposes the Profiling Core-Periphery algorithm.

And finally, the fifth tab selects prominent nodes based on the simple closeness and degree measure. 
This algorithm is used as a baseline algorithm.

 -->
<!-- ################################################################################################# -->

<head>
		<meta name="description" content="[Network Analysis and Graph visualization]" />
		<title>Most prominent nodes in a Network: Analysis and Graph visualization</title>
				
		<!--  To free our bandwidth, the user can directly download the library from Goolgle for instance. -->
		<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>  -->
		<script src="js/jquery-1.10.2.min.js"></script>
		<script src="js/jquery-ui.min.js"></script>
		
		<!-- https://code.google.com/p/flot/  http://www.flotcharts.org/ 
			Flot is a pure JavaScript plotting library for jQuery, with a focus on simple usage, 
			attractive looks and interactive features.
		-->
		<script src="js/jquery.flot.js"></script>
		
		
		<!-- jquery UI and theme JavaScripts libraries  -->	
		<link rel="stylesheet" href="css/jquery-ui.min.css">
		<link rel="stylesheet" href="css/jquery-ui.theme.min.css">
		<!-- My own style sheet, where I position, place, layout, and format the different component elements -->
		<link rel="stylesheet" href="css/Networkstyle.css">	
				
		<!-- Cytoscape.js JavaScript graph theory library for analysis and visualisation (http://cytoscape.github.io/cytoscape.js/) -->
		<script src="js/cytoscape.js"></script>
		<!-- arbor.js library: a graph visualization library using web worker and jQuery (http://arborjs.org/)  -->
		<script src="js/arbor.js"></script>
</head>


<body>

		<!-- header -->
		<h1 class="header_footer">Most prominent nodes in a Network: Analysis and Graph visualization</h1>
		

<div id="tabs">
	<ul>
	<li><a href="#metrics" 
	title="In this Metrics Network tab you can obtain statistics about you network, 
	such as the size, the average path, the diameter, centralisation measures, and the degree distribution."
	>Metrics Networks</a></li>
	<li><a href="#kcore"
	title="K-Core node partitioning of a graph.
	Reference: S. B. Seidman, 1983, Network structure and minimum degree, Social Networks 5:269-287">K-Core Decomposition</a></li>
	<li><a href="#brandomwalk"
	title="The Biased Random Walk BRW algorithm identifies l top prominent nodes.
	Reference: Identifying Prominent Actors in Online Social Networks using Biaised Random Walks. Frank w. Takes, Walter A. Koster, Leiden University, The Netherlands." 
	>Biased Random Walk</a></li>
	<li><a href="#procoreperif"
	title="The Core-Periphery Profile CPP algorithm is used to identify l top prominent nodes in the network. 
	The CPP provides a topological portrait of a network. 
	A numerical indicator, the coreness, is attributed to each node, highlighting its rank and role.
	Reference: Profiling core-periphery network structure by random walkers, 
	Fabio Della Rossa, Dercole, Piccardi, scientific reports, nature.com, March 2013"
	>Profiling Core-Periphery</a></li>
	<li><a href="#degreeProfile"
	title="Baseline algorithm. The degree is a good indicator of the importance of a node, 
	but does not take into account indirect ties.
	Therefore, we retrieve the l top nodes of the network based on their closeness, which emphasizes the distance of a node to all others. 
	If two nodes have the same closeness measure, then we sort them based on their degree."
	>Closeness Degree Algorithm</a></li>	
	</ul>

<!--  ###############################################################
 Statistics Metrics Networks Graphs 
 ################################################### 	 -->

	<div id="metrics">
	
					<!-- Settings for the layout prior to load the file network diagram -->
		<div id="settings">
		
						
		<div id= "box_layout">
					<label style="font-weight:bold;">Select a layout</label>
						<select name="layout" id="laySelect">
						<option selected="selected">Dynamic</option>
							<option>Random</option>
							<option>Grid</option>
							<option>Concentric</option>
							<option>Circle</option>
							<option>BreadthFirst<option>
						</select>									
		</div>			


		<div id= "box_layout">
			<label for="spinner" style="font-weight:bold;">Time [ms]</label>
			<input id="spinner" name="spinner" value="4000" >
			<div id="slider"></div>	
		</div>	
		
	
			
	 <div id="spaceMetrics"> </div> 	
	
	<div id="densityPlotlayout"> </div> 
	
	<div id="spaceMetrics2"> </div> 	
				
		<div id= "box_load_File">					
			<div id="loadNetwork">
					<!-- button to load the network file graph in the Pajek format -->
					<label style="font-weight:bold;">Load Network</label>
				<a href="#" title="Load any network text (.txt format) file, using the edge list representation. 
				Each line in the network text file should contain an edge as follow: [node_source][SpaceCharacter][node_destination]. "	>
					<input type="file" id="fileinput" >  
				</a>
					
			<!--  	<div id="characteristics"> </div> -->
					
			</div>
		</div>
					

				
		</div>
		
				
		<!-- Element reserved for the display of the network graphs -->
		<!--  <div id='network_graph'></div>	-->	
		
		<!-- Element reserved for the display of the result of the algorithm on the original network graphs -->
		<div id='resNetGraph'>
		
			<div id='figuresMetrics' >
			
				<div id='Networkstatistics' ></div>
				<div id='degreeDistribution' ></div>
				
			</div>
		
			<div id='GraphNodes' ></div>
			
			
		
		
		</div>
	
	
	
	
	</div>
	
<!--  ###############################################################
 KCore decomposition 
 ################################################### 	 -->	
	<div id="kcore">

		<!-- Settings for the layout prior to load the file network diagram -->
		<div id="settings">
		
						
		<div id= "box_layout">
					<label style="font-weight:bold;">Select a layout</label>
						<select name="layout" id="laySelect">
						<option selected="selected">Dynamic</option>
							<option>Random</option>
							<option>Grid</option>
							<option>Concentric</option>
							<option>Circle</option>
							<option>BreadthFirst<option>
						</select>									
		</div>			


		<div id= "box_layout">
			<label for="spinner" style="font-weight:bold;">Time [ms]</label>
			<input id="spinner" name="spinner" value="4000" >
			<div id="slider"></div>	
		</div>	
		
		
		<div id="radio">
		<label for="radio" style="font-weight:bold; font-size:medium;">Display Option</label>
			 
			<input type="radio" id="allNodes" name="radio" checked="checked"><label for="allNodes">All Nodes</label>
			<input type="radio" id="kcoreOnly" name="radio" ><label for="kcoreOnly">K-Core Only</label>			
					
		</div>
		
				
		<div id= "idLegend">	
		<label  style="font-size:medium;" >Legend</label>
		<!-- Added dynamically by accessing the DOM elements
			<div class="legend1">Core1</div> 
			<div class="legend2">Core2</div>
			<div class="legend3">Core3</div>	
		-->	
		</div>
		
		<div id="listNodes">
		
			<div id="accordionKCore">
		
		 <!--  Dynamically inserted by jQuery javaScript
					<h3>Core1</h3>
						<div>
	
						</div>
				
					<h3>Core2</h3>
						<div>
	
						</div>
						
					<h3>Core3</h3>
						<div>
	
						</div>
		-->
									
			</div>
			
		 </div>
		
				
		<div id= "box_load_File">					
			<div id="loadNetwork">
					<!-- button to load the network file graph in the Pajek format -->
					<label style="font-weight:bold;">Load Network</label>
					<input type="file" id="fileinput1" >  
					
			<!--  	<div id="characteristics"> </div> -->
					
			</div>
		</div>
					

				
		</div>
		
		
				
		<!-- Element reserved for the display of the network graphs -->
		<!--  <div id='network_graph'></div>	-->	
		
		<!-- Element reserved for the display of the result of the algorithm on the original network graphs -->
		<div id='resNetGraph'></div>
		
		<div id= "algoMetrics" align="center"> </div>

	</div>
	
<!--  ###############################################################
 brandomwalk corresponds to the second tab for the Biased Random walk algorithm
 ################################################### 	 -->	
	<div id="brandomwalk">

				<!-- Settings for the layout prior to load the file network diagram -->
		<div id="settings">
		
						
		<div id= "box_layout">
					<label style="font-weight:bold;">Select a layout</label>
						<select name="layout" id="laySelect">
						<option selected="selected">Dynamic</option>
							<option>Random</option>
							<option>Grid</option>
							<option>Concentric</option>
							<option>Circle</option>
							<option>BreadthFirst<option>
						</select>									
		</div>			


		 <div id= "box_layout">
			<label for="spinner" style="font-weight:bold;">Time [ms]</label>
			<input id="spinner" name="spinner" value="4000" >
			<div id="slider"></div>	
		</div>	
		
	 	<div id="spaceRoom"></div> 	
	 			
		<div id= "boxReducedLayout">
		
			<label for="spinnProb"  style="font-weight:bold;">Probability</label>
			<input id="spinnProb" name="proba" value = 0.15> <!-- as suggested in literature "Sampling for large Graphs. J. Leskovec C. Faloutsos."  -->
		
		</div>
		
		<div id= "boxReducedLayout">	
			<label for="spinnAlpha" style="font-weight:bold;"> 0 &lt &alpha; &lt 1</label>
			<input id="spinnAlpha" name="alpha" value = 0.5> <!--  It turned out that nay value between 0.2 and 0.8 gage decent results and therefor we fixed the parameter to 0.5. -->
		</div>
		
		<div id= "boxReducedLayout">	
			<label for="spinnNbrIt" style="font-weight:bold;">Iterations</label>
			<input id="spinnNbrIt" name="nbrIter" value=100000> <!--  Number of iterations ~ k x n should be set to a value significantly lager than the number of nodes n -->
		</div>
		
		<div id= "boxReducedLayout">	
			<label for="spinnNbrNodes" style="font-weight:bold;">l top Nodes</label>
			<input id="spinnNbrNodes" name="nbrNodes" value=10> <!--  Number of prominent nodes to retrieve from the server -->
		</div>


	 	<div id="boxPlotlayout"> </div> 
		   <!--  <div id= "box_layout"> </div> -->

		
		<div id= "ListPromNodes">
		
			<div id="accordionNodes">
			
			<!--   Dynamically inserted by jQuery javaScript
						<h3>Prominent Nodes</h3>
							<div>
		
							</div>
			-->
																
			</div>
				
		
		
		</div>


		<div id= "box_layout" >					
			<div id="loadNetwork">
					<!-- button to load the network file graph in the Pajek format -->
					<label style="font-weight:bold;">Load Network</label>
					<input type="file" id="fileinput2" >  
					
			<!--  	<div id="characteristics"> </div> -->
					
			</div>
		</div>
					

				
		</div>
		
				<!-- Element reserved for the display of the result of the algorithm on the original network graphs -->
		<div id="resNetGraph">
		
		</div>
		
		<div id= "algoMetrics" align="center"> </div>
	
	
	</div>
	
<!--  ###############################################################	
Core-Periphery Profile
############################################################### -->
	<div id="procoreperif">
	
					<!-- Settings for the layout prior to load the file network diagram -->
		<div id="settings">
		
						
		<div id= "box_layout">
					<label style="font-weight:bold;">Select a layout</label>
						<select name="layout" id="laySelect">
						<option selected="selected">Dynamic</option>
							<option>Random</option>
							<option>Grid</option>
							<option>Concentric</option>
							<option>Circle</option>
							<option>BreadthFirst<option>
						</select>									
		</div>			


		<div id= "box_layout">
			<label for="spinner" style="font-weight:bold;">Time [ms]</label>
			<input id="spinner" name="spinner" value="4000" >
			<div id="slider"></div>	
		</div>	
		
			
		<div id="spaceMetrics"> </div> 	
	<!--    	<div id="legendCorePeriphProf"> </div> 	 -->	
	  <!--
	   	<div id="boxPlotCPC"> </div> 
	    <div id="boxPlotStrength"> </div> 
	    <div id="boxPlotCompar"> </div> 

-->
	   			
		<div id= "boxReducedLayout">	
			<label for="spinnNbrNodes" style="font-weight:bold;">l top Nodes</label>
			<input id="spinnNbrNodes" name="nbrNodes" value=10> <!--  Number of prominent nodes to retrieve from the server -->
		</div>     
   		
		<div id= "CorePeriProfNodes">
		
			<div id="accordionNodes">
			
			<!--   Dynamically inserted by jQuery javaScript
						<h3>Prominent Nodes</h3>
							<div>
		
							</div>
			-->
																
			</div>
			
	
		</div>	
		
					
		<div id= "box_load_File">					
			<div id="loadNetwork">
					<!-- button to load the network file graph in the Pajek format -->
					<label style="font-weight:bold;">Load Network</label>
					<input type="file" id="fileinput3" >  
					
			<!--  	<div id="characteristics"> </div> -->
					
			</div>
		</div>

				
		</div>
		
							

<!-- ############################# EndboxPlotCPC of the setting bar on the left ###################### -->		
		
				
		<!-- Element reserved for the display of the network graphs -->
		<!--  <div id='network_graph'></div>	-->	
		
		<!-- Element reserved for the display of the result of the algorithm on the original network graphs -->
		<div id='resNetGraph'>
		
			<div id="legendVertPCP"> </div> 
		
		<div id='PCP_Graph1' ></div>
		
			<div id='PCP_Graph2' ></div>
			
			<div id='PCP_Graph3' ></div>
			
			<div id='PCP_Graph4' ></div>
		
		</div>
	
	<div id= "algoMetrics" align="center"> </div>
	
	</div>
	
	
	<!--  ###############################################################	
Simple degree Profile
############################################################### -->
	<div id="degreeProfile">
	
					<!-- Settings for the layout prior to load the file network diagram -->
		<div id="settings">
		
						
		<div id= "box_layout">
					<label style="font-weight:bold;">Select a layout</label>
						<select name="layout" id="laySelect">
						<option selected="selected">Dynamic</option>
							<option>Random</option>
							<option>Grid</option>
							<option>Concentric</option>
							<option>Circle</option>
							<option>BreadthFirst<option>
						</select>									
		</div>			


		<div id= "box_layout">
			<label for="spinner" style="font-weight:bold;">Time [ms]</label>
			<input id="spinner" name="spinner" value="4000" >
			<div id="slider"></div>	
		</div>	
		
			
			
	   	<div id="legendDegreeProf"> </div> 		
	  <!--
	   	<div id="boxPlotCPC"> </div> 
	    <div id="boxPlotStrength"> </div> 
	    <div id="boxPlotCompar"> </div> 

-->
	
		<div id="spaceMetrics"> </div> 
	   			
		<div id= "boxReducedLayout">	
			<label for="spinnNbrNodes" style="font-weight:bold;">l top Nodes</label>
			<input id="spinnNbrNodes" name="nbrNodes" value=10> <!--  Number of prominent nodes to retrieve from the server -->
		</div>     
   		
		<div id= "degNodes">
		
			<div id="accordionNodes">
			
			<!--   Dynamically inserted by jQuery javaScript
						<h3>Prominent Nodes</h3>
							<div>
		
							</div>
			-->
																
			</div>
			
	
		</div>	
		
					
		<div id= "box_load_File">					
			<div id="loadNetwork">
					<!-- button to load the network file graph in the Pajek format -->
					<label style="font-weight:bold;">Load Network</label>
					<input type="file" id="fileinput4" >  
					
			<!--  	<div id="characteristics"> </div> -->
					
			</div>
		</div>

				
		</div>
		
							

<!-- ############################# EndboxPlotCPC of the setting bar on the left ###################### -->		
		
				
		<!-- Element reserved for the display of the network graphs -->
		<!--  <div id='network_graph'></div>	-->	
		
		<!-- Element reserved for the display of the result of the algorithm on the original network graphs -->
		<div id='resNetGraph'>
<!-- 		
		<div id='PCP_Graph1' ></div>
		
			<div id='PCP_Graph2' ></div>
			
			<div id='PCP_Graph3' ></div>
			
			<div id='PCP_Graph4' ></div>
		 -->
		</div>
	
	<div id= "algoMetrics" align="center"> </div>
	
	</div>
	
			<div id="dialog" title="Handling your request ...">
			<div id="progress-label"><p>Please Wait...</p></div>
			<div id="progressbar"></div>
		</div>
	

</div>


		
		<!-- footer -->
		<div class="header_footer">
			<p><span>UB: Network Graph analysis and  visualisation using open-source graph theory libraries. Copyleft 2014 MSc CS project. All rights reserved.</span></p>
		</div>
		
		<!-- Load the javascript script to be executed once the file network graph is loaded.
			The script simply parse the a network in Pajek format and convert it to a JSON Object
			and call the display graph method. It is easier to export the file to the server using the JSON format. -->
		<script src="js/load_Parse_Display_File.js"></script>
		<script src="js/widgets_UIjquery_interaction.js"></script>
		
</body>


</html>