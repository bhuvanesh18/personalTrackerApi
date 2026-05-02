package com.bhuvi.personalTrackerAPI.repository;

import com.bhuvi.personalTrackerAPI.constant.SqlConstants;
import com.bhuvi.personalTrackerAPI.entity.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GoalRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Goal> goalRowMapper = (rs, rowNum) -> {
        Goal goal = new Goal();
        goal.setGoalId(rs.getInt("goalId"));
        goal.setGoalName(rs.getString("goalName"));
        goal.setGoalDescription(rs.getString("goalDescription"));
        goal.setFrequency(Goal.Frequency.valueOf(rs.getString("frequency")));
        goal.setTarget(rs.getObject("target") != null ? rs.getInt("target") : null);
        goal.setUnit(Goal.Unit.valueOf(rs.getString("unit")));
        goal.setCreatedBy(rs.getInt("createdBy"));
        goal.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        goal.setLud(rs.getTimestamp("lud").toLocalDateTime());
        goal.setIsActive(rs.getString("isActive").charAt(0));
        return goal;
    };

    public Goal save(Goal goal) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws java.sql.SQLException {
                    PreparedStatement ps = con.prepareStatement(SqlConstants.INSERT_GOAL, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, goal.getGoalName());
                    ps.setString(2, goal.getGoalDescription());
                    ps.setString(3, goal.getFrequency().toString());
                    ps.setObject(4, goal.getTarget());
                    ps.setString(5, goal.getUnit().toString());
                    ps.setInt(6, goal.getCreatedBy());
                    return ps;
                }
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                goal.setGoalId(key.intValue());
            }
            return goal;
        } catch (Exception e) {
            throw new RuntimeException("Error saving goal: " + e.getMessage());
        }
    }

    public Optional<Goal> findById(Integer goalId) {
        try {
            List<Goal> goals = jdbcTemplate.query(SqlConstants.SELECT_GOAL_BY_ID, goalRowMapper, goalId);
            return goals.isEmpty() ? Optional.empty() : Optional.of(goals.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Goal> findByCreatedBy(Integer createdBy) {
        try {
            return jdbcTemplate.query(SqlConstants.SELECT_GOALS_BY_USER, goalRowMapper, createdBy, "Y");
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Goal> findByCreatedByAndIsActive(Integer createdBy, Character isActive) {
        try {
            return jdbcTemplate.query(SqlConstants.SELECT_GOALS_BY_USER, goalRowMapper, createdBy, isActive.toString());
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Goal> findAll() {
        try {
            return jdbcTemplate.query(SqlConstants.SELECT_ALL_GOALS, goalRowMapper);
        } catch (Exception e) {
            return List.of();
        }
    }

    public int update(Goal goal) {
        try {
            return jdbcTemplate.update(SqlConstants.UPDATE_GOAL,
                    goal.getGoalName(),
                    goal.getGoalDescription(),
                    goal.getFrequency().toString(),
                    goal.getTarget(),
                    goal.getUnit().toString(),
                    goal.getIsActive(),
                    goal.getGoalId());
        } catch (Exception e) {
            throw new RuntimeException("Error updating goal: " + e.getMessage());
        }
    }

    public int delete(Integer goalId) {
        try {
            return jdbcTemplate.update(SqlConstants.DELETE_GOAL, goalId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting goal: " + e.getMessage());
        }
    }
}




