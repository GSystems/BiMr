/**
 * 
 */

var map;
var heatmap;
var sliderDiv = document.getElementById("slider");


function initMap(){

    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 6,
        center: {lat: 37.775, lng: -122.434},
        mapTypeId: 'terrain'
      });

	var script = document.createElement('script');
	script.src = "WEB-INF/resources/javascript/geo.js";
	document.getElementsByTagName('head')[0].appendChild(script); 

    map.data.setStyle(function(feature) {
        var nrBirds = feature.getProperty('nrBirds'); 
        return {
          icon: getCircle(nrBirds)
        };
    });
    
	
}

sliderDiv.onchange = function(){
	switch(this.value){
		case '1':
			console.log(this.value);
			clearCensusData();
			getJSON('WEB-INF/resources/geoJSON/dataBirds1.json', function(err, data) {
				if (err !== null)
					alert('Something went wrong: ' + err);
				else {
					console.log(data);
					map.data.addGeoJson(data);
				}
			});
			break;
		case '2':
			console.log(this.value);
			clearCensusData();
			getJSON('WEB-INF/resources/geoJSON/dataBirds.json', function(err, data) {
				if (err !== null)
					alert('Something went wrong: ' + err);
				else {
					console.log(data);
					map.data.addGeoJson(data);
				}
			});
			break;
		default:
			break;
	}

}

function getJSON(url, callback){
    var xhr = new XMLHttpRequest();
    xhr.open('GET',"http://localhost:8080/BiMr/"+ url, true);
    console.log("http://localhost:8080/BiMr/" + url);
    xhr.responseType = 'json';
    xhr.onload = function() {
      var status = xhr.status;
      if (status === 200) {
        callback(null, xhr.response);
      } else {
        callback(status, xhr.response);
      }
    };
    xhr.send();
}


function clearCensusData() {
	map.data.forEach(function(feature) {
	    map.data.remove(feature);
	});
  }

function getCircle(magnitude) {
    return {
        path: google.maps.SymbolPath.CIRCLE,
        fillColor: 'red',
        fillOpacity: .9,
        scale: Math.pow(2, magnitude),
        strokeColor: 'red',
        strokeWeight: .5
    };
}

function eqfeed_callback(results) {
	console.log("Loading Data");
   	map.data.addGeoJson(results);
}

function getData(){
	
	var birds = [
		{
		 coord:{lat: 36.0369429, lng: -115.0544178},
		 name: 'American Tree Sparrow'
		},
	    {
	     coord:{lat: 39.500583,  lng: -119.80673},
		 name: 'American Tree Sparrow'
		},
	    {coord:{lat: 39.4908565, lng: -119.7410685},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 36.3249025, lng: -115.266594},
	  	 name: 'American Tree Sparrow'},
	    {coord:{lat: 36.0877316, lng: -114.9838436},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 39.5383369, lng: -119.8350906},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 36.0749791, lng: -115.0021416},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 39.3871964, lng: -119.8268473},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 36.0749791, lng: -115.0021416},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 36.438797,  lng: -115.359566},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 36.3249025, lng: -115.266594},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 39.5348915, lng: -119.7295699},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 39.5375943, lng: -119.8156735},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 39.5348915, lng: -119.7295699},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 39.5208387, lng: 145.137978},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 37.819616,  lng: -119.831905},
	 	 name: 'American Tree Sparrow'},
	    {coord:{lat: 39.500583,  lng: -119.80673},
	     name: 'American Tree Sparrow'},
	    {coord:{lat: 39.4908565, lng: -119.7410685},
	     name: 'American Tree Sparrow'},
	    {coord:{lat: 36.0749791, lng: -115.0021416},
	     name: 'American Tree Sparrow'},
	    {coord:{lat: 39.5470076, lng: -119.8303968},
	     name:'American Tree Sparrow'},
	    {coord:{lat: 36.1006947, lng: -115.022462},
	     name: 'American Tree Sparrow'},
	    {coord:{lat: 36.0749791, lng: -115.0021416},
	     name: 'American Tree Sparrow'},
	    {coord:{lat: 39.5348915, lng: -119.7295699},
	     name: 'American Tree Sparrow'}
	];
	
	return birds;
}
