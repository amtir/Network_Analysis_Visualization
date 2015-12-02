/**
 * 
 */

$(function() {
	
	$("#tabs").tabs();
	
	$( document ).tooltip();

	// ##################################################################
	
	
	 $('#metrics #spinner').spinner({
	 	 	value: 4000,
	         min: 2000,
	         max: 8000,
	         step: 10,           
	         spin: function( event, ui ) {
	         	$( "#slider" ).slider( "value", ui.value  );        	       	
	         }
	     });
		 
			$( "#metrics #laySelect" ).selectmenu({
				change: function( event, ui ) {
					
					if($( "#metrics #laySelect" ).val()=="Dynamic"){
						
						$( "#metrics #spinner" ).spinner("enable");
						$( "#metrics #slider" ).slider("enable");
						
					}else{
						$( "#metrics #spinner" ).spinner("disable");
						$( "#metrics #slider" ).slider("disable");
					}
					
				}
				});
			
			$( "#metrics  #slider" ).slider({
				range: "min",
				value: 4000,
				min: 2000,
				max: 8000,
				slide: function( event, ui ) {
					$('#metrics  #spinner').spinner("value", ui.value);
				}
			});
			
			
			$( "#dialog" ).dialog({
				autoOpen: false,
				modal: true,
				show: {
				effect: "blind",
				duration: 1000
				},
				hide: {
				effect: "explode",
				duration: 1000
				}
			});
			
			$( "#dialog" ).dialog({
				autoOpen: false,
				modal: true,
				show: {
				effect: "blind",
				duration: 800
				},
				hide: {
				effect: "explode",
				duration: 1000
				}
			});
	
	// ##################################################################
	
	 $('#kcore #spinner').spinner({
 	 	value: 4000,
         min: 2000,
         max: 8000,
         step: 10,           
         spin: function( event, ui ) {
         	$( "#slider" ).slider( "value", ui.value  );        	       	
         }
     });
	 
		$( "#kcore #laySelect" ).selectmenu({
			change: function( event, ui ) {
				
				if($( "#kcore #laySelect" ).val()=="Dynamic"){
					
					$( "#kcore #spinner" ).spinner("enable");
					$( "#kcore #slider" ).slider("enable");
					
				}else{
					$( "#kcore #spinner" ).spinner("disable");
					$( "#kcore #slider" ).slider("disable");
				}
				
			}
			});
		
		$( "#kcore  #slider" ).slider({
			range: "min",
			value: 4000,
			min: 2000,
			max: 8000,
			slide: function( event, ui ) {
				$('#kcore  #spinner').spinner("value", ui.value);
			}
		});
		
		
		$( "#kcore #radio" ).buttonset();
		
		$( "#kcore #accordionKCore" ).accordion({
			collapsible: true,
			active: false,
			heightStyle: "fill"
		});
// ##################################################################
		
	 $('#brandomwalk #spinner').spinner({
	 	 	value: 4000,
	         min: 2000,
	         max: 8000,
	         step: 10,           
	         spin: function( event, ui ) {
	         	$( "#brandomwalk #slider" ).slider( "value", ui.value  );        	       	
	         }
	     });
	 
		$( "#brandomwalk #slider" ).slider({
			range: "min",
			value: 4000,
			min: 2000,
			max: 8000,
			slide: function( event, ui ) {
				$('#brandomwalk #spinner').spinner("value", ui.value);
			}
		});

		
	 $('#brandomwalk #spinnProb').spinner({
	 	 	value: 0.15,
	         min: 0.05,
	         max: 0.95,
	         step: 0.05,           
	         spin: function( event, ui ) {
	         	      	       	
	         }
	     });
	
	 $('#brandomwalk #spinnAlpha').spinner({
	 	 	value: 0.5,
	         min: 0.05,
	         max: 0.95,
	         step: 0.05,           
	         spin: function( event, ui ) {
	         	      	       	
	         }
	     });
	

	 $('#brandomwalk #spinnNbrIt').spinner({
	 	 	value: 100000,
	         min: 100,
	         max: 10000000000,
	         step: 10,           
	         spin: function( event, ui ) {
	         	      	       	
	         }
	     });
	 

	 $('#brandomwalk #spinnNbrNodes').spinner({
	 	 	value: 10,
	         min: 1,
	         max: 100,
	         step: 1,           
	         spin: function( event, ui ) {
	         	      	       	
	         }
	     });
	 
			
	$( "#brandomwalk #laySelect" ).selectmenu({
		change: function( event, ui ) {
			
			if($( "#brandomwalk #laySelect" ).val()=="Dynamic"){
				
				$( "#brandomwalk #spinner" ).spinner("enable");
				$( "#brandomwalk #slider" ).slider("enable");
				
			}else{
				$( "#brandomwalk #spinner" ).spinner("disable");
				$( "#brandomwalk #slider" ).slider("disable");
			}
			
		}
		});
	$( "#brandomwalk #ListPromNodes #accordionNodes" ).accordion({
		collapsible: true,
		active: false,
		heightStyle: "fill"
	});
// ##################################################################
	
	 $('#procoreperif #spinner').spinner({
	 	 	value: 4000,
	         min: 2000,
	         max: 8000,
	         step: 10,           
	         spin: function( event, ui ) {
	         	$( "#procoreperif #slider" ).slider( "value", ui.value  );        	       	
	         }
	     });
	 
		$( "#procoreperif #slider" ).slider({
			range: "min",
			value: 4000,
			min: 2000,
			max: 8000,
			slide: function( event, ui ) {
				$('#procoreperif #spinner').spinner("value", ui.value);
			}
		});
		
		$( "#procoreperif #laySelect" ).selectmenu({
			change: function( event, ui ) {
				
				if($( "#procoreperif #laySelect" ).val()=="Dynamic"){
					
					$( "#procoreperif #spinner" ).spinner("enable");
					$( "#procoreperif #slider" ).slider("enable");
					
				}else{
					$( "#procoreperif #spinner" ).spinner("disable");
					$( "#procoreperif #slider" ).slider("disable");
				}
				
			}
			});
		
		
		 $('#procoreperif #spinnNbrNodes').spinner({
		 	 	value: 10,
		         min: 1,
		         max: 100,
		         step: 1,           
		         spin: function( event, ui ) {
		         	      	       	
		         }
		     });
		
		$('#procoreperif #CorePeriProfNodes #accordionNodes').accordion({
			collapsible: true,
			active: false,
			heightStyle: "fill"
		});
		
	// ##################################################################
		
		 $('#degreeProfile #spinner').spinner({
		 	 	value: 4000,
		         min: 2000,
		         max: 8000,
		         step: 10,           
		         spin: function( event, ui ) {
		         	$( "#degreeProfile #slider" ).slider( "value", ui.value  );        	       	
		         }
		     });
		 
			$( "#degreeProfile #slider" ).slider({
				range: "min",
				value: 4000,
				min: 2000,
				max: 8000,
				slide: function( event, ui ) {
					$('#degreeProfile #spinner').spinner("value", ui.value);
				}
			});
			
			$( "#degreeProfile #laySelect" ).selectmenu({
				change: function( event, ui ) {
					
					if($( "#degreeProfile #laySelect" ).val()=="Dynamic"){
						
						$( "#degreeProfile #spinner" ).spinner("enable");
						$( "#degreeProfile #slider" ).slider("enable");
						
					}else{
						$( "#degreeProfile #spinner" ).spinner("disable");
						$( "#degreeProfile #slider" ).slider("disable");
					}
					
				}
				});
			
			
			 $('#degreeProfile #spinnNbrNodes').spinner({
			 	 	value: 10,
			         min: 1,
			         max: 100,
			         step: 1,           
			         spin: function( event, ui ) {
			         	      	       	
			         }
			     });
			
			$('#degreeProfile #degNodes #accordionNodes').accordion({
				collapsible: true,
				active: false,
				heightStyle: "fill"
			});
		
		

});