<%@ page import="walker.*,java.util.*,java.text.*"  %>
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
<body>
Hello from tomcat..!
<table>
<%

/*
SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
Date start = sdf.parse("2010/10/01 00:00:00", new ParsePosition(0));
Date end = sdf.parse("2010/12/04 00:00:00", new ParsePosition(0));
*/

Date start = null;
Date end = null;

for (Map.Entry<String,Float> x : top20.getTopK(20, "android", start, end))
{   
    out.println("<tr><td>" + x.getKey() + "</td><td>" + 
        x.getValue() + "</td></tr>");
}
%>
</table>
</body>
</html>

