<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Protected</title>
</head>
<body>
    <h1>Page protégée</h1>
    <p>Bienvenue <%= request.getAttribute("user") %> (<%= request.getAttribute("role") %>)</p>
    <p><a href="logout">Logout</a></p>
</body>
</html>
