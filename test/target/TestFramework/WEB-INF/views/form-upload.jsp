<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Upload de fichier</title></head>
<body>
<h1>Upload de fichier (single)</h1>

<form action="upload" method="post" enctype="multipart/form-data">
    <p>Description : <input type="text" name="description" /></p>
    <p>Fichier : <input type="file" name="file" /></p>
    <p><button type="submit">Envoyer</button></p>
</form>

<h1>Upload de fichiers (multiple)</h1>

<form action="upload-multi" method="post" enctype="multipart/form-data">
    <p>Description : <input type="text" name="description" /></p>
    <p>Fichiers : <input type="file" name="file" multiple /></p>
    <p><button type="submit">Envoyer</button></p>
</form>

<p><a href="${pageContext.request.contextPath}/">Retour a l'accueil</a></p>
</body>
</html>