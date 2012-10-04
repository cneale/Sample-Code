<jsp:include page="template-top.jsp" />

<p style="font-size:medium">
    My Bookmark List 
</p>
<%@ page import="edu.cmu.cs15437.hw4.databeans.MessageBean" %>
<%@ page import="edu.cmu.cs15437.hw4.databeans.UserBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="error-list.jsp" />

<form name="search" action="find.do" method="post">
	<table>
								<tr>
									<td>
										<font size="4">Last Name:</font>
									</td>
									<td>
										<input id="last" type="text" name="last" value="${last}"/>
									</td>
									<td>
										&nbsp;&nbsp;
										<font size="4">First Name:</font>
									</td>
									<td>
										<input id="first" type="text" name="first" value="${first}"/>
									</td>
									<td>
										<input id="submit" type="submit" name="submit" value="search"/>
									</td>
								</tr>
	<table>
</form>	
<p>
	<table>
<%
    for (UserBean p : (UserBean[])request.getAttribute("searchlist")) {
%>	
		<form method=post  style="width:250px;" action="addfriend.do">
			<div id="bookmark<%= p.getUserID() %>" >
				<a style="font-family:Arial Black;color:green;font-size:20px;" class="sender" ><%= p.getFirstName() %></a></br>
				<a style="font-family:Arial Black;color:#003300;font-size:15px;" class="subject"  ><%= p.getLastName() %></a></br>
				<a style="font-family:Arial Black;color:#003300;font-size:15px;" class="subject"  ><%= p.getUserID() %></a></br>
				<input  class="add" type="hidden" name="reciever" value="<%= p.getUserID() %>"/>
				<input  class="add" type="submit" name="button" value="add"/>
			</div>
		</form>

<%
		}
%>
	</table>
</p>


<jsp:include page="template-bottom.jsp" />
