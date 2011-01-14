<html>
<head>
<title>
CDF
</title>
<link href="default.css"
		 rel="stylesheet"
		 type="text/css" />

<script language="JavaScript" src="slider.js" ></script>
<script language="javascript" type="text/javascript" src="flot/jquery.js"></script>
<script language="javascript" type="text/javascript" src="flot/jquery.flot.js"></script>



</head>

<body>
<form name="insertForm" method="post" action="Ranking.jsp">
<input type="hidden" name="gap" value="">
</form>



<select name="category" id="category">
	<option value="">Select category:</option>
	<option value="global">global</option>
	<option value="jobs">jobs</option>
	<option value="ZodiacFacts">ZodiacFacts</option>
	<option value="blackfriday">blackfriday</option>
	<option value="music">music</option>
	<option value="newtwitter">newtwitter</option>
	<option value="iphone">iphone</option>
	<option value="travel">travel</option>
	<option value="health">health</option>
	<option value="love">love</option>
	<option value="android">android</option>
	<option value="linux">linux</option>
	<option value="football">football</option>
</select>

&nbsp;<p/>

Start Date: <input type="text" name="startdate" id="startdate" value=""/>
End Date:  <input type="text" name="enddate" id="enddate" value=""/>

<select name="gap" id="gap">
	<option value="0">Select time gap of:</option>
	<option value="10">10 hrs</option>
	<option value="20">20 hrs</option>
	<option value="24">24 hrs</option>
</select>

<form name="myform">
<div>
	<input type="text" id="inputUsername" size="25" value="Enter the user name here"><br>
	<input type="button" value="Add user to chart" onClick="addUser()">
	<input type="button" value="START" onClick="start()">
	<input type="button" value="STOP" onClick="stop()">
	<input type="button" value="CLEAR USER" onClick="clearUser()"><br>
</div>
</form>


<select name="refreshRate" id="refreshRate">
	<option value="10000">Refresh Rate:</option>
	<option value="30000">30 secs</option>
	<option value="40000">40 secs</option>
	<option value="50000">50 secs</option>
	<option value="60000">60 secs</option>
</select>

yaxis min: <input type="text" name="yaxismin" id="yaxismin" value="0.01"/>
yaxis max:  <input type="text" name="yaxismax" id="yaxismax" value="0.04"/>

<table border="0">
<tr>
<td></td>
<td><div id="placeholder" style="width:600px;height:300px;"></div></td>
<td>
	<div>
	<br><br>
		<textarea id="usernameText" disabled="true" COLS=60 ROWS=15>
		</textarea>
	<br><br>
	</div>
</td>
</tr>
<tr>
</tr>
</table> 	







</body>
</html>