import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.tasktracker.model.Project;
import ru.itis.tasktracker.repositories.UserRepository;
import ru.itis.tasktracker.repositories.impl.ProjectRepositoryJdbcImpl;
import ru.itis.tasktracker.repositories.impl.TaskRepositoryJdbcImpl;
import ru.itis.tasktracker.repositories.impl.UserRepositoryJdbcImpl;

import java.time.LocalDateTime;

public class Main {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Mqw333ert";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/TaskTracker";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    public static void main(String[] args) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(DB_URL);
        hikariConfig.setUsername(DB_USERNAME);
        hikariConfig.setPassword(DB_PASSWORD);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        UserRepository userRepository  = new UserRepositoryJdbcImpl(dataSource);
        System.out.println(userRepository.isAdmin(2,1));

    }
}
