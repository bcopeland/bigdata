<html>
<head>
<script type="text/javascript">

function showChart()
{

 var str=document.getElementById("users").value;
var category=document.getElementById("category").value;
var gap=document.getElementById("gap").value;
var startdate=document.getElementById("startdate").value;
var enddate=document.getElementById("enddate").value;
var refreshrate=document.getElementById("refreshrate").value;


if (str=="")
  {
  document.getElementById("txtHint").innerHTML="";
  return;
  } 
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
           
    var d = new Date();
if(startdate=="")
{
  startdate='11/25/2010 '+d.getHours()+':'+d.getMinutes()+':'+d.getSeconds();
}

if(enddate=="")
{
  enddate=(d.getMonth()+1)+'/'+d.getDate()+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes()+':'+d.getSeconds();
}

    getUpdatedChart(str,category,gap,startdate,enddate,refreshrate);
     
	//document.myiframe.document.body.innerHTML = 'yourHTML';
	// document.getElementById("myiframe").src="timelinechart.jsp?q="+str+"&category="+category+"&gap="+gap+"&startdate="+startdate+"&enddate="+enddate+"&refreshrate="+refreshrate;


    }
  }
xmlhttp.open("GET","getCharts.jsp?q="+str,true);

xmlhttp.send();
}

function getUpdatedChart(str,category,gap,startdate,enddate,refreshrate)
{
	//alert(d.getMonth()+'/'+d.getDate()+'/'+d.getFullYear());  
	  

   //document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
   document.getElementById("myiframe").src="timelinechart.jsp?q="+str+"&category="+category+"&gap="+gap+"&startdate="+startdate+"&enddate="+enddate+"&refreshrate="+refreshrate;
var startdateparts=startdate.split('/');
var yeartimeparts=startdateparts[2].split(' ');
var timeparts=yeartimeparts[1].split(':');
var d = new Date(parseInt(yeartimeparts[0]), parseInt(startdateparts[0]), parseInt(startdateparts[1]), parseInt(timeparts[0]), parseInt(timeparts[1]), parseInt(timeparts[2]), 0);
d.setSeconds(d.getSeconds()+refreshrate);
startdate= d.getMonth()+'/'+d.getDate()+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes()+':'+d.getSeconds();

d=new Date();
enddate=(d.getMonth()+1)+'/'+d.getDate()+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes()+':'+d.getSeconds();


var t=setTimeout(getUpdatedChart,refreshrate*1000,str,category,gap,startdate,enddate,refreshrate);

}
</script>
</head>
<body>

<form>


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



<select name="users" id="users">
<option value="">Select top K:</option>
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
<option value="4">4</option>
</select>

<select name="gap" id="gap">
<option value="">Select time gap of:</option>
<option value="10">10 hrs</option>
<option value="20">20 hrs</option>
<option value="24">24 hrs</option>
</select>

Start Date: <input type="text" name="startdate" id="startdate" value=""/>
End Date:  <input type="text" name="enddate" id="enddate" value=""/>
<select name="refreshrate" id="refreshrate">
<option value="">Refresh Rate:</option>
<option value="30">30 secs</option>
<option value="40">40 secs</option>
<option value="50">50 secs</option>
<option value="60">60 secs</option>
</select>

<input type="submit" value="Generate visualization" onClick="showChart();return false;"/>


</form>
<br />
<div id="txtHint"><b>Chart will be displayed here.</b></div>

<iframe name="myiframe" id="myiframe" width="100%" height="300">
  <p>Your browser does not support iframes.</p>
</iframe>

</body>
</html>