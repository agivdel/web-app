
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<p><strong>username:</strong>${user.username}</p>
<p><strong>balance:</strong>${user.balance}</p>
<form action="/webApp1311_war/payment-servlet" method="post">
    <button>pay</button>
</form>
<form action="/webApp1311_war/index.jsp">
    <button>log out</button>
</form>
</body>
</html>