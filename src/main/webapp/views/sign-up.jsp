
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>registration</title>
</head>
<body>
<form action="/webApp1311_war/signup-servlet" method="post">
    <input type="text" required name="username" placeholder="username"/>
    <input type="password" required name="password" placeholder="password"/>
    <button>sign up</button>
</form>
<form action="/webApp1311_war/index.jsp">
    <button>log in</button>
</form>
</body>
</html>
