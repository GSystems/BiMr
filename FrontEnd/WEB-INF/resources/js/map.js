var map;
var url = "http://localhost/WEB-INF/resources/locations/";
var javaUrl = "http://localhost/BiMr/";
var allInfoWindows = [];

var startDate = new Date(2017,12,12,0,0,0,0);
var endDate   = new Date(2018,1,12,0,0,0,0);


var contentTitleStart = '<div id="content">'+
    '<div id="siteNotice">'+
    '</div>'+
    '<h1 id="firstHeading" class="firstHeading">';

var contentTitleEnd = '</h1>'+
    '<div id="bodyContent">'+
    '<p>From <b>';

var contentUserEnd = "</b>: ";

var contentTweetEnd = "</p>" +
    '</div>'+
    '</div>';

var contetnTweetend = '';


function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 2,
        center: {lat: -33.865427, lng: 151.196123},
        mapTypeId: 'terrain'
    });

    initData(1);
}

function getCircle(magnitude) {
    var quan = 15;
    if(magnitude < 4 ){
        return {
            path: google.maps.SymbolPath.CIRCLE,
            fillColor: 'red',
            fillOpacity: .8,
            scale: quan,
            strokeColor: 'red',
            strokeWeight: .5
        };
    } else {
        return {
            path: google.maps.SymbolPath.CIRCLE,
            fillColor: 'red',
            fillOpacity: .9,
            scale: Math.pow(2, magnitude) / 2 + 1,
            strokeColor: 'red',
            strokeWeight: .5
        };    
    }

    
}

function slideStart(){
    var value = document.getElementById("myRange").value;
    clearWindowInfo();
    clearMap();
    initData(value);
}

function initAction(){
    var currentValue = document.getElementById("myRange").value;
    if(currentValue != 1){
        clearMap();
        document.getElementById("myRange").value = 1;
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


function takeJson(path,callback)
{
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "https://ebird.org/ws1.1/data/obs/geo/recent?lng=-76.51&lat=42.46&dist=2&back=5&maxResults=500&locale=en_US&fmt=json", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.onreadystatechange = function()
    {
        if(this.readyState == 4 && this.status == "200"){
            callback(this.responseText);
        }
    };
    xhttp.send(null);
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

    var infowindow = new google.maps.InfoWindow();
    allInfoWindows.push(infowindow);

    map.data.addListener('click', function(event){
        if(infowindow)
            infowindow.close();

        var content =  contentTitleStart +
            event.feature.getProperty('commonName') +
            contentTitleEnd+
            event.feature.getProperty('user')+
            contentUserEnd+
            event.feature.getProperty('information')+
            contentTweetEnd + 
            "<br>Number of birds estimated:" + event.feature.getProperty('number');

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
/*    var dateTime = document.getElementById('startDate').value;
    var d = new Date(2017,12,12,0,0,0,0);
    d.setDate(d.getDate() + 1);
    var res = d.toISOString().substring(0,10);
    console.log(res);*/

/*    takeJson('test',function(data){
        if(data !== null){
            console.log(data);
            map.data.addGeoJson(JSON.parse(data));
        } 
        else 
            console.log("Error");
    });*/

    loadJson(url + "data" + value + ".json", function(data){
        if(data !== null) {
            map.data.addGeoJson(JSON.parse(data))
        }
        else
            console.log("Error");
    });

    map.data.setStyle(function(feature) {
        var number = feature.getProperty('number');
        var iconProperty = feature.getProperty('icon');
        if(iconProperty == undefined){
            return {
                icon: getCircle(number)
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
    value = document.getElementById("myRange").value;
    console.log("iteration:" + value);
    if(value >= 11)
        return;
    clearMap();
    clearWindowInfo();
    initData(value);
    document.getElementById("myRange").value++;
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

document.getElementById('buttonSelect').addEventListener("click", function(){
    initAction();

    var start = document.getElementById('startDate').value;
    var div = document.getElementById('censusMin');
    div.innerHTML = start;

    var dateTime = document.getElementById('startDate').value;
    clearWindowInfo();
    initData(1);
})

function changeDataType()
{
    var select = document.getElementById('dataType');
    initAction();
    clearWindowInfo();
    initData(9);
    console.log(select.value);
}

document.addEventListener('DOMContentLoaded', function() {
    var start = document.getElementById('startDate').value;
    var div = document.getElementById('censusMin');
    div.innerHTML = start;
}, false);

