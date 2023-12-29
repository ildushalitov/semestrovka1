<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Task Information</title>
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
            max-height: 70vh;
            overflow-y: auto;
            width: 600px;
        }

        .task-info {
            margin-bottom: 10px;
        }

        .description {
            text-align: center;
            word-wrap: break-word;
            max-width: 95%;
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
    <div class="title"><strong>Task Information</strong></div>
    <div class="white-container">
        <div class="task-info">
            <strong>Task Name:</strong> ${task.name}
        </div>
        <div class="description">
            <strong>Description:</strong> ${task.description}
        </div>
        <div class="task-info">
            <strong>Deadline:</strong> ${date}  ${time}
        </div>
        <div class="task-info">
            <strong>Status:</strong> ${task.status}
        </div>
        <br>
        <#if isAdmin>
        <button onclick="goToUpdatePage()">Update</button>
        <br>
        <br>
        <form action="delete-task" method="post">
            <input type="hidden" name="project_id" value="${task.projectId}">
            <input type="hidden" name="id" value="${task.id}">
            <button type="submit" class="delete-button">Delete task</button>
            <br>
        </form>
        <br>
        </#if>
        <button onclick="goToCommentsPage()">Show comments</button>
        <br>
        <button class="back-button" onclick="goBack()">Back to Project</button>
    </div>
</div>
<script>
    function goBack() {
        window.location.href = 'project?id=${task.projectId}'
    }
    function goToUpdatePage(){
        window.location.href = 'update-task?id=${task.id}'
    }
    function goToCommentsPage(){
        window.location.href = 'comments?task_id=${task.id}'
    }
</script>
</body>
</html>