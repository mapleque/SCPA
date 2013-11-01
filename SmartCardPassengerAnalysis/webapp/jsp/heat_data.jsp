<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String d = request.getParameter("d");
	String t = request.getParameter("t");
	String re;
	if (Integer.parseInt(t) % 2 == 0)
		re = "{\"max\": 100,\"data\": [{\"lat\": 116.374068, \"lng\":39.907198, \"count\": 100},{\"lat\": 116.256515, \"lng\":39.995242, \"count\": 100}]}";
	else
		re = "{\"max\": 100,\"data\": [{\"lat\": 116.504, \"lng\":39.915, \"count\": 100},{\"lat\": 116.256515, \"lng\":39.995242, \"count\": 100}]}";
	out.print(re);
%>
