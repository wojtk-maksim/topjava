<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<html>
<head>
    <title>meals</title>
    <link rel="stylesheet" href="css/meals.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h1>Meals</h1>
<h3><a href="meals?action=edit">Add meal</a></h3>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:set var="dateTimeFormatter" value="${TimeUtil.DATE_TIME_FORMATTER}"/>
    <c:forEach items="${requestScope.meals}" var="meal">
        <tr style="color: ${meal.excess ? 'red' : 'green'}">
            <td>${meal.dateTime.format(dateTimeFormatter)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <form action="meals">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" value="${meal.id}">
                    <input type="submit" value="Update">
                </form>
            </td>
            <td>
                <form action="meals" method="POST">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${meal.id}">
                    <input type="submit" value="Delete">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
