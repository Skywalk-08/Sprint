<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Accueil - Framework Sprint 7</title></head>
<body>
<h1>Application de test du framework</h1>

<h2>Formulaire (binding de parametres)</h2>
<p><a href="form">Ouvrir le formulaire de saisie</a></p>

<h2>Routes disponibles</h2>
<ul>
    <li><code>GET /form</code> : affichage du formulaire</li>
    <li><code>POST /save</code> : binding <code>save()</code></li>
    <li><code>POST /save-named</code> : binding avec <code>@Param("...")</code></li>
    <li><code>POST /api/save</code> : reponse JSON (<code>@WebApi</code>)</li>
</ul>

<h2>API JSON</h2>
<ul>
    <li><a href="api/users">/api/users</a></li>
    <li><a href="api/user">/api/user</a></li>
    <li><a href="api/users-mv">/api/users-mv</a></li>
</ul>
</body>
</html>
