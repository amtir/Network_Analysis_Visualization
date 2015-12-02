//##########################################################################
// Global scope variable useful along the whole life of the program

var nodes_N; // number of nodes or vertices in the network.
var edges_E; // number of edges in the graph.
var nodes = {};
var PromNodes = {};
var edges = Array();
var metrics = {};
					
function _parse_Pajek_Network(contents){

					// Because how files are handled between OS, once the content is loaded we remove any \r invisible carriage return character.
					contents = contents.replace(/(\r)/gm,"");		
					// Next we split based on a \n new line character, which returns an array-like 
					var array = contents.toString().split("\n"); 
					
					// then we start formatting our JSON text file.
					var txt_json= '{"elements":{"nodes":['; 
															
					// Loop through the array, line by line in the content of the file network graph
					for ( i in array){
					
						if ( array[i].search('Vertices') != -1 ) // looking for a line that contain the string *Vertices. Better use regular expressions
						{
						// Extract the number of vertices in the network
							nodes_N = parseInt( (((array[i]).split(" "))[1]).trim()) ;
							//console.log( "nodes_N  " + nodes_N);	// print in the console the number of vertices
						}
						else if ( array[i].search("Edges") != -1)  // looking for a line that contain the string *Edges
						{
						// Extract the number of edges
							edges_E = parseInt( (((array[i]).split(" "))[1]).trim() );
							// console.log( "edges_E  " + edges_E);  // print the console the number of edges.
						}
					}
					
					
				// Constructing the data part composed of nodes_N vertices. (JSON)
				for(var j=1; j<= nodes_N; j++){  		
					txt_json.trim();	
					txt_json = txt_json + 		
					'{"data":{"id":"' +(((array[j+1]).split(" "))[0])    +  
					 '","name":' + (((array[j+1]).split(" "))[1]).trim()  + '}},'	;						
				}	
				txt_json.trim();
				txt_json = txt_json.substr(0,(txt_json.length-1)); // remove any last comma.
				txt_json.trim();
				txt_json = txt_json + '],'; // end of the data node part
			
				// Constructing the edges part composed of edges_E edges.
				 txt_json = txt_json +  '"edges":[' ;
		  				
				for(j=1; j<= edges_E; j++){ 		
					txt_json.trim();	
					txt_json = txt_json + // the parsing is based on the space character. Therefore space are not allowed in the id and name definition.
					'{"data":{"source":"' +(((array[j+2+nodes_N]).split(" "))[0])    +   	
					 '","target":"' + (((array[j+2+nodes_N]).split(" "))[1])  + '"}},'	;			
				} 
				txt_json.trim();
				txt_json = txt_json.substr(0,(txt_json.length-1));  // remove any last comma.
				txt_json.trim();
				txt_json = txt_json + ']}}';  // end of the JSON text construction.
				txt_json.trim();
				

return txt_json;
}



//##########################################################################
// According to the article "Identifying Prominent Actors in Online Social Networks using Biased Random Walks"
// W. Takes, A. Kosters, from the Leiden University, The Netherlands
// l is the top l nodes of the sorted list I. I is orderNodes.
// W the subset that contains the prominent nodes. W = PromNodes
// with |W| = k

function metricsPromNodes( PromNodes, orderNodes , l){
	// l is the subset W that we retrieve.
	var k = Object.keys(PromNodes).length; // set W of k elements 
	var dim = Object.keys(orderNodes).length;
	var metric = {};
	var precision = 0.0;
	
	for (var t=dim-l; t<dim; t++){ // the last part: the most prominents nodes. 

		//console.log("==============================================");
		for(var key in PromNodes) {
			
			if(  orderNodes[t][0] === key  ) {
								
				precision = precision + 1.0;
								
			}
		}
		
	}
	
	// Recall = | W intersection I | / | W |
	var recall = precision/k;
	// Precision = | W intersection I | / | I |
	precision = precision/l;
	var fMeasure = 2*precision*recall/(precision+recall);

	metric["precision"] = (100*precision).toPrecision(4);
	metric["recall"] = (100*recall).toPrecision(4);
	metric["fMeasure"] = (100*fMeasure).toPrecision(4);
	
	return metric;
}
//**********************************************************
function metPromNodes( PromNodes, orderNodes , l){
	
	var k = Object.keys(PromNodes).length;
	var dim = Object.keys(orderNodes).length;
	var metric = {};
	var precision = 0.0;
	
	for (var t=0; t<l; t++){ // the most prominents nodes. 

		//console.log("==============================================");
		for(var key in PromNodes) {
			
			if(  orderNodes[t][0] === key  ) {
								
				precision = precision + 1.0;
								
			}
		}
		
	}
	
	// Recall = | W intersection I | / | W |
	var recall = precision/k;
	// Precision = | W intersection I | / | I |
	precision = precision/l;
	var fMeasure = 0.0;
	if ( precision!=0 || recall!=0) fMeasure = 2*precision*recall/(precision+recall);

	metric["precision"] = (100*precision).toPrecision(4);
	metric["recall"] = (100*recall).toPrecision(4);
	metric["fMeasure"] = (100*fMeasure).toPrecision(4);
	
	return metric;
}
//##########################################################################
/* _draw_Network_Graph() function
   display draw network graph method
Input arguments:
	1 - JSON Object representation of the Network graph.
	2 - HTML node element where to plot draw the network graph.
	3 - Layout type selected: concentric, random, circle or grid.

Output: visual graphic network graph
*/

