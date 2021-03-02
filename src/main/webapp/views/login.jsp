
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index</title>
</head>
<body>
<form action="/webApp1311_war/login-servlet" method="post">
    <input type="text" required name="username" value="${username}" placeholder="username"/>
    <input type="password" required name="password" value="${password}" placeholder="password"/>
    <button>log in</button>
</form>
<form action="/webApp1311_war/sign-up.jsp">
    <button>sign up</button>
</form>
</body>
</html>