package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.services.AuthorizationService;
import ru.itis.tasktracker.dto.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {
    private AuthorizationService authorizationService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("sign-up.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        SignUpForm form = SignUpForm.builder()
                .fistName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();
        UserDto user;
        try {
            user = authorizationService.signUp(form);
        } catch (TaskTrackerException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("sign-up.ftl").forward(req, resp);
            return;
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        resp.sendRedirect("profile");
    }
}
