<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %><%--
  Created by IntelliJ IDEA.
  User: Dns
  Date: 09.02.2020
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
<html>
<head>
    <title>Meals</title>
</head>
<body>
        <table align = "center" border="1">
            <c:set var="dateTimeFormatter" value="${DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")}"/>
            <c:forEach var = "meal" items = "${meals}">
            <c:if test="${meal.excess}">
                <tr style="color: red">
            </c:if>
                <c:if test="${meal.excess == false}">
                    <tr style="color: green">
                </c:if>
                    <td>${meal.dateTime.format(dateTimeFormatter)}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                </tr>
            </c:forEach>
        </table>
</body>
</html>
