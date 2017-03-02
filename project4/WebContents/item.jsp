<%--
  Created by IntelliJ IDEA.
  User: Nero
  Date: 3/2/17
  Time: 2:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" type="text/css" href="css/main.css">
<html>
<head>
    <title>Item</title>
</head>
<body>
    <% String xmlRaw= (String) request.getAttribute("itemXML"); %>
    <div>
       <%= xmlRaw %>
    </div>
</body>
</html>
