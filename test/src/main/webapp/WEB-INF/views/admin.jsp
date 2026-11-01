<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
</head>
<body>
    <h1>Page Admin</h1>
    <p>Bienvenue <%= request.getAttribute("user") %> (<%= request.getAttribute("role") %>)</p>
    <p>Accès autorisé uniquement aux ADMIN</p>
    <p><a href="logout">Logout</a></p>
</body>
</html>
