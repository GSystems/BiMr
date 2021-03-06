var map;
var url = "http://localhost/WEB-INF/resources/locations/";
var javaUrl = "http://localhost:8080/BiMr/ws/bimr/getMigrations/";
var allInfoWindows = [];

var contentTitleStart = '<div id="content">'+
    '<div id="siteNotice">'+
    '</div>'+
    '<h1 id="firstHeading" class="firstHeading">';
var contentTitleEnd = '</h1>'+
    '<div id="bodyContent">'+
    '<p><b><h3>';
var contentUserEnd = " via Twitter</h3></b>";
var contentTweetEnd = ".</p>" +
    '</div>'+
    '</div>';
var contetnTweetend = '';

var allDays = []
var allHotspots;
var currentHotspots;
var currentDate;
var migrationHotspots;
var skip = false;

function sortByKey(a,b)
{
    let keyA = new Date(a.Date);
    let keyB = new Date(b.Date);
    // Compare the 2 dates
    if(keyA < keyB) return -1;
    if(keyA > keyB) return 1;
    return 0;
}

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 2,
        center: {lat: -33.865427, lng: 151.196123},
        mapTypeId: 'terrain'
    });


    initData(1);
    getMigrationStartData();
}

function initData(value,timestamp = null)
{ 
    //var urlCos = javaUrl + document.getElementById('startDate').value + "/" + document.getElementById('endDate').value;

/*    takeJson("http://localhost:8080/BiMr/ws/bimr/getAllHotspots",function(data){
        if(data !== null){
            allHotspots = JSON.parse(data);
        } 
        else 
            console.log("Error");
    });*/


    loadJson(url + "data" + value + ".json", function(data){
        if(data !== null) {  
            allHotspots = JSON.parse(data);
        }
        else
            console.log("Error");
    });
}

function getMigrationStartData()
{

/*    takeJson("http://localhost:8080/BiMr/ws/bimr/getAllMigrations", function(data){
        if(data !== null){
            migrationHotspots = JSON.parse(data);
            let date = findEarliesDate(JSON.parse(data),'migration');
            currentDate = date;
            date = formatDate(date);
            date = getTime(date);

            currentHotspots = getHotspotstsByTime(allHotspots,date,"hotspot");
            map.data.addGeoJson(currentHotspots);
        } 
        else 
            console.log("Error");
    });*/

    loadJson(url + "data" + 2 + ".json", function(data){
        if(data !== null) {

            migrationHotspots = JSON.parse(data);
            let date = findEarliesDate(JSON.parse(data),'migration');
            currentDate = date;
            date = formatDate(date);
            date = getTime(date);

            currentHotspots = getHotspotstsByTime(allHotspots,date,"hotspot");
            map.data.addGeoJson(currentHotspots);
        }
        else
            console.log("Error");
    });

    map.data.setStyle(function(feature) {
        var number = feature.getProperty('howMany');
        if(number == undefined)
            number = 1;
        return {
            icon: getCircle(number)
        };
    });

    initWindowInfo();
}

function getMigrationSelectStart(timestamp)
{
    loadJson(url + "data" + 2 + ".json", function(data){
        if(data !== null) {

            migrationHotspots = JSON.parse(data);
            
            let date = new Date(timestamp);
            currentDate = date;
            date = formatDate(date);
            date = getTime(date);

            currentHotspots = getHotspotstsByTime(allHotspots,date,"hotspot");
            map.data.addGeoJson(currentHotspots);
        }
        else
            console.log("Error");
    });
}

function showAllHotSpots()
{
    clearWindowInfo();
    clearMap();
    map.data.addGeoJson(allHotspots);
}

function getMigrations()
{

/*    takeJson("http://localhost:8080/BiMr/ws/bimr/getAllMigrations", function(data){
        if(data !== null){
            currentDate = findNextDate('migration');
            let date = formatDate(currentDate);
            date = getTime(date);


            console.log(currentDate);
            findNextStep(date);
            console.log(currentHotspots.features.length);

            if(!skip)
            {
                map.data.addGeoJson(currentHotspots);
            }
        } 
        else 
            console.log("Error");
    });*/

    loadJson(url + "data" + 2 + ".json", function(data){
        if(data !== null) {

            currentDate = findNextDate('migration');
            let date = formatDate(currentDate);
            date = getTime(date);


            console.log(currentDate);
            findNextStep(date);
            console.log(currentHotspots.features.length);

            if(!skip)
            {
                map.data.addGeoJson(currentHotspots);
            }

        }
        else
            console.log("Error");
    });

    map.data.setStyle(function(feature) {
        var number = feature.getProperty('howMany');
        let trail  = feature.getProperty('trail');
        if(number == undefined)
            number = 1;
        return {
            icon: getCircle(number,trail)
        };
    });

    initWindowInfo(); 
}

