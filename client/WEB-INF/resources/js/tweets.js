var data;
var url = "http://localhost/WEB-INF/resources/locations/";

document.addEventListener('DOMContentLoaded', function() {
    console.log('test');
    CreateTableFromJSON();
}, false);

function getTweets()
{
    loadJson(url + "tweets.json", function(data){
        if(data !== null) {
            data = JSON.parse(data);
        }
        else
            console.log("Error");
    });
}

function CreateTableFromJSON() {
    console.log('test');
    var myBooks = [
            {
                "tweetMessage": "The Killdeer found one of the only open water spaces today in this crack",
                "language": "en"
                },
                {
                    "tweetMessage": "This red-tailed Eagle is so well camouflaged that I came within 6 feet of it before seeing it",
                    "language": "en"
                },
                {
                    "tweetMessage": "The trees filled with little Killdeers",
                    "language": "en"
                },
                {
                    "tweetMessage": "Why these Osprey carry flames in their beaks",
                    "language": "en"
                },
                {
                    "tweetMessage": "Whopper Osprey at Washington outskirts today",
                    "language": "en"
                },
                {
                    "tweetMessage": "Tweety pie the cute Eagle being showered with love???",
                    "language": "en"
                },
                {
                    "tweetMessage": "This Killdeer had quite a bit to say to us this morning.",
                    "language": "en"
                },
                {
                    "tweetMessage": "Eagle Friday! This pair of eagles are our observation today.",
                    "language": "en"
                },
                {
                    "tweetMessage": "Tufted Eagle spotted",
                    "language": "en"
                },
                {
                    "tweetMessage": "Through the lens: Eagles sighted!",
                    "language": "en"
                },
                {
                    "tweetMessage": "Urbanized Eagle loitering in front of a KFC",
                    "language": "en"
                },
                {
                    "tweetMessage": "We are officially off to track down a Osprey!",
                    "language": "en"
                },
                {
                    "tweetMessage": "The other day I spent a long time chasing Killdeer around",
                    "language": "en"
                },
                {
                    "tweetMessage": "Tourism sector to promote Eagle as niche market",
                    "language": "en"
                },
                {
                    "tweetMessage": "White-throated Osprey going the distance",
                    "language": "en"
                },
                {
                    "tweetMessage": "What a hoot! Just look at this Osprey!",
                    "language": "en"
                },
                {
                    "tweetMessage": "Some birds of paradise are the hawk",
                    "language": "en"
                },
                {
                    "tweetMessage": "Usually don't see this guy so active - Osprey",
                    "language": "en"
                },
                {
                    "tweetMessage": "Think Killdeer watching is something only hardened people enjoy?",
                    "language": "en"
                },
                {
                    "tweetMessage": "What kinda bird is that? It is a hawk, right?",
                    "language": "en"
                },
                {
                    "tweetMessage": "You've never seen a hawk do this!",
                    "language": "en"
                },
                {
                    "tweetMessage": "This is one of the  4 subspecies of the spectacular Killdeer!",
                    "language": "en"
                },
                {
                    "tweetMessage": "This adorable Killdeer was making the most of a very overcast day",
                    "language": "en"
                },
                {
                    "tweetMessage": "The Killdeers are busy chasing each other today.",
                    "language": "en"
                },
                {
                    "tweetMessage": "Tricolored eagle Eagle outskirts of the city",
                    "language": "en"
                },
                {
                    "tweetMessage": "Look at this! Just found a flock of Osprey",
                    "language": "en"
                },
                {
                    "tweetMessage": "The winter months are a perfect time to keep an eye out for beautiful Killdeer",
                    "language": "en"
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