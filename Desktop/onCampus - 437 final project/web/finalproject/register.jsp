<jsp:include page="template-top.jsp" />

<p style="font-size:medium">
    To register, enter the following information. (All fields required.)
</p>

<jsp:include page="error-list.jsp" />

<p>
	<form method="post" action="register.do">
		<input type="hidden" name="redirect" value="${redirect}"/>
		<table>
			<tr>
				<td>First Name: </td>
				<td><input type="text" id="firstname" name="firstName" value="${form.firstName}"/></td>
			</tr>
			<tr>
				<td>Last Name: </td>
				<td><input type="text" id="lastname" name="lastName" value="${form.lastName}"/></td>
			</tr>
			<tr>
				<td><input type="hidden" id="email" name="userName" value="${form.userEmail}"/></td>
			</tr>
			<tr>
				<td><input type="hidden" id="password" name="password" value="${form.password}"/></td>
			</tr>
			<tr>
				<td>Confirm Password: </td>
				<td><input type="password" id="confirmpassword" name="confirm" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" id="completeregistration" name="button" value="Register"/>
				</td>
			</tr>
		</table>
	</form>
</p>

<jsp:include page="template-bottom.jsp" />

