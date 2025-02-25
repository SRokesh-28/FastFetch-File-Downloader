<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // Check if session already exists
    HttpSession sessionObj = request.getSession(false);
    boolean redirect = false;  // ✅ Track if redirection happens

    if (sessionObj == null || sessionObj.getAttribute("userEmail") == null) {
        Cookie[] cookies = request.getCookies();
        boolean loggedIn = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userEmail".equals(cookie.getName())) {
                    String userEmail = cookie.getValue();

                    // ✅ Create session and store userEmail
                    sessionObj = request.getSession(true);
                    sessionObj.setAttribute("userEmail", userEmail);

                    loggedIn = true;
                    break;
                }
            }
        }

        // ✅ Redirect based on login status
        if (loggedIn) {
            response.sendRedirect("DynamicDownlo.jsp");
        } else {
            response.sendRedirect("index.jsp");
        }
        redirect = true;  // ✅ Mark redirection
    }

    if (!redirect) {
        response.sendRedirect("DynamicDownlo.jsp");
        redirect = true;
    }
%>

<% if (!redirect) { %>  <!-- ✅ Only display HTML when no redirect -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Multi-Threaded File Downloader</title>
<style>
body {
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
    text-align: center;
    margin-top: 100px;
    position: relative;
}

.container {
    width: 50%;
    margin: auto;
    background: white;
    padding: 20px;
    box-shadow: 0px 0px 10px gray;
    border-radius: 8px;
    opacity: 0.4;
    pointer-events: none;
    backdrop-filter: blur(5px);
}

input, button {
    width: 90%;
    padding: 10px;
    margin: 10px 0;
    border: 1px solid #ddd;
    border-radius: 5px;
}

button {
    background: #28a745;
    color: white;
    cursor: pointer;
}

button:hover {
    background: #218838;
}

.login-button {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 1;
    width: 200px;
    height: 50px;
    font-size: 18px;
    font-weight: bold;
}
.login-button, a {
    text-decoration: none;
    color: white;
}
</style>
</head>
<body>

<div class="container">
    <h2>Multi-Threaded File Downloader</h2>
    <form id="downloadForm" action="DownloadServlet" method="post">
        <label for="fileUrl">Enter File URL:</label>
        <input type="text" id="fileUrl" name="fileUrl" required>
        
        <label for="numSegments">Enter Number of Segments:</label>
        <input type="number" id="numSegments" name="numSegments" required min="1">
        
        <label for="outputFile">Enter Output File Name:</label>
        <input type="text" id="outputFile" name="outputFile" required>
        
        <button type="submit">Download</button>
    </form>
    <div id="status"></div>
</div>

<button type="button" class="login-button"><a href="login.jsp"> Log In </a></button>

</body>
</html>
<% } %>  <!-- ✅ Close conditional block -->
