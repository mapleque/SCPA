<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%
	String d = request.getParameter("d");
	String t = request.getParameter("t");
	String path = request.getSession().getServletContext()
			.getRealPath("/");
	BufferedReader in = new BufferedReader(new InputStreamReader(
			new FileInputStream(path + "data/heat_" + t + "_" + d+".json"),
			"gbk"));
	String line = null;
	while ((line = in.readLine()) != null) {
		out.print(line);
	}
	in.close();
	//	String re;
	//	if (Integer.parseInt(t) % 2 == 0)
	//		re = "{\"max\": 100,\"data\": [{\"lat\": 116.374068, \"lng\":39.907198, \"count\": 100},{\"lat\": 116.256515, \"lng\":39.995242, \"count\": 100}]}";
	//	else
	//		re = "{\"max\": 100,\"data\": [{\"lat\": 116.504, \"lng\":39.915, \"count\": 100},{\"lat\": 116.256515, \"lng\":39.995242, \"count\": 100}]}";
	//	out.print(re);
%>
