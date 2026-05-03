package com.bhuvi.personalTrackerAPI.repository;

import com.bhuvi.personalTrackerAPI.constant.SqlConstants;
import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.service.HashingService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final HashingService hashingService;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("userId"));
        user.setMailId(rs.getString("mailId"));
        user.setUserName(rs.getString("userName"));
        user.setPassword(rs.getString("passwordHash"));
        user.setIsActive(rs.getString("isActive").charAt(0));
        user.setCreatedDate(rs.getTimestamp("createdDate").toLocalDateTime());
        user.setLud(rs.getTimestamp("lud").toLocalDateTime());
        return user;
    };

    public User save(User user) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(SqlConstants.INSERT_USER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUserName());
                ps.setString(2, hashingService.hashPassword(user.getPassword()));
                ps.setString(3, user.getMailId());
                ps.setString(4, String.valueOf(user.getIsActive()));
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                user.setUserId(key.intValue());
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    public Optional<User> findByUserName(String userName) {
        try {
            List<User> users = jdbcTemplate.query(SqlConstants.SELECT_USER_BY_USERNAME, userRowMapper, userName);
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public User findByMailId(String mailId) {
        try {
            List<User> users = jdbcTemplate.query(SqlConstants.SELECT_USER_BY_MAILID, userRowMapper, mailId);
            return users.isEmpty() ? null : users.get(0);
        }  catch (Exception e) {
            return null;
        }
    }

    public User findByVerifiedMailId(String mailId) {
        try {
            List<User> users = jdbcTemplate.query(SqlConstants.SELECT_USER_BY_VERIFIED_MAILID, userRowMapper, mailId);
            return users.isEmpty() ? null : users.get(0);
        }  catch (Exception e) {
            return null;
        }
    }

    public boolean validatePassword(String rawPassword, String hashedPassword) {
        return hashingService.validatePassword(rawPassword, hashedPassword);
    }

    public Optional<User> findById(Integer userId) {
        try {
            List<User> users = jdbcTemplate.query(SqlConstants.SELECT_USER_BY_ID, userRowMapper, userId);
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        try {
            return jdbcTemplate.query(SqlConstants.SELECT_ALL_USERS, userRowMapper);
        } catch (Exception e) {
            return List.of();
        }
    }

    public int update(User user) {
        try {
            return jdbcTemplate.update(SqlConstants.UPDATE_USER,
                    user.getUserName(),
                    user.getPassword(),
                    user.getMailId(),
                    user.getIsActive().toString(),
                    user.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }

    public int delete(Integer userId) {
        try {
            return jdbcTemplate.update(SqlConstants.DELETE_USER, userId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }
}




