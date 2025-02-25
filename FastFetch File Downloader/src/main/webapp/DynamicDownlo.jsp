<%@page import="User.AESUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String userEmail = null;

// Retrieve cookies
Cookie[] cookies = request.getCookies();
if (cookies != null) {
    for (Cookie cookie : cookies) {
        if (cookie.getName().equals("userEmail")) {
            userEmail = cookie.getValue();
        }
    }
}
if (userEmail == null) {
    response.getWriter().println("No user logged in.");
    return;
}

%>
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
    margin: 0;
}
/* Header Styles */
.header {
    background: #333;
    color: white;
    padding: 10px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.profile {
    position: relative;
    cursor: pointer;
    display: flex;
    align-items: center;
}
.profile img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 10px;
}
.profile-menu {
    display: none;
    position: absolute;
    top: 50px;
    right: 0;
    background: white;
    box-shadow: 0px 0px 5px gray;
    border-radius: 5px;
    padding: 10px;
    text-align: left;
}
.profile:hover .profile-menu {
    display: block;
}
.profile-menu a{
    text-decoration: none;
    color: black;
    display: block;
    padding: 5px;
}
p {
    color: black; /* Ensure text is visible */
    display: block; /* Ensure it is not hidden */
}
.logout-btn {
    background: #d9534f;
    color: white;
    border: none;
    padding: 5px 10px;
    cursor: pointer;
    border-radius: 3px;
}
.logout-btn:hover {
    background: #c9302c;
}

/* Form Styles */
.container {
    width: 50%;
    margin: auto;
    background: white;
    padding: 20px;
    box-shadow: 0px 0px 10px gray;
    border-radius: 8px;
    margin-top: 20px;
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
</style>
<script>
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("downloadForm").addEventListener("submit", function(event) {
        let fileUrl = document.getElementById("fileUrl").value;
        let numSegments = document.getElementById("numSegments").value;
        
        if (fileUrl.trim() === "" || numSegments <= 0) {
            event.preventDefault();
            alert("Please enter a valid URL and number of segments.");
        }
    });

    document.getElementById("logoutBtn").addEventListener("click", function() {
        window.location.href = "LogoutServlet"; 
    });
});
</script>
</head>
<body>
<!-- Header -->
<div class="header">
    <h2>File Downloader</h2>
    <div class="profile">
        <img src="images/user.png" alt="Profile">
        <div class="profile-menu">
            <p><b><%= AESUtil.decrypt(userEmail) %></b> </p>
            <form action="History" method="get">
            <button class="logout-btn">Download History</button>
            </form>
            <button id="logoutBtn" class="logout-btn">Logout</button>
        </div>
    </div>
</div>
<!-- Form -->
<div class="container">
    <h2>Multi-Threaded File Downloader</h2>
    <form id="downloadForm" action="DownloadServlet" method="post">
        <label for="fileUrl">Enter File URL:</label>
        <input type="text" id="fileUrl" name="fileUrl" required>
        <br>
        <label for="numSegments">Enter Number of Segments:</label>
        <input type="number" id="numSegments" name="numSegments" required min="1">
         <br>
        <label for="outputFile">Enter Output File Name:</label>
        <input type="text" id="outputFile" name="outputFile" required>
        <button type="submit">Download</button>
    </form>
</div>
</body>
</html>