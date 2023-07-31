<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h1>Edit meal</h1>
<form action="meals" method="POST">
    <c:set var="meal" value="${requestScope.meal}"/>
    <table>
        <tbody>
        <tr>
            <td><label for="dateTime">DateTime:</label></td>
            <td><input type="datetime-local" id="dateTime" name="dateTime" value="${meal.dateTime}"></td>
        </tr>
        <tr>
            <td><label for="description">Description:</label></td>
            <td><input type="text" id="description" name="description" value="${meal.description}"></td>
        </tr>
        <tr>
            <td><label for="calories">Calories:</label></td>
            <td><input type="number" id="calories" name="calories" value="${meal.calories}"></td>
        </tr>
        <tr>
            <td>
                <input type="hidden" name="id" value="${meal.id}">
                <input type="hidden" name="action" value="edit">
                <input type="submit" value="Save">
                <button onclick="window.history.back()" type="button">Cancel</button>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>
