<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<form action="/index.jsp" method="post">
<div>
    <h2>Welcome, ${username}</h2>
    <input type="submit" name="logout" value="Logout">
    <input type="submit" name="editProfile" value="Edit Profile">
</div>

    <input type="submit" name="addUserButton" value="Add User">

    <h1>List of Users</h1>
    <table>
      <c:forEach items="${usernameStatusMap}" var="usern">
        <tr>
          <td><c:out value="${usern.key}" /></td>
          <td><p>${usern.value}</p></td>
          <td><input type="submit" name="${usern.key}" value="remove" onclick="return confirm('Are you sure you want to continue')"
          ></td>
        </tr>
      </c:forEach>
    </table>
</form>
</body>
</html>
