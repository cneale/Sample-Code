<jsp:include page="template-top.jsp" />

<p style="font-size:medium">
    Friends 
</p>
<%@ page import="edu.cmu.cs15437.hw4.databeans.UserBean" %>

<jsp:include page="error-list.jsp" />

<p>
	<table>
<%
	String[] ids =  (String[])request.getAttribute("friendid");
	int i = 0;
    for (UserBean p : (UserBean[])request.getAttribute("myfriendlist")) {
%>	
		<form method=post  style="width:250px;" action="removefriend.do?id=<%=ids[i]%>">
			<div id="bookmark<%= ids[i] %>" >
				<a style="font-family:Arial Black;color:green;font-size:20px;" class="sender" ><%= p.getFirstName() %></a></br>
				<a style="font-family:Arial Black;color:#003300;font-size:15px;" class="subject"  ><%= p.getLastName() %></a></br>
				<a style="font-family:Arial Black;color:#003300;font-size:15px;" class="subject"  ><%= p.getUserID() %></a></br>
				<input  class="delete" type="submit" name="button" value="remove"/>
			</div>
		</form>

<%
		i++;
		}
%>
	</table>
</p>


<jsp:include page="template-bottom.jsp" />
