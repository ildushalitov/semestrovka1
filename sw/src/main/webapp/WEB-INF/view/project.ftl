<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Project Information</title>
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

        .tasks {
            margin-top: 20px;
        }

        .task-link {
            display: block;
            padding: 5px 0;
            text-decoration: none;
            color: #333;
            transition: color 0.3s ease;
        }

        .task-link:hover {
            color: #007bff;
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
            max-height: 70vh;
            overflow-y: auto;
            width: 600px;
        }

        .project-info {
            margin-bottom: 10px;
        }

        .description {
            text-align: center;
            word-wrap: break-word;
            max-width: 95%;
        }

        .delete-button {
            margin-top: 10px;
            padding: 5px 10px;
            border: none;
            border-radius: 3px;
            background-color: #dc3545;
            color: white;
            cursor: pointer;
        }

        .back-button {
            margin-top: 10px;
            padding: 5px 10px;
            border: none;
            border-radius: 3px;
            background-color: #007bff;
            color: white;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="container">
    <#include "menu.ftl">
    <div class="title"><strong>Project Information</strong></div>
    <div class="white-container">
        <div class="project-info">
            <strong>Project Name:</strong> ${project.name}
        </div>
        <div class="description">
            <strong>Description:</strong> ${project.description}
        </div>
        <br>
        <div class="tasks">
            <ul>
                <div class="tasks">
                    <strong>Tasks:</strong>
                    <ul>
                        <#list tasks as task>
                            <a href="task?id=${task.id}" class="task-link">${task.name}</a><br>
                        </#list>
                    </ul>
                </div>
            </ul>
        </div>
        <br><br>

        <form action="delete-project" method="post">
            <input type="hidden" name="projectId" value="${project.id}">
            <#if isAdmin>
                <button type="submit" class="delete-button">Delete project</button>
            </#if>
            <br>
        </form>
        <br>
        <#if isAdmin>
            <button onclick="goToUpdatePage()">Update Project</button>
            <br>
            <br>
            <button onclick="goToCreateTaskPage()">Create Task</button>
            <br> <br>
            <button onclick="goToMembersPage()">Members</button>
            <br>
        </#if>
        <br>
        <br>
        <button class="back-button" onclick="goBack()">Back to Projects</button>
    </div>
</div>
<script>
    function goToMembersPage(){
        window.location.href = "members?project_id=${project.id}"
    }
    function goToCreateTaskPage() {
        window.location.href = `create-task?project_id=${project.id}`;
    }
    function goToUpdatePage() {
        window.location.href = `update-project?id=${project.id}`;
    }
    function goBack() {
        window.location.href = 'my-projects';
    }
</script>

</body>
</html>
