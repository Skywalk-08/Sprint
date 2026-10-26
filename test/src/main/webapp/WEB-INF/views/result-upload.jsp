<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Resultat upload</title></head>
<body>
<h1>Fichier uploade</h1>
<p>Description : <c:out value="${description}" /></p>
<p>Nom : <c:out value="${file.filename}" /></p>
<p>Extension : <c:out value="${file.extension}" /></p>
<p>Taille : <c:out value="${file.size}" /> octets</p>
<p>Contenu : <pre><c:out value="${file.contentAsString}" /></pre></p>
<p><a href="${pageContext.request.contextPath}/form-upload">Retour au formulaire</a></p>
</body>
</html>