function _draw_Network_Graph(obj_JSON, id_html_elem, layout_option){
	
	var lay_out = $(  id_html_elem +" #laySelect" ).val().toLowerCase();

	 if (  lay_out == "dynamic") { 
		 if (id_html_elem=="#procoreperif")
			 var dest = " #resNetGraph #PCP_Graph4";
		 else if(id_html_elem=="#metrics")
			 var dest = " #resNetGraph #GraphNodes";
		 else
			 var dest = " #resNetGraph";
		 
			$(id_html_elem + dest).cytoscape({
			  style: cytoscape.stylesheet()
			                .selector('node')
			                .css({
			                	'content': 'data(name)',
			                    'text-valign': 'center',
			                    'color': 'white',					
			                    'text-outline-width': 1,
			                    'text-outline-color': 'black',
								'background-color': 'white',
								'border-width':3,
								'border-color': 'white',
								'height': 'mapData(weight, 0, 100, 40, 500)',
						        'width': 'mapData(weight, 0, 100, 40, 500)'
			            })
			                .selector('edge')
			                .css({
			                'target-arrow-shape': 'none',
							'width': 'mapData(intedg,0, 100, 3, 20)' ,//3,
							'background-color': '#E8E8E8',
							'line-color': '#E8E8E8'
			            })
			    ,
			  
			  elements: obj_JSON.elements,
			  
			  
			  layout: {
				
				    name: 'arbor',
				    arbor: function(){ return this.data('weight'); },
				    liveUpdate: true, // whether to show the layout as it's running
				    ready: undefined, // callback on layoutready 
				    stop: undefined, // callback on layoutstop
				    maxSimulationTime: parseInt( $('#spinner').spinner("value") ), // 10000, // max length in ms to run the layout
				    fit: true, // reset viewport to fit default simulationBounds
				    padding: [ 40, 40, 40, 40 ], // top, right, bottom, left
				    simulationBounds: undefined, // [x1, y1, x2, y2]; [0, 0, width, height] by default
				    ungrabifyWhileSimulating: true, // so you can't drag nodes during layout
			
				    // forces used by arbor (use arbor default on undefined)
				    repulsion: undefined,
				    stiffness: undefined,
				    friction: undefined,
				    gravity: true,
				    fps: undefined,
				    precision: undefined,
			
				    // static numbers or functions that dynamically return what these
				    // values should be for each element
				    nodeMass: undefined, 
				    edgeLength: undefined,
			
				    stepSize: 1, // size of timestep in simulation
			
				    // function that returns true if the system is stable to indicate
				    // that the layout can be stopped
				    stableEnergy: function( energy ){
				      var e = energy; 
				      return (e.max <= 0.5) || (e.mean <= 0.3);
				    }    
			    
			    
			    
			  }
			
			
			
			  
			});

	 }else {
		 
		 if (id_html_elem=="#procoreperif")
			 var dest = " #resNetGraph #PCP_Graph4";
		 else if(id_html_elem=="#metrics")
			 var dest = " #resNetGraph #GraphNodes";
		 else
			 var dest = " #resNetGraph";
		 
		 $(id_html_elem + dest).cytoscape({
			  style: cytoscape.stylesheet()
                .selector('node')
                .css({
                'content': 'data(name)',
                    'text-valign': 'center',
                    'color': 'white',					
                    'text-outline-width': 1,
                    'text-outline-color': '#000',
					'background-color': 'white',
					'border-width':3,
					'border-color': 'white',
					'height': 'mapData(weight, 0, 100, 40, 300)',
			        'width': 'mapData(weight, 0, 100, 40, 300)'
            })
                .selector('edge')
                .css({
                'target-arrow-shape': 'none',
				'width': 3,
				'line-color': 'white'
            })
    ,
  
			  
			  elements: obj_JSON.elements,
			  
			  
			  layout: {

					name: lay_out , //'random',
					lay_out: function(){ return this.data('weight'); },
					ready: undefined,
					stop: undefined,
					padding: 5, // padding used on fit
					fit: true,   // whether to fit the viewport to the graph
					ready: undefined, // callback on layoutready
				    stop: undefined // callback on layoutstop
			  }
		 
			  });
		 
	 }

} 

//###############################################################

