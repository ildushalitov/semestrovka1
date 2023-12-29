<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Comments</title>
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

        .comment-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-height: 70vh;
            overflow-y: auto;
            width: 600px;
            margin-bottom: 20px;
        }

        .comment {
            margin-bottom: 10px;
            border-bottom: 1px solid #ccc;
            padding-bottom: 10px;
        }

        .user-info {
            font-style: italic;
        }

        .content {
            margin-top: 5px;
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
    <div class="title"><strong>Comments</strong></div>
    <#include "menu.ftl">
    <div class="comment-container">
        <#list comments as comment>
            <div class="comment">
                <div class="content">${comment.email} : ${comment.content}</div>
            </div>
        </#list>
    </div>
    <br><br>
    <button onclick="goToAddCommentPage()">Write a comment</button>
    <br><br>
    <button class="back-button" onclick="goBackToTask()">Back</button>

</div>

</div>
<script>
    function goBackToTask(){
        window.location.href = 'task?id=${task_id}'
    }
    function goToAddCommentPage(){
        window.location.href = 'add-comment?task_id=${task_id}'
    }
</script>
</body>
</html>
