
<html>
  <head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
    
      // Load the Visualization API and the piechart package.
      google.load('visualization', '1', {'packages':['annotatedtimeline']});
      google.setOnLoadCallback(timedMsg);
      
      // Set a callback to run when the Google Visualization API is loaded.
      
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
	  
	  
	  
        var data = new google.visualization.DataTable();
        data.addColumn('datetime', 'Time');
        data.addColumn('number', '[Person 1]');
        data.addColumn('string', 'title1');
        data.addColumn('string', 'text1');
        data.addColumn('number', '[Person 2]');
        data.addColumn('string', 'title2');
        data.addColumn('string', 'text2');
        data.addColumn('number', '[Person-3]');
        data.addColumn('string', 'title3');
        data.addColumn('string', 'text3');
		data.addRows(3);
		data.setCell(0, 0, new Date(2009, 1 ,1,10,30,20));
		data.setCell(0, 1, 3);
		data.setCell(0, 2, undefined);
		data.setCell(0,3, undefined);
		data.setCell(0,4, 4);
		data.setCell(0,5, undefined);
		data.setCell(0,6, undefined);
		data.setCell(0,7, 5);
		data.setCell(0,8, undefined);
		data.setCell(0,9, undefined);
		
		data.setCell(1, 0, new Date(2009, 1 ,2,10,30,20));
		data.setCell(1, 1, 4);
		data.setCell(1, 2, undefined);
		data.setCell(1,3, undefined);
		data.setCell(1,4, 2);
		data.setCell(1,5, undefined);
		data.setCell(1,6, undefined);
		data.setCell(1,7, 4);
		data.setCell(1,8, undefined);
		data.setCell(1,9, undefined);
		
		data.setCell(2, 0, new Date(2009, 1 ,3,10,30,20));
		data.setCell(2, 1, 5);
		data.setCell(2, 2, undefined);
		data.setCell(2,3, undefined);
		data.setCell(2,4, 5);
		data.setCell(2,5, undefined);
		data.setCell(2,6, undefined);
		data.setCell(2,7, 3);
		data.setCell(2,8, undefined);
		data.setCell(2,9, undefined);
		
		/*
        data.addRows([
          [new Date(2009, 1 ,1,10,30,20),3, undefined, undefined, 4, undefined, undefined,  5, undefined, undefined],
          [new Date(2009, 1 ,2,10,30,20), 4, undefined, undefined, 2, undefined, undefined, 4, undefined, undefined],
          [new Date(2009, 1 ,3,10,30,20), 5, undefined, undefined, 5, undefined, undefined, 3, undefined, undefined],
          [new Date(2009, 1 ,4,10,30,20), 7, undefined, undefined, 4, undefined,undefined, 2, undefined, undefined],
          [new Date(2009, 1 ,5,10,30,20), 1, undefined,undefined, 6, undefined, undefined,  5, undefined, undefined],
        [new Date(2009, 1 ,6,10,30,20), 1, 'Latest Rank of person 1', '3', 6, 'Latest Rank of person 2','4',  5, 'Latest Rank of person 3', '5']

        ]);
		*/
alert(new Date(2009, 1 ,1,10,30,20));

        var chart = new google.visualization.AnnotatedTimeLine(document.getElementById('chart_div'));
        alert(document.getElementById(chart));		
        chart.draw(data, {displayAnnotations: true});
	  
	  /*
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
		
i=i+8;

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, {width: 400, height: 240, is3D: true, title: 'My Daily Activities'});
		*/
      }
    </script>
  </head>

  <body>
    <!--Div that will hold the pie chart-->
    <div id='chart_div' style='width: 700px; height: 240px;'></div>
	
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

