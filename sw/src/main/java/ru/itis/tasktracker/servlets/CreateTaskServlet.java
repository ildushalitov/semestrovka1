package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.*;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.services.TaskService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/create-task")

public class CreateTaskServlet extends HttpServlet {
    TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        taskService = (TaskService) servletContext.getAttribute("taskService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("create-task.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskForm form = TaskForm.builder()
                .name(req.getParameter("name"))
                .description(req.getParameter("description"))
                .deadline(req.getParameter("deadline"))
                .status(req.getParameter("status"))
                .build();
        TaskDto task;
        try {
            task = taskService.save(form, Integer.parseInt(req.getParameter("project_id")));
        } catch (TaskTrackerException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("create-task.ftl?project_id=" + req.getParameter("project_id")).forward(req, resp);
            return;
        }
        resp.sendRedirect("project?id=" + req.getParameter("project_id"));
    }

}
