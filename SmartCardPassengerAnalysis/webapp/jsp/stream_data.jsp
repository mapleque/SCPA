<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String d=request.getParameter("d");
String t=request.getParameter("t");
String re;
if (Integer.parseInt(t)%2==0)
re="[[116.304,39.915,116.633,40.012,10],[116.404,39.915,116.476,39.787,500],[116.404,39.915,116.308,39.998,7403],[116.404,39.915,116.443,39.813,1613]]";
else
re="[[116.404,39.915,116.633,40.012,10],[116.404,39.915,116.476,39.787,500],[116.404,39.915,116.308,39.998,7403],[116.404,39.915,116.443,39.813,1613]]";
out.print(re);
%>
