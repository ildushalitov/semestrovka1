package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.services.AuthorizationService;
import ru.itis.tasktracker.services.ProjectService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/my-projects")
public class MyProjectsServlet extends HttpServlet {

    private ProjectService projectService;



    @Override
    public void init(ServletConfig config) throws ServletException {
        projectService = (ProjectService) config.getServletContext().getAttribute("projectService");

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object user = req.getSession().getAttribute("user");
        Object projects = projectService.getUsersProjects((UserDto)user);
        req.setAttribute("projects", projects);
        req.getRequestDispatcher("my-projects.ftl").forward(req, resp);
    }

}
