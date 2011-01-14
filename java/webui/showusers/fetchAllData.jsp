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
String username=request.getParameter("username");
String category=request.getParameter("category");
long startDate=Long.parseLong(request.getParameter("startDate"));
long endDate=Long.parseLong(request.getParameter("endDate"));
long gap=Long.parseLong(request.getParameter("gap"));

SimpleDateFormat formater = new SimpleDateFormat("MMddyyyyhhmmss");
Date start_date =  new Date();
start_date.setTime(startDate);
Date end_date =  new Date();
end_date.setTime(endDate);


long timelap = (end_date.getTime() - start_date.getTime()) / 5;

for (int i = 0; i < 6; i++) {
	long tempTime = start_date.getTime() + i * timelap;
	Date end_date_new = new Date();
	end_date_new.setTime(tempTime);
	Date start_date_new = new Date();
	start_date_new.setTime(tempTime - gap);
	if (gap == 0) {
		start_date_new = null;
	}
	for (Map.Entry<String,Float> x : top20.getTopK(20, category, start_date_new, end_date_new)) { 
		if (x.getKey().equals(username)) {
			out.println(end_date_new.getTime() + "," + x.getValue());
		}
	}
}

%>