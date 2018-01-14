var map;
var url = "http://localhost/BiMr/WEB-INF/resources/locations/";
var allInfoWindows = [];

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 2,
        center: {lat: -33.865427, lng: 151.196123},
        mapTypeId: 'terrain'
    });

    initData(1);
}

function getCircle(magnitude) {
    return {
        path: google.maps.SymbolPath.CIRCLE,
        fillColor: 'red',
        fillOpacity: .9,
        scale: Math.pow(2, magnitude) / 4,
        strokeColor: 'red',
        strokeWeight: .5
    };
}

function slideStart(){
    var value = document.getElementById("slider").value;
    clearWindowInfo();
    clearMap();
    initData(value);
}

function initAction(){
    var currentValue = document.getElementById("slider").value;
    if(currentValue != 1){
        clearMap();
        document.getElementById("slider").value = 1;
    }
}

function loadJson(path,callback){
    var xobj = new XMLHttpRequest();
    xobj.overrideMimeType("application/json");
    xobj.open('GET', path, true);
    xobj.onreadystatechange = function () {
        if(xobj.readyState == 4 && xobj.status == "200"){
            callback(xobj.responseText);
        }
    };
    xobj.send(null);
}

function clearMap(){
    map.data.forEach(function(feature){
        map.data.remove(feature);
    })
}

function clearWindowInfo(){
    allInfoWindows.forEach(function(element){
        element.close();
    })
}

function initWindowInfo(){

    var contentString = '<div id="content">'+
    '<div id="siteNotice">'+
    '</div>'+
    '<h1 id="firstHeading" class="firstHeading">Uluru</h1>'+
    '<div id="bodyContent">'+
    '<p><b>Uluru</b>, also referred to as <b>Ayers Rock</b>, is a large ' +
    'sandstone rock formation in the southern part of the '+
    'Northern Territory, central Australia. It lies 335&#160;km (208&#160;mi) '+
    'south west of the nearest large town, Alice Springs; 450&#160;km '+
    '(280&#160;mi) by road. Kata Tjuta and Uluru are the two major '+
    'features of the Uluru - Kata Tjuta National Park. Uluru is '+
    'sacred to the Pitjantjatjara and Yankunytjatjara, the '+
    'Aboriginal people of the area. It has many springs, waterholes, '+
    'rock caves and ancient paintings. Uluru is listed as a World '+
    'Heritage Site.</p>'+
    '<p>Attribution: Uluru, <a href="https://en.wikipedia.org/w/index.php?title=Uluru&oldid=297882194">'+
    'https://en.wikipedia.org/w/index.php?title=Uluru</a> '+
    '(last visited June 22, 2009).</p>'+
    '</div>'+
    '</div>';

    var infowindow = new google.maps.InfoWindow();
    allInfoWindows.push(infowindow);

    map.data.addListener('click', function(event){
        if(infowindow)
            infowindow.close();
        var content = contentString;
        infowindow.setContent(content);
        infowindow.setPosition(event.feature.getGeometry().get());
        infowindow.setOptions({ pixelOffset: new google.maps.Size(0, -20)});
        infowindow.open(map);

    })

    google.maps.event.addListener(infowindow,'closeclick',function(){
        console.log("closeclick event");
        clearWindowInfo();
    });
}

function initData(value)
{
    loadJson(url + "data" + value + ".json", function(data){
        if(data !== null) {
            map.data.addGeoJson(JSON.parse(data))
        }
        else
            console.log("Error");
    });

    map.data.setStyle(function(feature) {
        var magnitude = feature.getProperty('number');
        var iconProperty = feature.getProperty('icon');
        if(iconProperty == undefined){
            return {
                icon: getCircle(magnitude)
            };
        } else {
            return {
                icon: feature.getProperty('icon')
            };
        }
    });

    initWindowInfo();

}

function startAction(){
    value = document.getElementById("slider").value;
    console.log("iteration:" + value);
    if(value >= 11)
        return;
    clearMap();
    clearWindowInfo();
    initData(value);
    document.getElementById("slider").value++;
    console.log("new value:" + document.getElementById("slider").value);
}

document.getElementById("button").addEventListener("click", function(){
    initAction();
    var timesRun = 0;
    var intervalId = setInterval(function(){
        timesRun++;
        if(timesRun === 10)
            clearInterval(intervalId);
        startAction();
    } , 1000);
});


