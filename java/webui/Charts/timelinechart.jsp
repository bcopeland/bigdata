<%@ page import="walker.*,java.util.*,java.text.*" %>
<%!
HibernateGraphDB db;
Top20 top20;

public void jspInit()
{
    db = new HibernateGraphDB();
    top20 = new Top20(db);
}
%>


<html>
  <head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
    
      // Load the Visualization API and the piechart package.
      google.load('visualization', '1', {'packages':['annotatedtimeline']});
      google.setOnLoadCallback(drawChart);
      
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
        if(document.getElementById("days").value=="yes")
        data.addColumn('date', 'Date');
else
        data.addColumn('datetime', 'Time');

        
          for(var p=1;p<=document.getElementById("topk").value;p++)
          {

            data.addColumn('number', document.getElementById("person"+p).value);
            data.addColumn('string', 'title'+p);
            data.addColumn('string', 'text'+p);
        }
		data.addRows(4);
      for(var point=1;point<=4;point++)
          {   
            var k=0; 
               var year=document.getElementById("date"+point+"_year").value;
               var month=document.getElementById("date"+point+"_month").value;
               var date=document.getElementById("date"+point+"_day").value;
               var hour=document.getElementById("date"+point+"_hour").value;
               var minute=document.getElementById("date"+point+"_minute").value;
               var second=document.getElementById("date"+point+"_second").value;
		data.setCell(point-1, k, new Date(year, month ,date,hour,minute,second));

             for(var p=1;p<=document.getElementById("topk").value;p++)
             {
              k++;
		data.setCell(point-1, k, parseFloat(document.getElementById("data"+p+"_"+point).value));
              k++;
              if(point==4)
              data.setCell(point-1, k, 'Latest rank of '+document.getElementById("person"+p).value);
		else
		data.setCell(point-1, k, undefined);
              k++;
		if(point==4)
		data.setCell(point-1,k, document.getElementById("data"+p+"_"+point).value);
		else
              data.setCell(point-1, k, undefined);

             }
       }

        var chart = new google.visualization.AnnotatedTimeLine(document.getElementById('chart_div'));
        //alert(document.getElementById(chart));		
        chart.draw(data, {displayAnnotations: true, displayZoomButtons: false, displayRangeSelector: false});
	  
	
      }
    </script>
  </head>

  <body>
    <!--Div that will hold the pie chart-->
    <div id='chart_div' style='width: 700px; height: 240px;'></div>
	
	<input type="hidden" name="selectparam" id="selectparam" value="<%= request.getParameter("q") %>"/>
	<%
	  int q=Integer.parseInt(request.getParameter("q"));
      	  String category=request.getParameter("category");
         String gap=request.getParameter("gap");
         String startdate=request.getParameter("startdate");
         String enddate=request.getParameter("enddate");
         int refreshrate=Integer.parseInt(request.getParameter("refreshrate"));

Calendar now = Calendar.getInstance();

/*
int eyear=now.get(Calendar.YEAR);
int emonth=now.get(Calendar.MONTH)+1;
int eday=now.get(Calendar.DATE);
int ehour=now.get(Calendar.HOUR);
int eminute=now.get(Calendar.MINUTE);
int esecond=now.get(Calendar.SECOND);
*/

int eyear=0;
int emonth=0;
int eday=0;
int ehour=0;
int eminute=0;
int esecond=0;



int syear=0;
int smonth=0;
int sday=0;
int shour=0;
int sminute=0;
int ssecond=0;

if(!enddate.equalsIgnoreCase(""))
{
  String[] dates=enddate.split("/");
  emonth=Integer.parseInt(dates[0]);
  eday=Integer.parseInt(dates[1]);
  String day_times[]=dates[2].split(" ");
  eyear=Integer.parseInt(day_times[0]);
 
  String times[]=day_times[1].split(":");
  ehour=Integer.parseInt(times[0]);
  eminute=Integer.parseInt(times[1]);
  esecond=Integer.parseInt(times[2]);

}

if(!startdate.equalsIgnoreCase(""))
{
  String[] dates=startdate.split("/");
smonth=Integer.parseInt(dates[0]);  
sday=Integer.parseInt(dates[1]);
  
  
String day_times[]=dates[2].split(" ");
  syear=Integer.parseInt(day_times[0]);
 
  String times[]=day_times[1].split(":");
  shour=Integer.parseInt(times[0]);
  sminute=Integer.parseInt(times[1]);
  ssecond=Integer.parseInt(times[2]);

}


