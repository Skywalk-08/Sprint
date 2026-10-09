<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Resultat (binding objet)</title></head>
<body>
<h1>Binding d'objet User</h1>
<p>User cree a partir des parametres du formulaire :</p>
<ul>
    <li>nom = ${nom}</li>
    <li>age = ${age}</li>
</ul>
<p><a href="form">Nouveau formulaire</a> &nbsp;|&nbsp; <a href="${pageContext.request.contextPath}/">Retour a l'accueil</a></p>
</body>
</html>
