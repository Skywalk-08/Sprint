<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <h1>Login (Sprint 10)</h1>
    <% if(request.getAttribute("error") != null){ %>
        <p style="color:red"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="login" method="post">
        <label>Username: <input type="text" name="username" value="admin"></label><br>
        <label>Password: <input type="password" name="password" value="admin"></label><br>
        <button type="submit">Login</button>
    </form>
    <p>admin/admin (ADMIN) | user/user (USER)</p>
    <p><a href="index">Accueil</a></p>
</body>
</html>