String date1_start=smonth+"/"+sday+"/"+syear+" "+shour+":"+sminute+":"+ssecond;
String date1_end=emonth+"/"+eday+"/"+eyear+" "+ehour+":"+eminute+":"+esecond;



Calendar scal = Calendar.getInstance();
Calendar ecal = Calendar.getInstance();
Calendar scal_start = Calendar.getInstance();
Calendar ecal_start = Calendar.getInstance();

Calendar mid1= Calendar.getInstance();
Calendar mid2= Calendar.getInstance();
Calendar mid1_start=Calendar.getInstance();
Calendar mid2_start= Calendar.getInstance();


scal.set(syear-1900,smonth-1,sday);
ecal.set(eyear-1900,emonth-1,eday);
scal_start.set(syear-1900,smonth-1,sday);
ecal_start.set(eyear-1900,emonth-1,eday);

mid1.set(syear-1900,smonth-1,sday);
mid1_start.set(syear-1900,smonth-1,sday);

mid2.set(eyear-1900,emonth-1,eday);
mid2_start.set(eyear-1900,emonth-1,eday);

long difInDays = ((ecal.getTime().getTime() - scal.getTime().getTime())/(1000*60*60*24));
String days="";
if(difInDays>2)
 days="yes";
else
  days="no";


difInDays*=24;
mid1.add(Calendar.HOUR,(int)difInDays/3);
mid1_start.add(Calendar.HOUR,(int)(difInDays/3)-Integer.parseInt(gap));
mid2.add(Calendar.HOUR,-(int)difInDays/3);
mid2_start.add(Calendar.HOUR,-(int)difInDays/3-Integer.parseInt(gap));
scal_start.add(Calendar.HOUR,-Integer.parseInt(gap));
ecal_start.add(Calendar.HOUR,-Integer.parseInt(gap));



%>

       <input type="hidden" name="topk" id="topk" value="<%=  q %>"/>
    <input type="hidden" name="diffdays" id="diffdays" value="<%=  difInDays %>"/>
        <input type="hidden" name="days" id="days" value="<%=  days %>"/>
	<input type="hidden" name="date1_year" id="date1_year" value="<%=  scal.get(Calendar.YEAR)+1900 %>"/>
      <input type="hidden" name="date2_year" id="date2_year" value="<%=  mid1.get(Calendar.YEAR)+1900  %>"/>
      <input type="hidden" name="date3_year" id="date3_year" value="<%=  mid2.get(Calendar.YEAR)+1900  %>"/>
      <input type="hidden" name="date4_year" id="date4_year" value="<%=  ecal.get(Calendar.YEAR)+1900  %>"/>

	<input type="hidden" name="date1_month" id="date1_month" value="<%=  scal.get(Calendar.MONTH) %>"/>
      <input type="hidden" name="date2_month" id="date2_month" value="<%=  mid1.get(Calendar.MONTH)  %>"/>
      <input type="hidden" name="date3_month" id="date3_month" value="<%=  mid2.get(Calendar.MONTH)  %>"/>
      <input type="hidden" name="date4_month" id="date4_month" value="<%=  ecal.get(Calendar.MONTH)  %>"/>

	<input type="hidden" name="date1_day" id="date1_day" value="<%=  scal.get(Calendar.DATE) %>"/>
      <input type="hidden" name="date2_day" id="date2_day" value="<%=  mid1.get(Calendar.DATE)  %>"/>
      <input type="hidden" name="date3_day" id="date3_day" value="<%=  mid2.get(Calendar.DATE)  %>"/>
      <input type="hidden" name="date4_day" id="date4_day" value="<%=  ecal.get(Calendar.DATE)  %>"/>



      <input type="hidden" name="date1_hour" id="date1_hour" value="<%=  scal.get(Calendar.HOUR) %>"/>
      <input type="hidden" name="date2_hour" id="date2_hour" value="<%=  mid1.get(Calendar.HOUR)  %>"/>
      <input type="hidden" name="date3_hour" id="date3_hour" value="<%=  mid2.get(Calendar.HOUR)  %>"/>
      <input type="hidden" name="date4_hour" id="date4_hour" value="<%=  ecal.get(Calendar.HOUR)  %>"/>

	 <input type="hidden" name="date1_minute" id="date1_minute" value="<%=  scal.get(Calendar.MINUTE) %>"/>
      <input type="hidden" name="date2_minute" id="date2_minute" value="<%=  mid1.get(Calendar.MINUTE)  %>"/>
      <input type="hidden" name="date3_minute" id="date3_minute" value="<%=  mid2.get(Calendar.MINUTE)  %>"/>
      <input type="hidden" name="date4_minute" id="date4_minute" value="<%=  ecal.get(Calendar.MINUTE)  %>"/>

	 <input type="hidden" name="date1_second" id="date1_second" value="<%=  scal.get(Calendar.SECOND) %>"/>
      <input type="hidden" name="date2_second" id="date2_second" value="<%=  mid1.get(Calendar.SECOND)  %>"/>
      <input type="hidden" name="date3_second" id="date3_second" value="<%=  mid2.get(Calendar.SECOND)  %>"/>
      <input type="hidden" name="date4_second" id="date4_second" value="<%=  ecal.get(Calendar.SECOND)  %>"/>



