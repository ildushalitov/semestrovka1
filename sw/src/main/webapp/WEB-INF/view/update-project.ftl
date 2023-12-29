<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Project</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            text-align: center;
        }
        .title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .white-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input[type="text"],
        .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 3px;
        }
        .submit-button {
            padding: 10px 20px;
            background-color: #007bff;
            border: none;
            border-radius: 3px;
            color: #fff;
            font-weight: bold;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="container">
    <#include "menu.ftl">
    <h1 class="title">Update Project</h1>
    <#if errorMessage??>
        <div class="error_message">${errorMessage}</div>
    </#if>
    <div class="white-container">
        <form action="update-project" method="post">
            <div class="form-group">
                <label for="name">Project Name:</label>
                <input type="text" id="name" name="name" value="${project.name}" required>
            </div>
            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="4" required>${project.description}</textarea>
            </div>
            <input type="hidden" name="id" value="${project.id}">
            <button type="submit" class="submit-button">Update Project</button>
            <br>
            <br>
            <br>
            <button onclick="goToProjectPage()">Back to project</button>
        </form>
    </div>
</div>
<script>
    function goToProjectPage(){
        window.location.href ='project?project_id=${project.id}';
    }
</script>
</body>
</html>
