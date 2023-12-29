package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.services.CommentService;
import ru.itis.tasktracker.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/comments")

public class CommentsServlet extends HttpServlet {
    CommentService commentService;
    UserService userService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();

        userService = (UserService) servletContext.getAttribute("userService");

        commentService = (CommentService) servletContext.getAttribute("commentService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskId = req.getParameter("task_id");
        req.setAttribute("task_id",taskId);
        req.setAttribute("comments", commentService.getCommentsByTask(taskId));
        req.getRequestDispatcher("comments.ftl").forward(req, resp);
    }
}
