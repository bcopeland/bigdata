<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="walker.*,java.util.*,java.text.*"%>
<%!
HibernateGraphDB db;
Top20 top20;

public void jspInit()
{
    db = new HibernateGraphDB();
    top20 = new Top20(db);
}
%>

<%
String usernames=request.getParameter("usernames");
String[] usernameList = usernames.split("\\|");
String category=request.getParameter("category");
long currentDate=Long.parseLong(request.getParameter("currentDate"));
long gap=Long.parseLong(request.getParameter("gap"));

Date end_date = new Date();
end_date.setTime(currentDate);
Date start_date = new Date();
start_date.setTime(currentDate - gap);

if (gap == 0) {
	start_date = null;
}

for (int i = 0; i < usernameList.length; i++) {
	for (Map.Entry<String,Float> x : top20.getTopK(20, category, start_date, end_date)) { 
		if (x.getKey().equals(usernameList[i])) {
			out.println(usernameList[i] + "," + end_date.getTime() + "," + x.getValue());
		}
	}
}

%>