function plot_nodes_graph(data, txt_json){
	 	// data is the object received 
		// data is the object sent to the server 
		
	
	var txtText = $( "#dialog #progress-label" ).text();
	$( "#dialog #progress-label" ).text( txtText + "\n" + "Algorithm Results received from the server ..." );
	txtText = $( "#dialog #progress-label" ).text();
	$( "#dialog #progress-label" ).text( txtText + "\n" + "Formatting and presentation ..." );
	 	
	var obj_JSON = JSON.parse(txt_json);	
	var countLegend = 0;
	//console.log("Before String txt_json: " + txt_json);
	
	var safeColors = [ "gold", "teal", "red", "olive", "blue", "green",   "salmon", "pink", "purple", "orange", 
	                   "orangered",   "darkred" ,"gray" , "darkgreen" , "darkblue", "Cyan", "Crimson" , "RosyBrown", 
	                   "SpringGreen", "YellowGreen", "SlateGray", "Sienna", "PapayaWhip", "PaleGreen"  ];
	
	var active = $( "#tabs" ).tabs( "option", "active" );
	var destGraph = $("#tabs ul>li a").eq(active).attr('href');
	console.log(destGraph);
	
	var v =JSON.stringify(data);
	metrics = {};
	
//#################################################################################	
//#################################################################################	
if(destGraph=="#metrics"){	
//#################################################################################	
//#################################################################################	

	console.log("Object received: " + v);		
	var pcp = data.metrics; // we get the Profiling Core-Periphery  objects
	var distrib = [];
	var denseDeg = [];
	var distDeg = Array();
	var comparing = [];
	var rank = 0;
	var orderNodes= {};
	var strStats = "<font size='3'><strong>Network Statistics and Properties: </strong><br>";
	var elemPromNodes = '{"elements":{"nodes":['; 
	//#################################################################################	
	
	strStats += "<table>";
	
	$.each(pcp, function(key, value) { // 
				
		if (key=="nodes"){
			for(var prop in value){ 
			elemPromNodes += "{\"data\":{\"id\":\"" + value[prop].data["id"] +"\",\"name\":\""+ value[prop].data["name"] + "\"},\"css\":{\"background-color\": \"" + "teal" + "\"}},";					
			
			distDeg.push( Number(value[prop].data["deg"]));
			
			}
			
		}
		else if (key=="edges"){
			
			elemPromNodes.trim();
			elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1));
			elemPromNodes = elemPromNodes + '], "edges":[' ;
			
			for(var prop in value){ 
				
				elemPromNodes = elemPromNodes + // the parsing is based on the space character. Therefore space are not allowed in the id and name definition.
				'{"data":{"source":"' +  value[prop].data["source"]
				+ '","target":"' +  value[prop].data["target"]  
				+ '"}},'	;
			
			}

			
		}else if (key == "stats"){
			for(var prop in value){ // Loop to access each element in the current core.
				strStats += "<tr>";
				strStats += "<td>" + value[prop].data["id"] + "</td>"   
				+ "<td>" + "  <span style ='color:#AFE38F'>" + Number(value[prop].data["val"]).toPrecision(4) + "</span></td></tr>" ; // + <br>";				
			}
			
		}
		

	});
	
	var max = Math.max.apply(null,distDeg);
	var plotDistDeg = new Array(max);
	
	//initialisation
	for(var i =0; i<=max; i++)
		plotDistDeg[i] = 0.0;
	//constructing the histogram by cumulating the values
	for(var val in distDeg){
		plotDistDeg[Number(distDeg[val])] +=1; //Number(distDeg[val]);
	}
	
	var cumul = 0.0;
	for(var i =0; i<=max; i++){
		distrib.push([i,plotDistDeg[i]]);
		
		 cumul += plotDistDeg[i];
				
		/*if (i!=0) 
			denseDeg.push([i,denseDeg[i-1] + plotDistDeg[i]]);
		else*/
			denseDeg.push([i, cumul ]);
	}

	max = Math.max.apply(null,plotDistDeg);
	
	var options = {
			series: {
			label: "Degree distribution",
				lines: { show: true , fill: true, fillColor: "rgba(175, 227, 143, 0.25)"},
				//points: { show: true },
				bars: { show: true, barWidth: 0.25, align:"center"	}
			},
			yaxis:{
				show: true,
				//color:["#AFE38F"],
				tickcolor:["#AFE38F"],
			    font: {color: "#AFE38F"},
			   // min: 0.0,
			    max: max + 1
				
			},
			legend: {
			    show: true,
			    colors: "#AFE38F",
			    labelFormatter: function(label, series) {
			    	return '<font color="#AFE38F">' + label + '</font>';
			    },
			    backgroundOpacity: 0.2,
			    position: "nw" // or "se" or "sw"
			},
			colors: ["#AFE38F"]
		};
	
	
	var densityDeg = {
			series: {
			label: "Cumulative distribution",
				lines: { show: true , fill: true, fillColor: "rgba(175, 227, 143, 0.25)"},
				//points: { show: true },
				bars: { show: true, barWidth: 0.25, align:"center"	}
			},
			yaxis:{
				show: true,
				//color:["#AFE38F"],
				tickcolor:["#AFE38F"],
			    font: {color: "#AFE38F"},
			   // min: 0.0,
			 //   max: max + 1
				
			},
			legend: {
			    show: true,
			    colors: "#AFE38F",
			    labelFormatter: function(label, series) {
			    	return '<font color="#AFE38F">' + label + '</font>';
			    },
			    backgroundOpacity: 0.2,
			    position: "nw" // or "se" or "sw"
			},
			colors: ["#AFE38F"]
		};
	
	
	$.plot("#metrics #degreeDistribution", [distrib] , options); 
	
	$.plot("#metrics #densityPlotlayout", [denseDeg] , densityDeg); 
	
	
	elemPromNodes.trim();
	elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1));  // remove any last comma.				
	elemPromNodes.trim();
	elemPromNodes += ']}}'; 
	
	//console.log(elemPromNodes);
	
	var obj_JSONRes = JSON.parse(elemPromNodes);
	$( _draw_Network_Graph(obj_JSONRes, destGraph , "defaultArg") ); 
	
	
	strStats += "</table>";
	$('#metrics #Networkstatistics').empty().append( strStats); 	

	
}
	//####################################################
	//####################################################
	else if (destGraph=="#kcore")	{ // which tab is selected?? kcore? 
	//####################################################
//####################################################
	
		//Removes the child elements from the selected id idLegend element
		$('#idLegend').empty().append("<label  style=\"font-size:medium;\" >Legend<\/label>" ); 
		$('#accordionKCore').accordion( "option", "active", false);	
		$('#accordionKCore').empty();
	
	
		var kc = data.kcores; // we get the K core objects
		var edg = data.kcores.edges; // we get the K core objects
			
		var j =0; // simple counter to select the next colour
		var nbrNodes = 0;
		var kcoreSize = Array();
		var kcoreName = Array();
		var edgesCores = Array(Array());
		var nodesPerKCore = "";
		var strKCore = '{"elements":{"nodes":['; 
		var boolVar =0; // simple flag to distinguish between cores and edges
		var kcLength = Object.keys(kc).length;
		var lastCores=Array(Array());
		var lastCoreNames=Array(Array());
//####################################################
	
		console.log("Object received: " + v);
		
		
		$.each(kc, function(key, value) { // For each K-core object
			
			var tempArr = Array();
		    //console.log(key + "=========================");
			for(var prop in value){ // Loop to access each node element in the current core.
								
				if(value[prop].data.hasOwnProperty('name')){
					
						nbrNodes++;
						// console.log("prop: " + prop + " value: id " + value[prop].data["id"] + ", name " + value[prop].data["name"]);
						nodesPerKCore += value[prop].data["name"] + "<br>" ;
						// we identify the current node in the current core
						var strTemp = "{\"data\":{\"id\":\"" + value[prop].data["id"] +"\",\"name\":\""+ value[prop].data["name"] +"\"}}";				
						// we add some styles 
						var strTemp2 = "{\"data\":{\"id\":\"" + value[prop].data["id"] + "\",\"name\":\"" + value[prop].data["name"] + "\"}, \"css\": { \"background-color\": \"" + safeColors[kcLength-j] + "\" }} ";
				
						// we replace the initial node by adding the styling, node colour 
						txt_json = txt_json.replace(strTemp, strTemp2);		
							
						tempArr.push(value[prop].data["name"]);
						
						boolVar = 0;
						
					}else{
						boolVar = 1;
					}
				}	
			
			if(boolVar==0){
						
							kcoreSize.push(nbrNodes); // we add the number of nodes for each core.
		
							var percent = 100*nbrNodes/nodes_N;
							nbrNodes=0;
						strKCore +=
							"{\"data\":{\"id\":\"" + key + "\",\"name\":\"" + key +
							"\",\"weight\": " + (Math.floor(percent)).toString() +
							"}, \"css\": { \"background-color\": \"" + safeColors[kcLength-j] + 				
							"\" }},";	
							
							kcoreName.push(key); //"KCore" + j );
														
							
							// Should reverse the order to represent the most important cores.
							if (kcLength-4<=countLegend && countLegend<=kcLength) { // we just show the first  fourth Cores. 
								// Add the legend name and colour			
								$('#idLegend').append("<div class=\"legend" + j + "\" style=\"background-color:" + safeColors[kcLength-j] + 
										"; width: 50px; height: 20px;" + 
										"color: white; font-weight:bold; border: 3px solid black; " +
										" padding: 1px; margin: 1px; border-radius: 5px; \" >" + key + "<\/div>" );
								
								 
								lastCores.push(tempArr);
								lastCoreNames.push(key);
								
								// Add the accordion to the DOM, new Core
								$('#accordionKCore').append("<h3>"+ key +"<\/h3><div style=\"font-size:x-small;\">"+ nodesPerKCore +"<\/div>");
								$('#accordionKCore').accordion("refresh");
								nodesPerKCore="";
								
							}
							
							countLegend++;
							j++; // increment the j counter to select the next colour
							
							
					}

				});
		boolVar=0;
		strKCore.trim();
		strKCore = strKCore.substr(0,(strKCore.length-1)); // remove any last comma.
		strKCore.trim();
	
		// Need to identify the links between the cores and their number.

	
	
		
		if(kcoreName.length>1 && typeof(edg) !== "undefined"){
			
					strKCore = strKCore + '],'; // end of the data node part
					// Constructing the edges part composed of edges_E edges.
					strKCore = strKCore +  '"edges":[' ;
				 	var t=0;
			
			
					for(var j =0; j < edg.length; j++ ){
												
						strKCore.trim();	
						strKCore = strKCore + // the parsing is based on the space character. Therefore space are not allowed in the id and name definition.
						'{"data":{"source":"' +  edg[j].data.source  
						+ '","target":"' +   edg[j].data["target"]  
						+ '","intedg":' +  edg[j].data["int"] 
						+ '}},'	;	
						
					}
								
						strKCore.trim();
						strKCore = strKCore.substr(0,(strKCore.length-1));  // remove any last comma.				
		}		
		

			strKCore.trim();
			strKCore = strKCore + ']}}';  // end of the JSON text construction.
					
			//####################################################			
			if (!$.isEmptyObject(PromNodes) ){ // Prominent nodes available? are known?
								
				var k = Object.keys(PromNodes).length;
				var metric = {};
				var precision = 0.0;
				

				for (var t=1; t<lastCores.length; t++){ // loop scan each Core. // Fisrt 0 index element is empty
					
					for(var f = 0; f<lastCores[t].length;f++){ // scan loop inside the current Core
					//console.log("==============================================");
						for(var key in PromNodes) { // check our if any of our prominent node belongs to this Core.
							
							if(  lastCores[t][f]=== key  ) {
																
								precision = precision + 1.0;
																
							}
						}
					}
					
					metric[lastCoreNames[t]] = (100.*precision/k).toPrecision(4); // 
					precision = 0;
					
				}
				
				var strMetrics = "<font size='3'>" +
				"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
				nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
				edges_E + "</span> edges  <br>" +
				"Prominent nodes' distribution: ";
				
				for(var v in metric ){
					strMetrics += v + ":  <span style ='color:#AFE38F'>" + metric[v] + " %</span>,  ";
				}
				
				strMetrics = strMetrics.substr(0,(strMetrics.length-3)); 
				strMetrics += "</font>";
				
				$('#kcore #algoMetrics').empty().append( strMetrics); 	
			
			}
			else{
				$('#kcore #algoMetrics').empty().append("<font size='3'>" +
						"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
						nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
						edges_E + "</span> edges  <br> </font>" ); 
			}
			
			//####################################################		
	
		var obj_JSONRes = JSON.parse(txt_json);
		var kcore_JSONRes = JSON.parse(strKCore);

		
			if ($("#allNodes").is(":checked")){
				$( _draw_Network_Graph(obj_JSONRes, destGraph , "defaultArg") ); 
				//console.log("After String txt_json: "  + txt_json);
			}else{
				$( _draw_Network_Graph(kcore_JSONRes, destGraph , "defaultArg") ); 
				//console.log("After String txt_json: "  + strKCore);
			}	
			
	} 
