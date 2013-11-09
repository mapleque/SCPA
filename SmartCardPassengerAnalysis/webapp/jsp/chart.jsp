<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String id=request.getParameter("id");
    String d=request.getParameter("d");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chart</title>
<style>
html,body {
	margin: 0;
	background:#fef;
	opacity:0.9;
}
</style>
</head>
<body>
	<div style="width: 100%; height: 100px;" id="graphdiv"></div>
	<script type="text/javascript" src="../js/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="../js/dygraph.js"></script>
	<script type="text/javascript" src="../js/area_1_data.js"></script>
	<script type="text/javascript">
		var div = document.getElementById("graphdiv");
		$(div).height($(window).height());
		g = new Dygraph(div, "../data/chart_<%out.print(id+"_"+d);%>.csv", {
			errorBars : true,
			valueRange : [ 0, 15000 ]
		}

		);
	</script>

</body>
</html>