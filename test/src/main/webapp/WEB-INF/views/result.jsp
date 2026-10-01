<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Resultat</title></head>
<body>
<h1>Donnees liees (binding)</h1>
<p>Valeurs associees a <code>save(String i, String n, int age)</code> :</p>
<ul>
    <li>i = ${id}</li>
    <li>n = ${nom}</li>
    <li>age = ${age}</li>
</ul>
<p><a href="form">Nouveau formulaire</a></p>
<p><a href="${pageContext.request.contextPath}/">Retour a l'accueil</a></p>
</body>
</html>
