<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.io.BufferedReader" %>
<%@page import="java.io.FileInputStream" %>
<%@page import="java.io.InputStreamReader" %>
<%
String d=request.getParameter("d");
String t=request.getParameter("t");
String path = request.getSession().getServletContext()
.getRealPath("/");
BufferedReader in = new BufferedReader(new InputStreamReader(
new FileInputStream(path + "data/stream_" + t + "_" + d+".json"),
"gbk"));
String line = null;
while ((line = in.readLine()) != null) {
out.print(line);
}
in.close();
//String re;
//if (Integer.parseInt(t)%2==0)
//re="[[116.304,39.915,116.633,40.012,10],[116.404,39.915,116.476,39.787,500],[116.404,39.915,116.308,39.998,7403],[116.404,39.915,116.443,39.813,1613]]";
//else
//re="[[116.404,39.915,116.633,40.012,10],[116.404,39.915,116.476,39.787,500],[116.404,39.915,116.308,39.998,7403],[116.404,39.915,116.443,39.813,1613]]";
//out.print(re);
%>
