package ru.itis.tasktracker.servlets;
import ru.itis.tasktracker.services.TaskService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete-task")

public class DeleteTaskServlet extends HttpServlet {
    TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        taskService = (TaskService) servletContext.getAttribute("taskService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskId = req.getParameter("id");
        String projectId = req.getParameter("project_id");
        taskService.delete(taskId);
        resp.sendRedirect("project?id="+projectId);
    }
}
