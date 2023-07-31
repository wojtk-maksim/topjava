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
            <td>
                DateTime:
            </td>
            <td>
                <label>
                    <input type="datetime-local" name="dateTime" value="${meal.dateTime}">
                </label>
            </td>
        </tr>
        <tr>
            <td>
                Description:
            </td>
            <td>
                <label>
                    <input type="text" name="description" value="${meal.description}">
                </label>
            </td>
        </tr>
        <tr>
            <td>
                Calories:
            </td>
            <td>
                <label>
                    <input type="number" name="calories" value="${meal.calories}">
                </label>
            </td>
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