//####################################################
	//####################################################
else if (destGraph=="#brandomwalk"){
	//####################################################
//####################################################
		
		$('#ListPromNodes #accordionNodes').accordion( "option", "active", false);	
		$('#ListPromNodes #accordionNodes').empty();
		var rbw = data.randBiasWalk; // we get the K core objects
		var edg = data.randBiasWalk.edges; // we get the K core objects
		var nodesProm = "Rank | Node | Score<br>";
		var rank = 0;
		var elemPromNodes = '{"elements":{"nodes":['; 
						

		var distrib = [];
		var orderNodes = [];
//#################################################################################
		$.each(rbw, function(key, value) { // 
			
			for(var prop in value){ // Loop to access each element in the current core.
								
				if(value[prop].data.hasOwnProperty('name')){
				
					distrib.push([rank, Number(value[prop].data["importance"]) ]);
					
					var id =  value[prop].data["id"] ;
					var name =  value[prop].data["name"] ;
					orderNodes.push(   [ id  , name ]   );
					
					rank++;
								
					var score = ((Number(value[prop].data["importance"])).toPrecision(5)).toString();
						nodesProm += rank + " | " + value[prop].data["name"] +" | " + score + "<br>" ;

						elemPromNodes += "{\"data\":{\"id\":\"" + value[prop].data["id"] + "\",\"name\":\"" + value[prop].data["name"] + "\"}, \"css\": { \"background-color\": \"" + "teal" + "\"}},";				
						
					}
				

				}	
						
				});
		

		if (!$.isEmptyObject(PromNodes) ){ // Prominent nodes available? are known?
			
			
			var strNodes = parseInt( $('#brandomwalk #spinnNbrNodes').spinner("value") ); 
			
			//***********************************************************
			metrics = metPromNodes( PromNodes, orderNodes , strNodes);
			//***********************************************************
			
			console.log("precision: " + metrics["precision"] + "%");
			console.log("recall: " + metrics["recall"]+ "%");
			console.log("fMeasure: " + metrics["fMeasure"]+ "%");
			
			$('#brandomwalk #algoMetrics').empty().append("<font size='3'>" +
					"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
					nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
					edges_E + "</span> edges  <br>" +
					"Algorithm Precision = <span style ='color:#AFE38F'>" + 
					metrics["fMeasure"] + " %</span>,  Recall = <span style ='color:#AFE38F'>" + 
					metrics["recall"] + " %</span>,  " +  
					"F_measure = <span style ='color:#AFE38F'>" + 
					metrics["fMeasure"] + " % </span> </font>" ); 	
		}
		else{
			$('#brandomwalk #algoMetrics').empty().append("<font size='3'>" +
					"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
					nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
					edges_E + "</span> edges  <br> </font>" ); 
		}
		
//#################################################################################	

			console.log(distrib);

				var options = {
						series: {
						//label: "Rank and Score Prominent vertices",
							lines: { show: true , fill: true, fillColor: "rgba(175, 227, 143, 0.25)"},
							//points: { show: true },
							bars: { show: true, barWidth: 0.25, align:"center"	}
						},
						colors: ["#AFE38F", "#3399FF", "#33FFFF"]
					};
		
			$.plot("#boxPlotlayout", [distrib] , options); 
	
//#################################################################################		
				
		// Add the accordion to the DOM, new Core
		$('#ListPromNodes #accordionNodes').append("<h3>"+ "Prominent Nodes" +"<\/h3><div style=\"font-size:x-small;\">"+ nodesProm +"<\/div>");
		$('#ListPromNodes #accordionNodes').accordion("refresh");
		nodesProm="";

		elemPromNodes.trim();
		elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1)); // remove any last comma.
		elemPromNodes.trim();		
		
		if( !(typeof edg === 'undefined') && (edg.length>=1) ){
			
			elemPromNodes = elemPromNodes + '],'; // end of the data node part */
					// Constructing the edges part composed of edges_E edges.
		elemPromNodes = elemPromNodes +  '"edges":[' ;
				 	var t=0;
						
					for(var j =0; j < edg.length; j++ ){
						
						//console.log( "Edges Cores: " + edg[j].data.source+ " " + edg[j].data["target"] + " " + edg[j].data["int"] );
						
						elemPromNodes.trim();	
						elemPromNodes = elemPromNodes + // the parsing is based on the space character. Therefore space are not allowed in the id and name definition.
						'{"data":{"source":"' +  edg[j].data.source  
						+ '","target":"' +   edg[j].data["target"]  
						+ '"}},'	;	
						
					}
								
					elemPromNodes.trim();
					elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1));  // remove any last comma.				
		}	
		

		elemPromNodes.trim();
		elemPromNodes = elemPromNodes + ']}}';  // end of the JSON text construction.
			
