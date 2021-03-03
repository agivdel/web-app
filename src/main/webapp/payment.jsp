<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<p>username:${username}</p>
<p>balance:${balance}</p>
<form action="/webApp1311_war/payment" method="post">
    <button>pay</button>
</form>
<form action="/webApp1311_war/logout">
    <button>log out</button>
</form>
${errorMessage}
</body>
</html>