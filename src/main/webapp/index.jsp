
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index</title>
</head>
<body>
<form action="/webApp1311_war/login" method="post">
    <input type="text" required name="username" placeholder="username"/>
    <input type="password" required name="password" placeholder="password"/>
    <button>log in</button>
</form>
<form action="/webApp1311_war/signup.jsp">
    <button>sign up</button>
</form>
<form action="/webApp1311_war/payment.jsp">
    <button>payment</button>
</form>
${errorMessage}
</body>
</html>