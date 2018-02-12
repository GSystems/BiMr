var data;
var url = "http://localhost/WEB-INF/resources/locations/";

document.addEventListener('DOMContentLoaded', function() {
    CreateTableFromJSON();
}, false);

function CreateTableFromJSON() {
    console.log('test');
    var myBooks = [
		{
        "bird_species": "Eagle",
        "count": "7"
      } ,
      {
        "bird_species": "Killdeer",
        "count": "7"
      } ,
      {
        "bird_species": "Osprey",
        "count": "7"
      } ,
      {
        "bird_species": "hawk",
        "count": "3"
      } ,
      {
        "bird_species": "Killdeers",
        "count": "2"
      } ,
      {
        "bird_species": "Eagles",
        "count": "1"
      } ,
      {
        "bird_species": "Friday",
        "count": "1"
      } ,
      {
        "bird_species": "Tricolored",
        "count": "1"
      } ,
      {
        "bird_species": "Tufted",
        "count": "1"
      } ,
      {
        "bird_species": "Urbanized",
        "count": "1"
      } ,
      {
        "bird_species": "White-throated",
        "count": "1"
      } ,
      {
        "bird_species": "Whopper",
        "count": "1"
      } ,
      {
        "bird_species": "eagle",
        "count": "1"
      } ,
      {
        "bird_species": "eagles",
        "count": "1"
      } ,
      {
        "bird_species": "red-tailed",
        "count": "1"
      }
    ]

    var col = [];
    for (var i = 0; i < myBooks.length; i++) {
        for (var key in myBooks[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }

    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table");

    var tr = table.insertRow(-1);                   

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");     
        th.innerHTML = col[i];
        tr.appendChild(th);
    }

    for (var i = 0; i < myBooks.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = myBooks[i][col[j]];
        }
    }

    var divContainer = document.getElementById("showData");
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
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