//################################################################################
		console.log(elemPromNodes);
		var obj_JSONRes = JSON.parse(elemPromNodes);
		$( _draw_Network_Graph(obj_JSONRes, destGraph , "defaultArg") ); 
		console.log("Object received: " + v);
		
				
	} 
//####################################################
//####################################################
else if (destGraph=="#procoreperif")
//####################################################
//####################################################
		{
				console.log("Object received: " + v);
				$('#CorePeriProfNodes #accordionNodes').accordion( "option", "active", false);
				$('#CorePeriProfNodes #accordionNodes').empty();		
				var pcp = data.procoreperif; // we get the Profiling Core-Periphery  objects
				var distrib = [];
				var distDeg = [];
				var comparing = [];
				var rank = 0;
				var orderNodes= [];
				var nodesProm = "Rank | Node | PCP | Deg<br>";
				var elemPromNodes = '{"elements":{"nodes":['; 
				//#################################################################################	
				$.each(pcp, function(key, value) { // 
					
					for(var prop in value){ // Loop to access each element in the current core.
										
						if(value[prop].data.hasOwnProperty('name')){
						
							distrib.push([rank, Number(value[prop].data["profCorePeriph"]) ]);
							distDeg.push([rank,Number(value[prop].data["strength"]) ]);
							comparing.push([Number(value[prop].data["strength"]) , Number(value[prop].data["profCorePeriph"])]);			
	
							var id =  value[prop].data["id"] ;
							var name =  value[prop].data["name"] ;
							orderNodes.push(   [ id  , name ]   );
													
							rank++;
										
							var score = ((Number(value[prop].data["profCorePeriph"])).toPrecision(4)).toString();
							var strength =  ((Number(value[prop].data["strength"]))).toString();
								nodesProm += rank + " | " + value[prop].data["name"] +" | " + score +  " | " + strength + "<br>" ;
		
							var colorConv = 255*(1-score);
								elemPromNodes += "{\"data\":{\"id\":\"" + value[prop].data["id"] 
								+ "\",\"name\":\"" + value[prop].data["name"] 
								+ "\"}, \"css\": { \"background-color\": \"" 
								+ "rgb(" + colorConv +",255," +  colorConv +")" + "\"}},";	
				
							}
						
		
						}	
								
						});
								
				if (!$.isEmptyObject(PromNodes) ){ // Prominent nodes available? are known?
					
					var strNodes = parseInt( $('#procoreperif #spinnNbrNodes').spinner("value") ); 
					// let's calculate some metrics related to this method
					metrics = metricsPromNodes( PromNodes, orderNodes , strNodes);
					
					console.log("precision: " + metrics["precision"] + "%");
					console.log("recall: " + metrics["recall"]+ "%");
					console.log("fMeasure: " + metrics["fMeasure"]+ "%");
					
									
					$('#procoreperif #algoMetrics').empty().append("<font size='3'>" +
					"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
					nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
					edges_E + "</span> edges  <br>" +
					"Algorithm Precision = <span style ='color:#AFE38F'>" + 
					metrics["fMeasure"] + " %</span>,  Recall = <span style ='color:#AFE38F'>" + 
					metrics["recall"] + " %</span>,  " +  
					"F_measure = <span style ='color:#AFE38F'>" + 
					metrics["fMeasure"] + " % </span> </font>" ); 		
					
				}
				else{
					$('#procoreperif #algoMetrics').empty().append("<font size='3'>" +
							"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
							nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
							edges_E + "</span> edges  <br> </font>" ); 
				}
				
				console.log(distrib);
		
				var optCPC = {
						series: {
						label: "Core-Periphery Profile",
							lines: { show: true , fill: true, fillColor: "rgba(175, 227, 143, 0.25)"},
							//points: { show: true },
							bars: { show: true, barWidth: 0.25, align:"center"	}
						},
						yaxis:{
							show: true,
							//color:["#AFE38F"],
							tickcolor:["#AFE38F"],
						    font: {color: "#AFE38F"},
						    min: 0.0,
						    max: 1.5
							
						},
						legend: {
						    show: true,
						    colors: "#AFE38F",
						    labelFormatter: function(label, series) {
						    	return '<font color="#AFE38F">' + label + '</font>';
						    },
						    backgroundOpacity: 0.2,
						    position: "nw" // or "se" or "sw"
						},
						colors: ["#AFE38F", "#3399FF", "#33FFFF"]
					};
				var optStrength = {
						series: {
						label: "Strength Degree",
							lines: { show: true , fill: true, fillColor: "rgba(175, 227, 143, 0.25)"},
							//points: { show: true },
							bars: { show: true, barWidth: 0.25, align:"center"	}
						},
						yaxis:{
							show: true,
							//color:["#3399FF"],
							tickcolor:["#3399FF"],
						    font: {color: "#3399FF"},
						   // min: 0.0,
						    //max: 1.5
							
						},
						legend: {
						    show: true,
						    labelFormatter: function(label, series) {
						    	return '<font color="#3399FF">' + label + '</font>';
						    },
						    backgroundOpacity: 0.2,
						    position: "nw" // or "se" or "sw"
						},
						colors: ["#3399FF"]
					};
		
				var optCompar = {
						series: {
						label: "Comparing      ", // k-core and core-periphery",
							//lines: { show: true , fill: true, fillColor: "rgba(175, 227, 143, 0.25)"},
							points: { show: true, fill: true, fillColor: "rgba(175, 227, 143, 0.25)"},
							//bars: { show: true, barWidth: 0.25, align:"center"	}
						},
						xaxis:{
							show: true,
							color:["#3399FF"],
							tickcolor:["#3399FF"],
							font: {color: "#3399FF"},		
						},
						yaxis:{
							show: true,
							color:["#AFE38F"],
							tickcolor:["#AFE38F"],
						    font: {color: "#AFE38F"},
						    min: 0.0,
						    max: 1.5
							
						},
						legend: {
						    show: true,
						    labelFormatter: function(label, series) {
						    	return '<font color="#33FFFF">' + label + '</font>';
						    }, 
						    backgroundOpacity: 0.2,
						    position: "nw" // or "se" or "sw"
						},
						colors: ["#33FFFF"]
					};
				
			$.plot("#PCP_Graph1", [distrib] , optCPC); 
			$.plot("#PCP_Graph2",   [comparing]  , optCompar); 
			$.plot("#PCP_Graph3",[distDeg], optStrength );
			
				
			console.log(nodesProm);
			
			
			var edg = data.procoreperif.edges; // we get the K core objects
			// Add the accordion to the DOM, new Core
			$('#CorePeriProfNodes #accordionNodes').append("<h3>"+ "Core-Periphery Profile" +"<\/h3><div style=\"font-size:x-small;\">"+ nodesProm +"<\/div>");
			$('#CorePeriProfNodes #accordionNodes').accordion("refresh");
			nodesProm="";

			elemPromNodes.trim();
			elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1)); // remove any last comma.
			elemPromNodes.trim();
		
			
			if( !(typeof edg === 'undefined') && (edg.length>=1) ){
				
				elemPromNodes = elemPromNodes + '],'; // end of the data node part */
						// Constructing the edges part composed of edges_E edges.
			elemPromNodes = elemPromNodes +  '"edges":[' ;
					 	var t=0;
							
						for(var j =0; j < edg.length; j++ ){
								
							elemPromNodes.trim();	
							elemPromNodes = elemPromNodes + // the parsing is based on the space character. Therefore space are not allowed in the id and name definition.
							'{"data":{"source":"' +  edg[j].data.source  
							+ '","target":"' +   edg[j].data["target"]  
							+ '"}},'	;	
							
						}
									
						elemPromNodes.trim();
						elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1));  // remove any last comma.				
			}	
			

			elemPromNodes.trim();
			elemPromNodes = elemPromNodes + ']}}';  // end of the JSON text construction.
				
	//################################################################################
			console.log(elemPromNodes);
			var obj_JSONRes = JSON.parse(elemPromNodes);
			$( _draw_Network_Graph(obj_JSONRes, destGraph , "defaultArg") ); 
				
		}

