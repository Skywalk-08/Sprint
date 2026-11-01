<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Success</title>
</head>
<body>
    <h1>Connecté</h1>
    <p>Utilisateur: <%= request.getAttribute("user") %></p>
    <p>Rôle: <%= request.getAttribute("role") %></p>
    <ul>
        <li><a href="protected">Page protégée (connecté requis)</a></li>
        <li><a href="admin">Page admin (ADMIN requis)</a></li>
        <li><a href="logout">Logout</a></li>
    </ul>
</body>
</html>
