-- Banking Application Database Setup
-- Run this script in MySQL to create the database

-- Create database
CREATE DATABASE IF NOT EXISTS bankdb;

-- Use the database
USE bankdb;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_number INT PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL,
    email VARCHAR(100) NOT NULL,
    account_type VARCHAR(20) NOT NULL
);

-- Display message
SELECT 'Database setup completed successfully!' AS Message;
