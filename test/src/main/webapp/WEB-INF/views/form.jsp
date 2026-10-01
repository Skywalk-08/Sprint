<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Saisie utilisateur</title></head>
<body>
<h1>Formulaire de saisie</h1>


<form action="save" method="post">
    <p>Identifiant : <input type="text" name="i" /></p>
    <p>Nom : <input type="text" name="n" /></p>
    <p>Age : <input type="text" name="age" /></p>
    <p><button type="submit">Envoyer</button></p>
</form>

<p>
    <a href="${pageContext.request.contextPath}/">Retour a l'accueil</a>
    &nbsp;|&nbsp;
    <a href="api/users">Voir les users en JSON</a>
</p>
</body>
</html>
