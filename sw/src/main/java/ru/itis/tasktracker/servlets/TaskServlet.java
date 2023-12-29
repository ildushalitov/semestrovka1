package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.TaskDto;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.services.MembershipService;
import ru.itis.tasktracker.services.ProjectService;
import ru.itis.tasktracker.services.TaskService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;
    private MembershipService membershipService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        taskService = (TaskService) servletContext.getAttribute("taskService");
        membershipService = (MembershipService) servletContext.getAttribute(("membershipService"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskId = req.getParameter("id");
        TaskDto task  = taskService.getTask(taskId);
        req.setAttribute("task", task);
        String date = task.getCreatedAt().toString().substring(0,10);
        String time = task.getCreatedAt().toString().substring(11,16);
        req.setAttribute("date",date);
        req.setAttribute("time",time);
        Integer projectId = task.getProjectId();
        Object user = req.getSession().getAttribute("user");
        boolean isAdmin = membershipService.isAdmin(projectId.toString(), ((UserDto) user).getId().toString());
        req.setAttribute("isAdmin", isAdmin);
        req.getRequestDispatcher("task.ftl").forward(req, resp);
    }
}
