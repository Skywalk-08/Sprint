<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Resultat upload multiple</title></head>
<body>
<h1>Fichiers uploades</h1>
<p>Description : <c:out value="${description}" /></p>
<c:if test="${not empty files}">
  <ul>
    <c:forEach var="f" items="${files}">
      <li>
        <c:out value="${f.filename}" /> (.${f.extension}) - ${f.size} octets
        <c:if test="${not empty f.contentAsString}">
          <pre><c:out value="${f.contentAsString}" /></pre>
        </c:if>
      </li>
    </c:forEach>
  </ul>
</c:if>
<p><a href="${pageContext.request.contextPath}/form-upload">Retour au formulaire</a></p>
</body>
</html>