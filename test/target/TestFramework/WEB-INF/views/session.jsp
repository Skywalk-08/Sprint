<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Session</title></head>
<body>
<h1>Session counter: ${counter}</h1>
<p><a href="${pageContext.request.contextPath}/session">Incrementer</a></p>
<p><a href="${pageContext.request.contextPath}/">Retour</a></p>
</body>
</html>