<%


for(int k=1;k<=4;k++)
{
Top20 top20  = new Top20(new HibernateGraphDB());
int i=1;
 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
Date start_Date = null;
Date end_Date=null;

//end_Date = formatter.parse("12/1/2010 2:21:35");
start_Date = formatter.parse(date1_start);
end_Date = formatter.parse(date1_end);


if(k==1)
{
  start_Date = formatter.parse((scal_start.get(Calendar.MONTH)+1)+"/"+scal_start.get(Calendar.DATE)+"/"+(scal_start.get(Calendar.YEAR)+1900)+" "+scal_start.get(Calendar.HOUR)+":"+scal_start.get(Calendar.MINUTE)+":"+scal_start.get(Calendar.SECOND));
  end_Date = formatter.parse((scal.get(Calendar.MONTH)+1)+"/"+scal.get(Calendar.DATE)+"/"+(scal.get(Calendar.YEAR)+1900)+" "+scal.get(Calendar.HOUR)+":"+scal.get(Calendar.MINUTE)+":"+scal.get(Calendar.SECOND));
}
if(k==2)
{
  start_Date = formatter.parse((mid1_start.get(Calendar.MONTH)+1)+"/"+mid1_start.get(Calendar.DATE)+"/"+(mid1_start.get(Calendar.YEAR)+1900)+" "+mid1_start.get(Calendar.HOUR)+":"+mid1_start.get(Calendar.MINUTE)+":"+mid1_start.get(Calendar.SECOND));
  //end_Date = formatter.parse((mid1.get(Calendar.MONTH)+1)+"/"+mid1.get(Calendar.DATE)+"/"+(mid1.get(Calendar.YEAR)+1900)+" "+mid1.get(Calendar.HOUR)+":"+mid1.get(Calendar.MINUTE)+":"+mid1.get(Calendar.SECOND));
}

if(k==3)
{
  start_Date = formatter.parse((mid2_start.get(Calendar.MONTH)+1)+"/"+mid2_start.get(Calendar.DATE)+"/"+(mid2_start.get(Calendar.YEAR)+1900)+" "+mid2_start.get(Calendar.HOUR)+":"+mid2_start.get(Calendar.MINUTE)+":"+mid2_start.get(Calendar.SECOND));
  end_Date = formatter.parse((mid2.get(Calendar.MONTH)+1)+"/"+mid2.get(Calendar.DATE)+"/"+(mid2.get(Calendar.YEAR)+1900)+" "+mid2.get(Calendar.HOUR)+":"+mid2.get(Calendar.MINUTE)+":"+mid2.get(Calendar.SECOND));
}
if(k==4)
{
  start_Date = formatter.parse((ecal_start.get(Calendar.MONTH)+1)+"/"+ecal_start.get(Calendar.DATE)+"/"+(ecal_start.get(Calendar.YEAR)+1900)+" "+ecal_start.get(Calendar.HOUR)+":"+ecal_start.get(Calendar.MINUTE)+":"+ecal_start.get(Calendar.SECOND));
  end_Date = formatter.parse((ecal.get(Calendar.MONTH)+1)+"/"+ecal.get(Calendar.DATE)+"/"+(ecal.get(Calendar.YEAR)+1900)+" "+ecal.get(Calendar.HOUR)+":"+ecal.get(Calendar.MINUTE)+":"+ecal.get(Calendar.SECOND));
}






for (Map.Entry<String,Float> x : top20.getTopK(q, category, start_Date, end_Date))
{  
   
%>
<%if(k==1)%>
    <input type="hidden" name="<%= "person"+i%>" id="<%= "person"+i%>" value="<%=  x.getKey()  %>"/>
    <input type="hidden" name="<%= "data"+i+"_"+k %>" id="<%= "data"+i+"_"+k %>" value="<%=  x.getValue() %>"/>
<%
 i++;
}
}
%>
	


	
  </body>
</html>

