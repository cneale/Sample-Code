<jsp:include page="template-top.jsp" />

<p style="font-size:medium">
    Compose A Message
</p>
<%@ page import="edu.cmu.cs15437.hw4.databeans.MessageBean" %>

<jsp:include page="error-list.jsp" />
<%
	MessageBean p = (MessageBean)(request.getSession().getAttribute("message"));
%>	
<p>
<form method="post" action="send.do" class="desc">
<li class="complex">
	<p>Message View</p>
	<div>
		<a  class="sender" ><%= p.getSender() %></a></br>
		<a  class="subject" ><%= p.getSubject() %></a></br>
		<a  class="message" ><%= p.getMessage() %></a></br>
		<li>

		<input class="field text addr" size="2" type="hidden" id="reciever" name="reciever" value="${form.reciever}"/>
		
		<input class="field text addr" size="2" type="hidden" id="sender" name="sender" value="${form.sender}"/>

		<input class="field text addr" size="8" type="hidden" id="subject" name="subject" value="${form.subject}"/>
	<% if(p.getRequest() == false){ %>
		<label class="desc">Reply<span class="req">*</span></label>
		<div>
		<textarea rows="10" cols="50" class="field textarea large" type="text" id="message" name="textmessage"></textarea>
		</div>

		<p class="instruct">Click Send On Completion</p>
		</li>

		<span class="left">
		<input type="submit" id="send" name="button" value="send message"/>
		</span>
	<%	} %>
	<% if(p.getRequest() == true){ %>
		<span class="left">
		<input type="submit" id="send" name="button" value="Accept As Friend"/>
		</span>
	<%	} %>
	
		</div>
</li>		
</form>
</p>	

<jsp:include page="template-bottom.jsp" />
