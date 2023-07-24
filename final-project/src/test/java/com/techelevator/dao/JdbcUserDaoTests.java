package com.techelevator.dao;

import com.techelevator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests {
    protected static final User USER_1 = new User(1, "user1", "user1", "ROLE_USER", "User1 Name", "User1 address", "Cleveland", "OH", "44123");
    protected static final User USER_2 = new User(2, "user2", "user2", "ROLE_USER", "User2 Name", null, "Beverly Hills", "CA", "90210");
    private static final User USER_3 = new User(3, "user3", "user3", "ROLE_USER", "User3 Name", "User3 address", "Chicago", "IL", "60609");

    private JdbcUserDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcUserDao(jdbcTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIdByUsername_given_null_throws_exception() {
        dao.findIdByUsername(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findIdByUsername_given_invalid_username_throws_exception() {
        dao.findIdByUsername("invalid");
    }

    @Test
    public void findIdByUsername_given_valid_user_returns_user_id() {
        int actualUserId = dao.findIdByUsername(USER_1.getUsername());

        Assert.assertEquals(USER_1.getId(), actualUserId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByUsername_given_null_throws_exception() {
        dao.findByUsername(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_given_invalid_username_throws_exception() {
        dao.findByUsername("invalid");
    }

    @Test
    public void findByUsername_given_valid_user_returns_user() {
        User actualUser = dao.findByUsername(USER_1.getUsername());

        Assert.assertEquals(USER_1, actualUser);
    }

    @Test
    public void getUserById_given_invalid_user_id_returns_null() {
        User user = dao.getUserById(-1);
        Assert.assertNull("Call to getUserById with invalid id should return NULL", user);
    }

    @Test
    public void getUserById_given_valid_user_id_returns_user() {
        User actualUser = dao.getUserById(USER_1.getId());
        Assert.assertEquals(USER_1, actualUser);
    }

    @Test
    public void findAll_returns_all_users() {
        List<User> users = dao.findAll();

        Assert.assertNotNull(users);
        Assert.assertEquals(3, users.size());
        Assert.assertEquals(USER_1, users.get(0));
        Assert.assertEquals(USER_2, users.get(1));
        Assert.assertEquals(USER_3, users.get(2));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_user_with_null_username() {
        dao.create(
            new User(null, USER_3.getPassword(), "ROLE_USER", USER_3.getName(), USER_3.getAddress(), USER_3.getCity(), USER_3.getStateCode(), USER_3.getZIP())
        );
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_user_with_existing_username() {
        dao.create(
                new User(USER_1.getUsername(), USER_3.getPassword(), "ROLE_USER", USER_3.getName(), USER_3.getAddress(), USER_3.getCity(), USER_3.getStateCode(), USER_3.getZIP())
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_user_with_null_password() {
        dao.create(
                new User(USER_3.getUsername(), null, "ROLE_USER", USER_3.getName(), USER_3.getAddress(), USER_3.getCity(), USER_3.getStateCode(), USER_3.getZIP())
        );
    }

    @Test
    public void create_user_creates_a_user() {
        User newUser = new User("new", "user", "ROLE_USER", "new user", "new user address", "city", "ST", "ZIP");

        User user = dao.create(newUser);
        Assert.assertNotNull("Call to create should return non-null user", user);

        User actualUser = dao.getUserById(user.getId());
        Assert.assertNotNull("Call to getUserById after call to create should return non-null user", actualUser);

        newUser.setId(actualUser.getId());
        actualUser.setPassword(newUser.getPassword()); // reset password back to unhashed password for testing
        Assert.assertEquals(newUser, actualUser);
    }
}
