<jsp:include page="template-top.jsp" />

<p style="font-size:medium">
    Messages 
</p>
<%@ page import="edu.cmu.cs15437.hw4.databeans.MessageBean" %>

<jsp:include page="error-list.jsp" />

<p>
	<table>
<%
    for (MessageBean p : (MessageBean[])request.getAttribute("mymessagelist")) {
%>	
		<form method=post  style="width:250px;" action="deletereadmessage.do?id=<%=p.getId()%>">
			<div id="bookmark<%= p.getId() %>" >
				<a style="font-family:Arial Black;color:green;font-size:20px;" class="sender" ><%= p.getSender() %></a></br>
				<a style="font-family:Arial Black;color:#003300;font-size:15px;" class="subject"  ><%= p.getSubject() %></a></br>
				<input  class="delete" type="submit" name="button" value="delete"/>
				<input  class="read" type="submit" name="button" value="read" />
			</div>
		</form>

<%
		}
%>
	</table>
</p>


<jsp:include page="template-bottom.jsp" />
