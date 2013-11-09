<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.BufferedReader" %>
<%@page import="java.io.FileInputStream" %>
<%@page import="java.io.InputStreamReader" %>
<%
String d=request.getParameter("d");
String t=request.getParameter("t");
//String re;
String path=request.getSession().getServletContext().getRealPath("/");
BufferedReader in=new BufferedReader(new InputStreamReader(
		new FileInputStream(path+"data/area_"+t+"_"+d+".json"), "gbk"));
String line=null;
while ((line=in.readLine())!=null){
	out.print(line);
}
in.close();
//if (Integer.parseInt(t)%2==0)
//re="[[[ [ 116.156515, 39.995242 ], [ 116.252579, 39.971893 ],[ 116.284775, 39.978338 ], [ 116.286515, 39.996882 ] ],5380 ],[[ [ 116.356515, 39.995242 ], [ 116.352579, 39.971893 ],[ 116.384775, 39.978338 ], [ 116.386515, 39.996882 ] ],36 ],[[ [ 116.256515, 39.895242 ], [ 116.252579, 39.871893 ],[ 116.284775, 39.878338 ], [ 116.286515, 39.896882 ] ],1380 ],[[ [ 116.456515, 39.965242 ], [ 116.452579, 39.951893 ], [ 116.464775, 39.948338 ],[ 116.484775, 39.958338 ], [ 116.486515, 39.976882 ] ],380 ]]";
//else
//	re="[[[ [ 116.056515, 39.995242 ], [ 116.252579, 39.971893 ],[ 116.284775, 39.978338 ], [ 116.286515, 39.996882 ] ],5380 ],[[ [ 116.356515, 39.995242 ], [ 116.352579, 39.971893 ],[ 116.384775, 39.978338 ], [ 116.386515, 39.996882 ] ],36 ],[[ [ 116.256515, 39.895242 ], [ 116.252579, 39.871893 ],[ 116.284775, 39.878338 ], [ 116.286515, 39.896882 ] ],1380 ],[[ [ 116.456515, 39.965242 ], [ 116.452579, 39.951893 ], [ 116.464775, 39.948338 ],[ 116.484775, 39.958338 ], [ 116.486515, 39.976882 ] ],380 ]]";
//out.print(re);
%>