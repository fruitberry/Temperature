<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="cb" class="mybeans.ClBean" scope="request"/>
<%!
	ArrayList colname;
	ArrayList data;
%>
<%
	colname = cb.getColname();
	data    = cb.getData();
%>

<!DOCTYPE html>
<html>
<head>
<title>ある一日の気温</title>
</head>
<body>
日付     : <jsp:getProperty name="cb" property="ymd" />
<br/>
都道府県 : <jsp:getProperty name="cb" property="prefecture" />
<br/>
地点名   : <jsp:getProperty name="cb" property="area" />
<div style="text-align: center;">

<h2>時間当たりの気温推移</h2>
<hr>
<table border="1" style="margin-left: auto;
margin-right: auto;">
<tr bgcolor="#E0C76F">
<%
	for(int column=0; column< colname.size(); column++){
%>
<th>
<%= (String) colname.get(column)
%>
</th>
<%
	}
%>
</tr>
<%
	for(int row=0; row<data.size(); row++){
%>
<tr>
<%
		ArrayList rowdata = (ArrayList)(data.get(row));
		for(int column=0; column<rowdata.size(); column++){
%>
<td>
<%= rowdata.get(column) %>
</td>
<%
		}
%>
<tr>
<%
	}
%>
</table>
</div>
</body>
</html>