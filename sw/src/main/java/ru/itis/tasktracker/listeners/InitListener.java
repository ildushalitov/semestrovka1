package ru.itis.tasktracker.listeners;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.tasktracker.model.Project;
import ru.itis.tasktracker.repositories.CommentRepository;
import ru.itis.tasktracker.repositories.ProjectRepository;
import ru.itis.tasktracker.repositories.TaskRepository;
import ru.itis.tasktracker.repositories.UserRepository;
import ru.itis.tasktracker.repositories.impl.CommentRepositoryJdbcImpl;
import ru.itis.tasktracker.repositories.impl.ProjectRepositoryJdbcImpl;
import ru.itis.tasktracker.repositories.impl.TaskRepositoryJdbcImpl;
import ru.itis.tasktracker.repositories.impl.UserRepositoryJdbcImpl;
import ru.itis.tasktracker.services.*;
import ru.itis.tasktracker.services.impl.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener

public class InitListener implements ServletContextListener {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Mqw333ert";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/TaskTracker";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Postgresql Driver not found.");
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(DB_URL);
        hikariConfig.setUsername(DB_USERNAME);
        hikariConfig.setPassword(DB_PASSWORD);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        PasswordEncoder passwordEncoder = new PasswordEncoderImpl();
        UserMapper userMapper = new UserMapperImpl();
        CommentRepository commentRepository = new CommentRepositoryJdbcImpl(dataSource);
        TaskRepository taskRepository = new TaskRepositoryJdbcImpl(dataSource);
        ProjectRepository projectRepository = new ProjectRepositoryJdbcImpl(dataSource);
        UserRepository userRepository = new UserRepositoryJdbcImpl(dataSource);
        AuthorizationService authorizationService = new AuthorizationServiceImpl(userRepository, userMapper, passwordEncoder);
        ProjectMapper projectMapper = new ProjectMapperImpl();
        ProjectService projectService =  new ProjectServiceImpl(projectRepository, projectMapper);
        TaskMapper taskMapper = new TaskMapperImpl();
        TaskService taskService = new TaskServiceImpl(taskRepository,taskMapper);
        MembershipService membershipService = new MembershipServiceImpl(userRepository, projectService);
        CommentMapper commentMapper = new CommentMapperImpl();
        CommentService commentService = new CommentServiceImpl(userRepository, commentRepository, commentMapper);
        UserService userService = new UserServiceImpl(userRepository);

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("taskService", taskService);
        servletContext.setAttribute("userRepository", userRepository);
        servletContext.setAttribute("taskRepository", taskRepository);
        servletContext.setAttribute("projectRepository", projectRepository);
        servletContext.setAttribute("commentRepository", commentRepository);
        servletContext.setAttribute("authorizationService", authorizationService);
        servletContext.setAttribute("projectService", projectService);
        servletContext.setAttribute("membershipService", membershipService);
        servletContext.setAttribute("commentService", commentService);
        servletContext.setAttribute("userService", userService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