//####################################################
//####################################################
else if (destGraph=="#degreeProfile")
//####################################################
//####################################################
		{
				console.log("Object received: " + v);
				$('#degreeProfile #accordionNodes').accordion( "option", "active", false);							
				$('#degreeProfile #accordionNodes').empty();		
				var pcp = data.degreeProfile; // we get the Profiling Core-Periphery  objects
			//	var distrib = [];
			//	var distDeg = [];
			//	var comparing = [];
				var rank = 0;
				var orderNodes= [];
				var nodesProm = "Rank | Node |Clns | Degr<br>";
				var elemPromNodes = '{"elements":{"nodes":['; 
				//#################################################################################	
				$.each(pcp, function(key, value) { // 
					
					for(var prop in value){ // Loop to access each element in the current core.
										
						if(value[prop].data.hasOwnProperty('name')){
						
						//	distrib.push([rank, Number(value[prop].data["profCorePeriph"]) ]);
						//	distDeg.push([rank,Number(value[prop].data["strength"]) ]);
						//	comparing.push([Number(value[prop].data["strength"]) , Number(value[prop].data["profCorePeriph"])]);			
	
							var id =  value[prop].data["id"] ;
							var name =  value[prop].data["name"] ;
							orderNodes.push(   [ id  , name ]   );
													
							rank++;
										
							//var score = ((Number(value[prop].data["profCorePeriph"])).toPrecision(4)).toString();
							var strength =  ((Number(value[prop].data["strength"]))).toString();
							var closeness =  ((100*Number(value[prop].data["closeness"]))).toPrecision(4).toString();
								nodesProm += rank + " | " + value[prop].data["name"] + " | " + closeness  +" | " + strength + "<br>" ;
		
							//var colorConv = 255*(1-score);
								elemPromNodes += "{\"data\":{\"id\":\"" + value[prop].data["id"] 
								+ "\",\"name\":\"" + value[prop].data["name"] 
								//+ "\"}, \"css\": { \"background-color\": \"" 								
								+ "\"},\"css\":{\"background-color\": \"" + "teal" 
								+  "\"}},";	
				
							}
						
		
						}	
								
						});
								
				if (!$.isEmptyObject(PromNodes) ){ // Prominent nodes available? are known?
					
					var strNodes = parseInt( $('#degreeProfile #spinnNbrNodes').spinner("value") ); 
					// let's calculate some metrics related to this method
					metrics = metPromNodes( PromNodes, orderNodes , strNodes);
					
					console.log("precision: " + metrics["precision"] + "%");
					console.log("recall: " + metrics["recall"]+ "%");
					console.log("fMeasure: " + metrics["fMeasure"]+ "%");
					
									
					$('#degreeProfile #algoMetrics').empty().append("<font size='3'>" +
					"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
					nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
					edges_E + "</span> edges  <br>" +
					"Algorithm Precision = <span style ='color:#AFE38F'>" + 
					metrics["fMeasure"] + " %</span>,  Recall = <span style ='color:#AFE38F'>" + 
					metrics["recall"] + " %</span>,  " +  
					"F_measure = <span style ='color:#AFE38F'>" + 
					metrics["fMeasure"] + " % </span> </font>" ); 		
					
				}
				else{
					$('#degreeProfile #algoMetrics').empty().append("<font size='3'>" +
							"<strong>Network Graph G(V,E):  </strong> |V| = <span style ='color:#AFE38F'>" + 
							nodes_N + "</span> nodes,  |E| = <span style ='color:#AFE38F'>" +
							edges_E + "</span> edges  <br> </font>" ); 
				}
				
			//	console.log(distrib);
		


		

				
			console.log(nodesProm);
			
			
			var edg = data.degreeProfile.edges; // we get the K core objects
			// Add the accordion to the DOM, new Core
			$('#degreeProfile #accordionNodes').append("<h3>"+ "Degree Selection" +"<\/h3><div style=\"font-size:x-small;\" >"+ nodesProm +"<\/div>");
			$('#degreeProfile #accordionNodes').accordion("refresh");
			nodesProm="";

			elemPromNodes.trim();
			elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1)); // remove any last comma.
			elemPromNodes.trim();
		
			
			if( !(typeof edg === 'undefined') && (edg.length>=1) ){
				
				elemPromNodes = elemPromNodes + '],'; // end of the data node part */
						// Constructing the edges part composed of edges_E edges.
			elemPromNodes = elemPromNodes +  '"edges":[' ;
					 	var t=0;
							
						for(var j =0; j < edg.length; j++ ){
								
							elemPromNodes.trim();	
							elemPromNodes = elemPromNodes + // the parsing is based on the space character. Therefore space are not allowed in the id and name definition.
							'{"data":{"source":"' +  edg[j].data.source  
							+ '","target":"' +   edg[j].data["target"]  
							+ '"}},'	;	
							
						}
									
						elemPromNodes.trim();
						elemPromNodes = elemPromNodes.substr(0,(elemPromNodes.length-1));  // remove any last comma.				
			}	
			

			elemPromNodes.trim();
			elemPromNodes = elemPromNodes + ']}}';  // end of the JSON text construction.
				
	//################################################################################
			console.log(elemPromNodes);
			var obj_JSONRes = JSON.parse(elemPromNodes);
			$( _draw_Network_Graph(obj_JSONRes, destGraph , "defaultArg") ); 
				
		}
			
}

