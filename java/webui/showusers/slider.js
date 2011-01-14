var isRefresh = false;
var offset = 1000 * 60 * 60 * 5;

var users = [];
var userNumber = 0;
var userNames = [];

var xmlhttp;
var xmlUser;

var minShowTime;

function _showTest() {
	return function() {
		showTest(true);
	}
}


function showTest(continues) {


	if (continues) {
		window.setTimeout(_showTest(), document.getElementById('refreshRate').value);
	}
	
	if (isRefresh && userNumber >= 1) {
		var userNumberTemp = userNumber;
		
		for (var j = 0; j < userNumberTemp; j++) {
			while (typeof  users[j] == undefined || users[j] == null) {
			}
		}
		
		/*		
		for (var j = 0; j < userNumberTemp; j++) {
			if (typeof  users[j] == undefined || users[j] == null) {
				users[j] = [];
				for (var i = 0; i <= 30; i += 2) {
					var current = new Date();
					var tempTime = new Date();
					tempTime.setTime(current.getTime() - offset - (31 - i) * 1000);
					
					var temp =  Math.floor(Math.random()*1001);
					users[j].push([tempTime, temp / 1000]);
				}
			}			
		}
		*/
		
		var userTemp = [];
		for (var j = 0; j < userNumberTemp; j++) {
			userTemp[j] = [];
		}
			
		
		for (var j = 0; j < userNumberTemp; j++) {
			
			for (var i = (users[j].length > 50) ? users[j].length - 50 : 0; i < users[j].length; i ++) {
				userTemp[j].push([users[j][i][0], users[j][i][1]]);
			}
			
			
		}
		
			
		var currentTime = new Date();
		var localTime = currentTime.getTime() - offset;	
		var currentTemp = new Date();
		currentTemp.setTime(localTime);
		
		
		
		
		var usernameParam = "";
		for (var i = 0; i < userNumberTemp - 1; i++) {
			usernameParam += userNames[i] + "|";
		}
		usernameParam += userNames[userNumberTemp - 1];
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlUser =new XMLHttpRequest();
		}
		else {// code for IE6, IE5
			xmlUser =new ActiveXObject("Microsoft.XMLHTTP");
		}
		var url = "fetchData.jsp?";
		url += "&usernames=" + usernameParam;
		url += "&category=" + document.getElementById('category').value;
		url += "&currentDate=" + (new Date()).getTime();
		var gap = document.getElementById('gap').value * 60 * 60 * 1000;
		url += "&gap=" + gap;
		
		xmlUser.open("POST",url,false);
		xmlUser.onReadyStateChange = updateUser;
		xmlUser.setRequestHeader("content-type","application/x-www-form-urlencoded"); 
		xmlUser.send("");
		var responseStr = updateUser();
		//alert(responseStr);
		
			
		var splits = responseStr.split("\n");
		for (var m = 1; m < splits.length - 1; m++) {
			var elem = splits[m].split(",");
			for (var j = 0; j < userNumberTemp; j++) {
				/*
				var temp =  Math.floor(Math.random()*1001);
				userTemp[j].push([currentTemp, temp / 1000]);
				users[j] = userTemp[j];
				*/
				if (userNames[j] == elem[0]) {
					var dateTemp = new Date();
					dateTemp.setTime(elem[1] - offset);
					//alert(elem[0] + " : " + dateTemp + "  value is: " + elem[2]);
					userTemp[j].push([dateTemp, elem[2]]);
				}
				users[j] = userTemp[j];
			}
		}
			
			
		
		var minTime = localTime - minShowTime;
		var gapBetween = (localTime - minTime) / 6 / 1000 / 60;
		
		$.plot($("#placeholder"), 
				users, 
				{
					xaxis: 
					{
						mode: "time",
						timeformat: "%m/%d %h:%M:%S",
						//minTickSize: [document.getElementById('gap').value / 5, "hour"],
						//min: localTime - document.getElementById('gap').value * 60 * 60 * 1000,
						minTickSize: [gapBetween, "minute"],
						min:  minTime,
						max: localTime
						//min: (new Date("2010/11/28 UTC 00:00:00")).getTime(),
						//max: (new Date("2010/11/28 UTC 00:00:30")).getTime() - 10000
					},
					yaxis: 
					{
						min: document.getElementById('yaxismin').value,
						max: document.getElementById('yaxismax').value
					}
		});

		
		var str = "";
		for (var i = 0; i < userNumberTemp; i++) {
			str += userNames[i] + "'s current PageRank is " + users[i][users[i].length - 1][1] + "\n";
		}
		document.getElementById('usernameText').value = str;
	}
	
	else if (userNumber == 0) {
		var currentTime = new Date();
		var localTime = currentTime.getTime() - offset;	
		var currentTemp = new Date();
		currentTemp.setTime(localTime);
		
		$.plot($("#placeholder"), 
				[],
				{
					xaxis: 
					{
						mode: "time",
						timeformat: "%h:%M:%S",
						//minTickSize: [1, "second"],
						min: localTime - 40000,
						max: localTime
					},
					yaxis: 
					{
						min:0,
						max:0.1
					}
		});
	}
}

function updateUser() {	
	return xmlUser.responseText;
}

function start() {
	users[userNumber - 1]  = [];
	
	isRefresh = true;	
	
	//alert(xmlhttp.responseText);
	
	var splits = xmlhttp.responseText.split("\n");
	for (var i = 1; i < splits.length - 1; i++) {
		var elem = splits[i].split(",");
		var tempTime = new Date();
		tempTime.setTime(elem[0] - offset);
		users[userNumber - 1].push([tempTime, elem[1]]);
	}
	
	showTest(false);
}

function stop() {
	isRefresh = false;
}

function addUser() {
	if (userNumber == 0) {		
		minShowTime = (new Date()).getTime() - (new Date(document.getElementById('startdate').value)).getTime();
	}

	userNumber ++;
	userNames[userNumber - 1] = document.getElementById('inputUsername').value;	
	
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp =new XMLHttpRequest();
	}
	else {// code for IE6, IE5
		xmlhttp =new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	var url = "fetchAllData.jsp?";
	url += "&username=" + userNames[userNumber - 1];
	url += "&category=" + document.getElementById('category').value;
	url += "&startDate=" + (new Date(document.getElementById('startdate').value)).getTime();
	url += "&endDate=" + (new Date()).getTime();
	var gap = document.getElementById('gap').value * 60 * 60 * 1000;
	url += "&gap=" + gap;
	
	xmlhttp.open("POST",url,false);
	xmlhttp.onReadyStateChange = start;
	xmlhttp.setRequestHeader("content-type","application/x-www-form-urlencoded"); 
	xmlhttp.send("");
	start();
}

function clearUser() {
	userNumber = 0;
	users = [];
	userNames = [];
	showTest(false);
	document.getElementById('usernameText').value = "";
}


window.onload = function() {
	showTest(true);
}