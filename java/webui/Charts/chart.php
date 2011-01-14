
<html>
  <head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
    
      // Load the Visualization API and the piechart package.
      google.load('visualization', '1', {'packages':['corechart']});
      
      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(timedMsg);
      var i=11;
      function timedMsg()
{
drawChart();
var t=setTimeout("timedMsg()",5000);

}
      // Callback that creates and populates a data table, 
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {
//      alert('5 seconds!');
alert(document.getElementById("selectparam").value);
alert(document.getElementById("data1").value);
var no_of_elements=document.getElementById("selectparam").value;
      // Create our data table.
        var data = new google.visualization.DataTable();
		
        data.addColumn('string', 'Task');
        data.addColumn('number', 'Hours per Day');
		
		for(var k=1;k<=no_of_elements;k++)
		{
		  data.addRows([
          [document.getElementById("person"+k).value, parseInt(document.getElementById("data1").value)] 
        ]);
		}
		/*
        data.addRows([
          ['Work', i],
          ['Eat', i+3],
          ['Commute', i+6],
          ['Watch TV', i+6],
          ['Sleep', i+4]

        ]);
		
		data.addRows([
          ['Work-New', i]

        ]);
		data.addRows([
          ['Work-New1', i]

        ]);
		*/
		/*
		var p=document.getElementById("person1").value;
		data.addRows([
          [document.getElementById("person1").value, i]

        ]);
		var d=2;
		data.addRows([
          [document.getElementById("person"+d).value, i]

        ]);
	
		*/
i=i+8;

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, {width: 400, height: 240, is3D: true, title: 'My Daily Activities'});
      }
    </script>
  </head>

  <body>
    <!--Div that will hold the pie chart-->
    <div id="chart_div"></div>
	<input type="text" name="selectparam" id="selectparam" value="<?php echo $_GET["q"] ?>"/>
	<?php 
$q=$_GET["q"];
if ($q==2)
{
  for($i=1;$i<=$q;$i++)
  {
   // $i=1;
	?> 
  <input type="text" name="<?php echo "person".$i ?>" id="<?php echo "person".$i ?>" value="<?php echo "person".$i ?>"/>
  <input type="text" name="<?php echo "data".$i ?>" id="<?php echo "data".$i ?>" value="<?php echo $i*100 ?>"/>
  <?php 
  }
}
?>

	
  </body>
</html>
<?php 
$q=$_GET["q"];
echo "You selected ".$q;
?> 