function getCircle(magnitude,trail) {
    let opacity = .8;
    if(trail == true)
        opacity = .3;
    var quan = 15;
    if(magnitude < 4 ){
        return {
            path: google.maps.SymbolPath.CIRCLE,
            fillColor: 'red',
            fillOpacity: opacity,
            scale: quan,
            strokeColor: 'red',
            strokeWeight: .5
        };
    } else {
        return {
            path: google.maps.SymbolPath.CIRCLE,
            fillColor: 'red',
            fillOpacity: opacity,
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
    xhttp.open("GET", path);
    //xhttp.open("GET", "https://ebird.org/ws1.1/data/obs/geo/recent?lng=-76.51&lat=42.46&dist=2&back=5&maxResults=500&locale=en_US&fmt=json", true);
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
        let speciesList = ""; 

        for(let i = 0 ;i<event.feature.getProperty('birdSpeciesList').length; i++){
            speciesList += event.feature.getProperty('birdSpeciesList')[i];
            speciesList += ",";
        }
        speciesList = speciesList.slice(0, -1);

        var content =  contentTitleStart +
            speciesList +
            contentTitleEnd+
            event.feature.getProperty('user').name+
            contentUserEnd+
            event.feature.getProperty('tweetMessage')+
            contentTweetEnd + 
            "<br> Spotting Date: " + event.feature.getProperty('observationDate').year + "-"
            + event.feature.getProperty('observationDate').monthValue + "-" +
            + event.feature.getProperty('observationDate').dayOfMonth +
            "<br>Number of birds estimated:" + event.feature.getProperty('howMany');

        infowindow.setContent(content);
        infowindow.setPosition(event.feature.getGeometry().get());
        infowindow.setOptions({ pixelOffset: new google.maps.Size(0, -20)});
        infowindow.open(map);

    })

    google.maps.event.addListener(infowindow,'closeclick',function(){
        clearWindowInfo();
    });
}



function startAction(){
    //clearMap();
    clearWindowInfo();

    getMigrations();

    //initData(value);
    //document.getElementById("myRange").value++;
}

document.getElementById("button").addEventListener("click", function(){
    initAction();
    var timesRun = 0;
    var intervalId = setInterval(function(){
        console.log("iteration: " + timesRun);
        timesRun++;
        if(timesRun === 19)
            clearInterval(intervalId);
        startAction();
    } , 500);
});

document.getElementById('buttonSelect').addEventListener("click", function(){
    initAction();
    let start = document.getElementById('startDate').value;
    let div = document.getElementById('censusMin');
    div.innerHTML = start;

    let end = document.getElementById('endDate').value;
    let divMax = document.getElementById('censusMax');
    divMax.innerHTML = end;

    clearWindowInfo();
    clearMap();
    initData(1);
    getMigrationSelectStart(start);

/*    allDays = [];

    takeJson("http://localhost:8080/BiMr/ws/bimr/getMigrationsByDate/"+ start+"/" + end, function(data){
        if(data !== null){
            migrationHotspots = JSON.parse(data);
            let date = findEarliesDate(JSON.parse(data),'migration');
            currentDate = date;
            date = formatDate(date);
            date = getTime(date);

            currentHotspots = getHotspotstsByTime(allHotspots,date,"hotspot");
            map.data.addGeoJson(currentHotspots);
        } 
        else 
            console.log("Error");
    });*/
})

document.addEventListener('DOMContentLoaded', function() {
    let start = document.getElementById('startDate').value;
    let divMin = document.getElementById('censusMin');
    divMin.innerHTML = start;

    let end = document.getElementById('endDate').value;
    let divMax = document.getElementById('censusMax');
    divMax.innerHTML = end;

}, false);


function getTime(date)
{
    let timestamp = new Object(); 
    let options = date.split("-");

    timestamp.Year = options[0];
    timestamp.Month = options[1];
    timestamp.Day = options[2];

    return timestamp;
}

function changeDataType()
{
    let value = document.getElementById('dataType').value;
    if(value == 2) {
        showAllHotSpots();
    } else {
        clearWindowInfo();
        clearMap();
        getMigrationStartData();   
    }
}
