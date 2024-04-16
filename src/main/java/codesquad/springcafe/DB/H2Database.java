package codesquad.springcafe.DB;

import codesquad.springcafe.domain.Article;
import codesquad.springcafe.domain.User;
import codesquad.springcafe.dto.RegisterArticle;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class H2Database {
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<Article> articleRowMapper = (resultSet, rowNum) -> {
        Article article = new Article(
                resultSet.getString("writer"),
                resultSet.getString("title"),
                resultSet.getString("contents"),
                resultSet.getTimestamp("time").toLocalDateTime(),
                resultSet.getLong("id")
        );
        return article;
    };
    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        User user = new User(
                resultSet.getString("userId"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
        return user;
    };

    @Autowired
    public H2Database(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO USERS (userId, name, email, password) VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS", userRowMapper);
    }

    public User getUser(String id) {
        final String SELECT_USER = "SELECT * FROM USERS WHERE userId= ?";
        return jdbcTemplate.queryForObject(SELECT_USER, userRowMapper, id);
    }

    public void updateUser(String password, String name, String email, String userId) {
        final String UPDATE_USER = "UPDATE Users SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(UPDATE_USER, password, name, email, userId);
    }

    public void addArticle(RegisterArticle registerArticle) {
        jdbcTemplate.update("INSERT INTO Articles (writer, title, contents, time) VALUES (?,?,?,?)",
                registerArticle.getWriter(), registerArticle.getTitle(), registerArticle.getContents(), registerArticle.getTime());
    }

    public List<Article> getAllArticles() {
        return jdbcTemplate.query("SELECT * FROM Articles", articleRowMapper);
    }

    public Article getArticle(long id) {
        final String SELECT_ARTICLE = "SELECT * FROM Articles WHERE id= ?";
        return jdbcTemplate.queryForObject(SELECT_ARTICLE, articleRowMapper, id);
    }
}