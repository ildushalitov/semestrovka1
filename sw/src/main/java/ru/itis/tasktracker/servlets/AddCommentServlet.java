package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.services.CommentService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/add-comment")

public class AddCommentServlet extends HttpServlet {
    CommentService commentService;

    @Override

    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        commentService = (CommentService) servletContext.getAttribute("commentService");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskId = req.getParameter("task_id");
        req.setAttribute("task_id", taskId);

        req.getRequestDispatcher("add-comment.ftl").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        String content = req.getParameter("content");
        String taskId = req.getParameter("task_id");
        UserDto user = ((UserDto) session.getAttribute("user"));
        try {
            commentService.save(content, user, taskId);
        } catch (TaskTrackerException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("create-project.ftl").forward(req, resp);
            return;
        }
        resp.sendRedirect("comments?task_id=" + taskId);
    }
}
