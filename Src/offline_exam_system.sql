-- =======================================================================
-- DATABASE CREATION SCRIPT: offline_exam_system
-- =======================================================================

-- Create schema/user for Oracle (optional depending on setup)
CREATE USER offline_exam_system IDENTIFIED BY exam123;
GRANT CONNECT, RESOURCE TO offline_exam_system;
ALTER USER offline_exam_system DEFAULT ROLE ALL;

-- Switch to schema
ALTER SESSION SET CURRENT_SCHEMA = offline_exam_system;

-- =======================================================================
-- TABLES, SEQUENCES, AND TRIGGERS
-- =======================================================================

-- USERS TABLE
CREATE TABLE USERS (
    user_id VARCHAR2(20) PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) CHECK (role IN ('ADMIN','TEACHER','STUDENT')),
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP
);
