<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Saisie utilisateur</title></head>
<body>
<h1>Formulaire de saisie</h1>

<p>Test de binding d'objet : <code>saveUser(User user)</code>. Les champs <strong>name</strong> et <strong>age</strong> sont settes automatiquement sur l'objet <code>User</code>.</p>

<form action="save-user" method="post">
    <p>Nom : <input type="text" name="name" /></p>
    <p>Age : <input type="text" name="age" /></p>
    <p><button type="submit">Envoyer (binding objet)</button></p>
</form>

<p><a href="${pageContext.request.contextPath}/">Retour a l'accueil</a></p>
</body>
</html>
