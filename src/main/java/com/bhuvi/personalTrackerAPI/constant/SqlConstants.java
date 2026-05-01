package com.bhuvi.personalTrackerAPI.constant;

public class SqlConstants {

    // ==================== User SQL ====================

    public static final String INSERT_USER =
        "INSERT INTO users (userName, passwordHash, mailId, isActive, createdDate, lud) " +
        "VALUES (?, ?, ?, 'Y', NOW(3), NOW(3))";

    public static final String SELECT_USER_BY_USERNAME =
        "SELECT userId, userName, mailId, isActive, createdDate, lud FROM users WHERE userName = ?";

    public static final String SELECT_USER_BY_MAILID =
            "SELECT userId, userName, passwordHash, mailId, isActive, createdDate, lud FROM users WHERE mailId = ?";


    public static final String SELECT_USER_BY_ID =
        "SELECT userId, userName, mailId, isActive, createdDate, lud FROM users WHERE userId = ?";

    public static final String SELECT_ALL_USERS =
        "SELECT userId, userName, mailId, isActive, createdDate, lud FROM users";

    public static final String UPDATE_USER =
        "UPDATE users SET mailId = ?, isActive = ?, lud = NOW(3) WHERE userId = ?";

    public static final String DELETE_USER =
        "DELETE FROM users WHERE userId = ?";

    // ==================== Goal SQL ====================

    public static final String INSERT_GOAL =
        "INSERT INTO goals (goalName, goalDescription, frequency, target, unit, createdBy, createdAt, lud, isActive) " +
        "VALUES (?, ?, ?, ?, ?, ?, NOW(3), NOW(3), 'Y')";

    public static final String SELECT_GOAL_BY_ID =
        "SELECT goalId, goalName, goalDescription, frequency, target, unit, createdBy, createdAt, lud, isActive FROM goals WHERE goalId = ?";

    public static final String SELECT_GOALS_BY_USER =
        "SELECT goalId, goalName, goalDescription, frequency, target, unit, createdBy, createdAt, lud, isActive FROM goals WHERE createdBy = ? AND isActive = ?";

    public static final String SELECT_ALL_GOALS =
        "SELECT goalId, goalName, goalDescription, frequency, target, unit, createdBy, createdAt, lud, isActive FROM goals";

    public static final String UPDATE_GOAL =
        "UPDATE goals SET goalName = ?, goalDescription = ?, frequency = ?, target = ?, unit = ?, isActive = ?, lud = NOW(3) WHERE goalId = ?";

    public static final String DELETE_GOAL =
        "DELETE FROM goals WHERE goalId = ?";

    // ==================== Goal Log SQL ====================

    public static final String INSERT_GOAL_LOG =
        "INSERT INTO goalLogs (goalId, achieved, completedOn, loggedAt) " +
        "VALUES (?, ?, ?, NOW(3))";

    public static final String SELECT_GOAL_LOG_BY_ID =
        "SELECT logId, goalId, achieved, completedOn, loggedAt FROM goalLogs WHERE logId = ?";

    public static final String SELECT_GOAL_LOGS_BY_GOAL =
        "SELECT logId, goalId, achieved, completedOn, loggedAt FROM goalLogs WHERE goalId = ? ORDER BY completedOn DESC";

    public static final String SELECT_GOAL_LOGS_BY_GOAL_AND_DATE =
        "SELECT logId, goalId, achieved, completedOn, loggedAt FROM goalLogs WHERE goalId = ? AND completedOn = ?";

    public static final String SELECT_ALL_GOAL_LOGS =
        "SELECT logId, goalId, achieved, completedOn, loggedAt FROM goalLogs";

    public static final String UPDATE_GOAL_LOG =
        "UPDATE goalLogs SET achieved = ?, completedOn = ? WHERE logId = ?";

    public static final String DELETE_GOAL_LOG =
        "DELETE FROM goalLogs WHERE logId = ?";
}



