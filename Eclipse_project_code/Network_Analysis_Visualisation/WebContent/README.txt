//##########################################
Programmer: axm1064 Akram M'Tir
MSc Student ID: 1402590
@date: 07/09/2014 
//##########################################

Master in Computer Science
Project Title: 
Network Analysis and Visualization. 
Identifying most prominent nodes.

//##########################################
Abstract, Summary:
//##########################################

The aim of this Master Project in Computer Science, is to analyze and visualize different networks, ranging from social networks to biology networks. 
More precisely, we focused on tackling and addressing the problem of identifying most prominent nodes in different medium scale networks. 
Three highly technical and complex detection algorithms have been implemented, namely the k-cores algorithm, the bias random walk 
and finally the core-periphery profile. (Research project)
These algorithms have been evaluated, compared and contrasted to a baseline algorithm using four data sets 
where the prominents nodes are known.

To reach this goal, I devised a web application, a client server application, offering an online service to users interested in visualizing,
 analysing and characterizing their networks (Software project).
This software framework have been developed using the latest technology such as jQuery, java-script, HTML5, CSS3, AJAX, JSON, 
Java EE API, servlet, JSP and Tomcat server. The originality of this work is that it offers an online service, 
a web application to users who want to analyse their networks to determine the most important vertices.

//##########################################
Description of the application:
//##########################################

 This online web-application implements four algorithms to detect the most important nodes in a network.
This User Interface, the browser offers five tabs, labelled by the name of the different functionalities and algorithms implemented .

The first tab simply allows the user to visualise and collect statistics about their networks prior to apply any algorithm.

The second tab implements the k-core decomposition algorithm.

The third tab offers the identification of prominent nodes by the Biased Random Walk algorithm.

The forth tab proposes the Profiling Core-Periphery algorithm.

And finally, the fifth tab selects prominent nodes based on the simple closeness and degree measure. 
This algorithm is used as a baseline algorithm.

 The format of the network files uploaded by users, are basically a consecutive list of edges of the graph, 
where each line in the file contains a start node of an edge and a finish node separated by a space character. 
Samples are provided under the data repository for demonstration purpose. 
------------------------------------------------------
# This is a comment
# Let's define a network (.txt) in the form of indirected and unweighted graph
# For example a star network 

node1 node2
node1 node3
node1 node4
node1 node5
node1 node6

@ Prominent node if known, let's assume node1 is the unique prominent node 
node1
----------------------------------------------------


(*)Important notes concerning the implementation:
 Once the file (.txt) is uploaded, a JavaScript code parses it and transforms it into a JSON object, sends it to the Tomcat7 server using the AJAX technology
where the JAVA algorithm (JSP, Servlets) is applied, and another JSON object with the information requested is created and sent back to the user browser.

//##########################################
Client/Server side: libraries imported
//##########################################
 Java jars:
	
	flexigraph.jar (A Java graph algorithms library, mainly developed in 2006-7. ) 
	https://code.google.com/p/flexigraph/
	
	json-simple-1.1.1.jar (JSON.simple is a simple Java toolkit for JSON. You can use JSON.simple to encode or decode JSON text.)
	https://code.google.com/p/json-simple/
	
JavaScript libraries: 

	A graph visualization library using web workers and jQuery
	arbor.js		
	arbor-tween.js   http://arborjs.org/

	An open-source JavaScript graph theory (a.k.a. network) library for analysis and visualisation
	cytoscape.js	http://cytoscape.github.io/cytoscape.js/?utm_source=javascriptweekly

	Flot is a pure JavaScript plotting library for jQuery,
	jquery.flot.js	www.flotcharts.org

	jQuery UI is a curated set of user interface interactions, effects, widgets, 
	and themes built on top of the jQuery JavaScript Library.
	jquery-1.10.2.min.js	http://jquery.com/
	jquery-ui.min.js
	jquery-ui-1.10.3.custom.min.js


CSS
	jquery-ui.min.css
	jquery-ui.theme.min.css
	jquery-ui.structure.min.css




//######################################################