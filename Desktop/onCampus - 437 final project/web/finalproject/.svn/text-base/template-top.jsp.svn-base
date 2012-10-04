<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml">


<head>

	<!-- Meta Tags -->
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="expires" content="0">

	<!-- JavaScript -->
	<script type="text/javascript" src="scripts/wufoo.js"></script>
	<script type="text/javascript" src="scripts/ajax.js"> </script>
    <script type="text/javascript" src="scripts/search-suggest.js"> </script>

	<!-- CSS -->
	<link rel="stylesheet" href="css/structure.css" type="text/css" />
	<link rel="stylesheet" href="css/form.css" type="text/css" />
	<link rel="stylesheet" href="css/theme.css" type="text/css" />

	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="pragma" content="no-cache">
	<title>Bookmark Website</title>
	<style>
		.menu-head {font-size: 10pt; font-weight: bold; color: black; }
		.menu-item {font-size: 10pt;  color: black }
    </style> 
</head>

<body>
<%@ page import="edu.cmu.cs15437.hw4.databeans.UserBean" %>

<table cellpadding="4" cellspacing="0">
    <tr>
	<%
		if (request.getAttribute("title") == null) {
	%>
		        <font size="7">OnCampus</font>
	<%
		} else {
	%>
		        <font size="5"><%=request.getAttribute("title")%></font>
	<%
		}
	%>
			</p>
		</td>
    </tr>
	
	<!-- Spacer row -->
	<tr>
		<td style="font-size:20px">&nbsp;</td>
		<td colspan="20" style="font-size:20px">&nbsp;</td>
	</tr>
	
	<tr>
		<td valign="top" height="500" width="200">
			<!-- Navigation bar is one table cell down the left side -->
            <p align="left">
<%
    UserBean user = (UserBean) session.getAttribute("user");
	if (user != null) {
%>
				<span class="menu-head"><%=user.getFirstName()%> <%=user.getLastName()%></span><br/>
<%
    } 
%>
				<span class="menu-item"><a id="inbox" href="messages.do"><img src="http://hyperlinkcode.com/images/sample-image.gif"></a></span><br/>
				<span class="menu-item"><a id="allbookmarks" href="send.do">Compose</a></span><br/>
				<span class="menu-item"><a id="Find Friends" href="find.do">All Bookmarks</a></span><br/>
				<span class="menu-item"><a id="My Friends" href="myfriendlist.do">All Bookmarks</a></span><br/>
				<span class="menu-item"><a id="logout" href="logout.do">Logout</a></span><br/>
				<span class="menu-item">&nbsp;</span><br/>
				
			</p>
		</td>
		
		<td>
			<!-- Padding (blank space) between navbar and content -->
		</td>
		<td  valign="top">