//###############################################################


function parserPajekFile(contents){
	
	edges = []; // empty the edges array
	nodes = {}; 
	PromNodes = {}; // empty 
	
	// Because how files are handled between OS, once the content is loaded we remove any \r invisible carriage return character.
	contents = contents.replace(/(\r)/gm,"");		
	
	// ^n Matches any string with n at the beginning of it
	var comment = /^#/; // comment in the input dataset.
	var promNode = /^@/; //^@knownProminentNodes/; 
	var boolPromNodes = false;
	
	// Next we split based on a \n new line character, which returns an array-like 
	var array = contents.toString().split("\n"); 
											
	// Loop through the array, line by line in the content of the file network graph
	for ( var i=0; i<= ((array.length)-1); i++){ // Surprisingly the array stores an additional element the length of the array at the end. Therefore we need to subtract 1.
	
		
		var strArr = ((array[i]).replace(/\t/g,'')).trim(); // remove any tab character 
		
		if(promNode.test( strArr )==true ){ // Does this file contain the known nodes?
			boolPromNodes=true; // Yes then set the bool flag to true
			}
		else if ( boolPromNodes){ // Does the following lines contain the important major prominent nodes?
		
			var splitData = ( strArr ).split(" ");
			//console.log("index: " + i + " " + (splitData[0]).trim());
			var proNode = (splitData[0]).trim();						
			if (proNode != "") PromNodes[proNode] = proNode;
			//console.log(PromNodes);				
			}	
		
		else if ( comment.test( strArr )!=true && strArr !="" ) // looking for a line that contain the string *Vertices. Better use regular expressions
			{
				var splitData = (strArr).split(" ");
				
				if(splitData.length>=2){ // at least two nodes connected with an edge
					//console.log("index: " + i + " " + splitData[0].trim());
					//console.log("index: " + i + " " + splitData[1].trim());
					var node1 = (splitData[0]).trim();
					var node2 = (splitData[1]).trim();
							
					nodes[node1] = node1;
					nodes[node2] = node2;
					
					edges.push([node1, node2]);
				}
			else{ //just one node
			
				console.log("index: " + i + " " + (splitData[0]).trim());
				var node1 = (splitData[0]).trim();						
				nodes[node1] = node1;

				}
				
		}		

	}

	nodes_N = Object.keys(nodes).length;
	edges_E = edges.length;
	
	if (nodes_N ===0)
		return "ByPass";
	
	var keysNodes = Object.keys(nodes);
	
	// Which tab/algorithm is active
	var active = $( "#tabs" ).tabs( "option", "active" );
	//alert($("#tabs ul>li a").eq(active).attr('href'));
	var destGraph = $("#tabs ul>li a").eq(active).attr('href');

	
	var txt_json= '{"elements":{'; 

	//########################################################################
	if (destGraph=="#metrics")	{ // which tab is selected?? kcore? 
//########################################################################
		txt_json += '"algorithm":"metrics" , "nodes":['; 	
			
	}
//########################################################################
	else if (destGraph=="#kcore")	{ // which tab is selected?? kcore? 
//########################################################################
			
				txt_json += '"algorithm":"kcore" , "nodes":['; 	
				
//########################################################################
		} else if (destGraph=="#brandomwalk"){  // biais random walk?
//########################################################################
	
			// 	Also the parameters of the algorithm are sent to the server, such as the number of iteations, the probability and alpha.  
			
			var strNbrIter = parseInt( $('#spinnNbrIt').spinner("value") ) ;
			var strNodes = parseInt( $('#spinnNbrNodes').spinner("value") ) ; 
			var strAlpha = Number( $('#spinnAlpha').spinner("value") ) ; 
			var strProb = Number( $('#spinnProb').spinner("value") ) ; 
			
			if (strNodes===0) { 
				return "ByPass";
				 // Not worth to perform any calculationas as the user want to retrieve 0 nodes.
			}
			else if(strNodes > nodes_N){
				return "Nodes>TotNodes";
			}
					
				txt_json += '"algorithm":"brandomwalk" , "numbIter":"' + strNbrIter +
				'", "numbNodes":"'+ strNodes +'", "prob":"'+ strProb +'", "alpha":"'+ strAlpha +'" , "nodes":['; 	
//########################################################################					
		} else if (destGraph=="#procoreperif")
//########################################################################
			{
			
				txt_json += '"algorithm":"procoreperif" , "nodes":['; 	
			
			}
//########################################################################
		else if (destGraph=="#degreeProfile")
//########################################################################
						{
						
							txt_json += '"algorithm":"degreeProfile" , "nodes":['; 	
						
						}
//########################################################################


for(var i=0; i < ((keysNodes.length)); i++){  	// idem a length key is stored at the end 	
	txt_json.trim();	
	txt_json = txt_json + 		
	'{"data":{"id":"' + keysNodes[i] +  
	 '","name":"' + nodes[keysNodes[i]] + '"}},'	;							
}	
txt_json.trim();
txt_json = txt_json.substr(0,(txt_json.length-1)); // remove any last comma.
txt_json.trim();


if(edges_E!=0) {
			txt_json = txt_json + '],'; // end of the data node part
			
			// Constructing the edges part composed of edges_E edges.
			 txt_json = txt_json +  '"edges":[' ;
						
			for(var j=0; j<= (edges_E -1); j++){ 		
				txt_json.trim();	
				txt_json = txt_json + // the parsing is based on the space character. Therefore space are not allowed in the id and name definition.
				'{"data":{"source":"' + (edges[j])[0] +   	
				 '","target":"' + (edges[j])[1] + '"}},'	;			
			} 
			txt_json.trim();
			txt_json = txt_json.substr(0,(txt_json.length-1));  // remove any last comma.
			txt_json.trim();
	}	
			
txt_json = txt_json + ']}}';  // end of the JSON text construction.
txt_json.trim();

//console.log(txt_json);

return txt_json;
}

