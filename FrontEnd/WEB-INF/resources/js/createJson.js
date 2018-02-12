function createGeoJson(data)
{

	var jsonData = new Object();
	jsonData.type = "FeatureCollection";
	jsonData.features = [];

	for(var i = 0 ;i<data.features.length; i++){

		var hotspot = data.features[i];
		findTimeStamp(hotspot);

		jsonData.features.push(hotspot);



/*		var feature = new Object();
		feature.type = "Feature";

		var properties = new Object();
		properties.howMany = hotspot.properties.howMany;
		properties.tweetMessage = hotspot.properties.tweetMessage;
		properties.birdSpeciesList = [];
		for(var j = 0; j < hotspot.properties.birdSpeciesList.length; j++)
		{
			var item = hotspot.properties.birdSpeciesList[j];
			properties.birdSpeciesList.push(item);	
		}
		
		properties.timestamp = hotspot.properties.timestamp;

		var user = new Object();
		user = hotspot.properties.user;


		user.name = hotspot.properties.user.name;
		user.location = hotspot.properties.user.location;
		user.id = hotspot.properties.user.id;
		user.screenName = hotspot.properties.user.screenName;
		user.isGeoEnabled = hotspot.properties.user.isGeoEnabled;
		user.email = hotspot.properties.user.email;

		properties.user = user;

		var geometry = new Object();
		geometry = hotspot.geometry;

		
		geometry.type = "Point";
		geometry.coordinates = hotspot.
		geometry.coordinates.push(hotspot.geometry.coordinates[0]);
		geometry.coordinates.push(hotspot.geometry.coordinates[1]);

		feature.geometry = geometry;
		feature.properties = properties;

		jsonData.features.push(feature);*/
		
	}

	console.log(jsonData);

}

function findTimeStamp(hotspot)
{
	console.log(hotspot.properties.timestamp);
}

function findNextHotspot(data,id)
{
	for(let i = 0 ; i<data.features.length; i++) {
		if(data.features[i].properties.hotspotId == id)
			return data.features[i];
	}

	return null;
}

function getHotspotstsByTime(data,timestamp,type)
{

	let hotspots = new Object();
	hotspots.type = "FeatureCollection";
	hotspots.features = [];
	let key = timestamp.Year + "-" + timestamp.Month + "-" + timestamp.Day;

	if(type == "migrations")
	{
		for(let i = 0 ; i<data.features.length; i++) {
			var hotspot = data.features[i];
			if(hotspot.properties.fromHotspot.observationDate.year == timestamp.Year &&
		   		hotspot.properties.fromHotspot.observationDate.monthValue == timestamp.Month &&
		   		hotspot.properties.fromHotspot.observationDate.dayOfMonth == timestamp.Day
			) {
				hotspots.features.push(hotspot);
			}
		}
	} else {
		for(let i = 0 ; i<data.features.length; i++) {
			var hotspot = data.features[i];
			if(hotspot.properties.observationDate.year == timestamp.Year &&
		   		hotspot.properties.observationDate.monthValue == timestamp.Month &&
		   		hotspot.properties.observationDate.dayOfMonth == timestamp.Day
			) {
				hotspots.features.push(hotspot);
			}
		}
	}

	let item = new Object();
	item.Date = key;
	item.Data = hotspots;

	allDays.push(item)
	allDays.sort(sortByKey);
	
	return hotspots;
}

function separateHotspots(data)
{
	let hotspots = new Object();
	hotspots.type = "FeatureCollection";
	hotspots.features = [];
}

function findEarliesDate(data, type)
{

	let date = new Date("2030-01-01");

	for(let i = 0 ; i<data.features.length; i++)
	{
		let hotspot = data.features[i];
		let currentDate = new Object();

		if( type == "migration")
		{
			currentDate.Year  = hotspot.properties.fromHotspot.observationDate.year;
			currentDate.Month = hotspot.properties.fromHotspot.observationDate.monthValue;
			currentDate.Day   = hotspot.properties.fromHotspot.observationDate.dayOfMonth;

		} else {
			currentDate.Year  = hotspot.properties.observationDate.year;
			currentDate.Month = hotspot.properties.observationDate.monthValue;
			currentDate.Day   = hotspot.properties.observationDate.dayOfMonth;
		}
		
		let curDate = currentDate.Year + "-" + currentDate.Month + "-" + currentDate.Day;
		let newDate = new Date(curDate);

		if(newDate < date){
			date = newDate;
		}
	}

	return date;
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

function findNextStep(date)
{
	tempSpots = getHotspotstsByTime(allHotspots,date,'hostpot');
	console.log(tempSpots.features.length);
	for(let j = 0; j<tempSpots.features.length; j++)
	{
		let spotId = tempSpots.features[j].properties.hotspotId;
		fromId = findHotspotFromById(spotId);
		if(!fromId) {
			currentHotspots.features.push(tempSpots.features[j]);
		} else {
			removeHotspotById(fromId);
			currentHotspots.features.push(tempSpots.features[j]);
		}
	}
}

function findHotspotFromById(id)
{
	for(let i = 0 ; i<migrationHotspots.features.length; i++)
	{
		let hotspot = migrationHotspots.features[i];
		if(id == hotspot.properties.toHotspot.hotspotId)
		{
			return hotspot.properties.fromHotspot.hotspotId;
		}
	}

	return false;
}

function removeHotspotById(id)
{
	for(let i = 0 ; i<currentHotspots.features.length; i++)
	{
		let hotspot = currentHotspots.features[i];
		if(id == hotspot.properties.hotspotId){
			currentHotspots.features[i].properties.trail = true;
			//currentHotspots.features.splice(i, 1);
		}
	}
}


