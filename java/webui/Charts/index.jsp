<%@ page import="walker.*,java.util.*" %>

<html>
<body>
Hello from tomcat..!
<table>
<%
/*
Top20 top20  = new Top20(new HibernateGraphDB());

for (Map.Entry<String,Float> x : top20.getTopK(4, "global"))
{   
    out.println("<tr><td>" + x.getKey() + "</td><td>" + 
        x.getValue() + "</td></tr>");
}
*/
Calendar now = Calendar.getInstance();

    

 out.println(now.get(Calendar.YEAR));
 out.println(now.get(Calendar.MONTH));
 out.println(now.get(Calendar.DATE));
 out.println(now.get(Calendar.HOUR));
 out.println(now.get(Calendar.MINUTE));
 out.println(now.get(Calendar.SECOND));

Calendar scal = Calendar.getInstance();
scal.set(now.get(Calendar.YEAR)-1900,now.get(Calendar.MONTH)-1,now.get(Calendar.DATE),now.get(Calendar.HOUR),now.get(Calendar.MINUTE),now.get(Calendar.SECOND));




%>
</table>
</body>
</html>

