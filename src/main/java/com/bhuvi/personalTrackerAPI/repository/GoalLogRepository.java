package com.bhuvi.personalTrackerAPI.repository;

import com.bhuvi.personalTrackerAPI.constant.SqlConstants;
import com.bhuvi.personalTrackerAPI.entity.GoalLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GoalLogRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<GoalLog> goalLogRowMapper = (rs, rowNum) -> {
        GoalLog goalLog = new GoalLog();
        goalLog.setLogId(rs.getInt("logId"));
        goalLog.setGoalId(rs.getInt("goalId"));
        goalLog.setAchieved(rs.getObject("achieved") != null ? rs.getInt("achieved") : null);
        goalLog.setCompletedOn(rs.getDate("completedOn").toLocalDate());
        goalLog.setLoggedAt(rs.getTimestamp("loggedAt").toLocalDateTime());
        return goalLog;
    };

    public GoalLog save(GoalLog goalLog) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws java.sql.SQLException {
                    PreparedStatement ps = con.prepareStatement(SqlConstants.INSERT_GOAL_LOG, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, goalLog.getGoalId());
                    ps.setObject(2, goalLog.getAchieved());
                    ps.setDate(3, Date.valueOf(goalLog.getCompletedOn()));
                    return ps;
                }
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                goalLog.setLogId(key.intValue());
            }
            return goalLog;
        } catch (Exception e) {
            throw new RuntimeException("Error saving goal log: " + e.getMessage());
        }
    }

    public Optional<GoalLog> findById(Integer logId) {
        try {
            List<GoalLog> goalLogs = jdbcTemplate.query(SqlConstants.SELECT_GOAL_LOG_BY_ID, goalLogRowMapper, logId);
            return goalLogs.isEmpty() ? Optional.empty() : Optional.of(goalLogs.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<GoalLog> findByGoalId(Integer goalId) {
        try {
            return jdbcTemplate.query(SqlConstants.SELECT_GOAL_LOGS_BY_GOAL, goalLogRowMapper, goalId);
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<GoalLog> findByGoalIdAndCompletedOn(Integer goalId, LocalDate completedOn) {
        try {
            return jdbcTemplate.query(SqlConstants.SELECT_GOAL_LOGS_BY_GOAL_AND_DATE, goalLogRowMapper, goalId, Date.valueOf(completedOn));
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<GoalLog> findAll() {
        try {
            return jdbcTemplate.query(SqlConstants.SELECT_ALL_GOAL_LOGS, goalLogRowMapper);
        } catch (Exception e) {
            return List.of();
        }
    }

    public int update(GoalLog goalLog) {
        try {
            return jdbcTemplate.update(SqlConstants.UPDATE_GOAL_LOG,
                    goalLog.getAchieved(),
                    goalLog.getCompletedOn(),
                    goalLog.getLogId());
        } catch (Exception e) {
            throw new RuntimeException("Error updating goal log: " + e.getMessage());
        }
    }

    public int delete(Integer logId) {
        try {
            return jdbcTemplate.update(SqlConstants.DELETE_GOAL_LOG, logId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting goal log: " + e.getMessage());
        }
    }
}