//##########################################################################


function readTextNetworkFile(evt) {
	
	$( "#dialog #progress-label" ).text("");
    //Retrieve the first file from the FileList object
    var f = evt.target.files[0];

	// some checks before loading the file, such as file extension type, existence, ...
    if (!f) {
		alert("Failed to load files: " + f.name);
    }
	
	if (!f.type.match('text.*')) {
		alert(f.name + " is not a valid text file.");
	}
	else {
	
	var r = new FileReader();
            r.onload = (function (f) {
                return function (e) {
                    var contents = e.target.result;
					//***********************************************

                    contents.replace(/^\s+|\s+$/gm,'');
					
                    // is the file empty?
					if (contents ===""){
						
						alert("Please check your input file.\nIt seems that your network file is Empty." );
						return;
					}
                    
					var txt_json = parserPajekFile(contents);
					
					if (txt_json =="ByPass") 
						return;
					else if (txt_json =="Nodes>TotNodes"){
						alert("The number of prominent nodes you're trying to retrieve are higher than the total number of nodes!");
						return;
					}
					
				try{
					//  The following JavaScript function JSON.parse(text) converts the JSON text into a JavaScript object					
					var obj_JSON = JSON.parse(txt_json);
				} catch(err){
					alert("Please check your input file.\nError : " + err );
					return;
				}

				console.log(txt_json);	// print the JSON text object to the console.
//=========================================================================================
				$( "#dialog" ).dialog( "open" );
				
				 $( "#progressbar" ).progressbar({
					 value: false
					 });
				 progressbar = $( "#progressbar" ),
				 progressbarValue = progressbar.find( ".ui-progressbar-value" );
				 progressbar.progressbar( "option", { value: 50 });
					 
				 progressbarValue.css({
						 "background": '#AFE38F' // '#' + Math.floor( Math.random() * 16777215 ).toString( 16 )
						 });
					 
				 progressbar.progressbar( "option", "value", false );	
				 $( "#dialog #progress-label" ).text( "Please Wait, Your data have been sent to the server... The server is processing your request..." );

//=========================================================================================				
				 
$.ajax({ 
		type:"POST", 
		datatype: "JSON",
		url:"controller_Servlet", 
		data: {
			loadGraph:1,
			obj_JSON:JSON.stringify(obj_JSON) 			
		},
		success: function(data){ 
			//alert("About to display the Network Graph");
			//console.log("About to display the Network Graph");
			plot_nodes_graph(data, txt_json);
			
			$("#dialog").dialog( "close" );
			
			
			
		},
		
		error: function(data){
			
				alert("Error, fail: " + data);
				$("#dialog").dialog( "close" );
			
		}	
	
});

//==========================================================================================

                };
            })(f);
            r.readAsText(f);
    }
}


document.getElementById('fileinput').addEventListener('change', readTextNetworkFile, false);
document.getElementById('fileinput1').addEventListener('change', readTextNetworkFile, false);
document.getElementById('fileinput2').addEventListener('change', readTextNetworkFile, false);
document.getElementById('fileinput3').addEventListener('change', readTextNetworkFile, false);
document.getElementById('fileinput4').addEventListener('change', readTextNetworkFile, false);




