package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.TaskDto;
import ru.itis.tasktracker.dto.TaskForm;
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

@WebServlet("/update-task")

public class UpdateTaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        taskService = (TaskService) servletContext.getAttribute("taskService");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskId = req.getParameter("id");
        req.setAttribute("task", taskService.getTask(taskId));
        req.getRequestDispatcher("update-task.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskId = req.getParameter("id");
        TaskDto task = taskService.getTask(taskId);
        TaskForm form = TaskForm.builder()
                .id(task.getId())
                .createdAt(task.getCreatedAt().toString())
                .projectId(task.getProjectId())
                .name(req.getParameter("name"))
                .description(req.getParameter("description"))
                .status(req.getParameter("status"))
                .deadline(req.getParameter("deadline"))
                .build();
        try {
            taskService.update(form);
        } catch (TaskTrackerException e) {
            req.setAttribute("task", taskService.getTask(taskId));
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("update-task.ftl?id=" + req.getParameter("id")).forward(req, resp);
            return;
        }
        resp.sendRedirect("task?id=" + taskId);
    }
}
