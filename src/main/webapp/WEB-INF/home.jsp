<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h2>Welcome, ${username}</h2>
<form action="/index.jsp" method="post">
    <input type="submit" name="addUserButton" value="Add User">

    <h1>List of Users</h1>
    <table>
      <c:forEach items="${usernamelst}" var="usern">
        <tr>
          <td><c:out value="${usern}" /></td>
          <td><input type="submit" name="${usern}" value="remove" onclick="return confirm('Are you sure you want to continue')"
          ></td>
        </tr>
      </c:forEach>
    </table>
</form>
</body>
</html>
