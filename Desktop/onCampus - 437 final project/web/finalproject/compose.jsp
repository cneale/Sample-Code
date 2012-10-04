<jsp:include page="template-top.jsp" />

<p style="font-size:medium">
    Compose A Message
</p>
<%@ page import="edu.cmu.cs15437.hw4.databeans.MessageBean" %>

<jsp:include page="error-list.jsp" />

<p>
<form method="post" action="send.do" class="desc">
<li class="complex" style="width:500px;">
	<p>Compose</p>
	<div>
		<span class="full">
		<input class="field text addr" size="2" type="text" id="reciever" name="reciever" value="${form.reciever}"/>
		<label>User ID</label>
		</span>
		
		<input class="field text addr" size="2" type="hidden" id="sender" name="sender" value="${form.sender}"/>

		<span class="full">
		<input class="field text addr" size="8" type="text" id="subject" name="subject" value="${form.subject}"/>
		<label>Subject</label>
		</span>
		
		<li>
		<label class="desc">Message<span class="req">*</span></label>
		<div>
		<textarea name="textmessage" style="background-color:#D0F18F;color:#53760D;font:24px/30px cursive;scrollbar-base-color:#638E0D;" ></textarea>
		</div>

		<p class="instruct">Click Send On Completion</p>
	</li>

		<span class="left">
		<input type="submit" id="send" name="button" value="send message"/>
		</span>
		
		</div>
</li>		
</form>
</p>	

<jsp:include page="template-bottom.jsp" />
