<html>
<head>
<script type="text/javascript">
function showChart(str)
{
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
    document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
	document.getElementById("myiframe").src="timelinechart.php?q="+str;
	//document.myiframe.document.body.innerHTML = 'yourHTML';

    }
  }
xmlhttp.open("GET","getCharts.php?q="+str,true);
//xmlhttp.open("GET",chart.php,true);
xmlhttp.send();
}
</script>
</head>
<body>

<form>
<select name="users" onchange="showChart(this.value)">
<option value="">Select top K:</option>
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
<option value="4">4</option>
</select>
</form>
<br />
<div id="txtHint"><b>Chart will be displayed here.</b></div>

<iframe name="myiframe" id="myiframe" width="100%" height="300">
  <p>Your browser does not support iframes.</p>
</iframe>

</body>
</html>