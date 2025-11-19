-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 15, 2025 at 06:46 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `offline_exam_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `course_id` int(11) NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `created_by` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`course_id`, `course_name`, `description`, `created_by`) VALUES
(1, 'database', 'database administation', 'INS-0004'),
(2, 'Operating system', 'WINDOW OS', 'INS-0005');

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments` (
  `enrollment_id` int(11) NOT NULL,
  `student_id` varchar(20) NOT NULL,
  `course_id` int(11) NOT NULL,
  `status` enum('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
  `request_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `approved_by` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `enrollments`
--

INSERT INTO `enrollments` (`enrollment_id`, `student_id`, `course_id`, `status`, `request_date`, `approved_by`) VALUES
(1, '24RP0-0003', 1, 'APPROVED', '2025-11-15 08:59:03', 'ADM-0002'),
(2, '24RP0-0003', 2, 'APPROVED', '2025-11-15 08:59:16', 'INS-0005');

-- --------------------------------------------------------

--
-- Table structure for table `exams`
--

CREATE TABLE `exams` (
  `exam_id` int(11) NOT NULL,
  `course_id` int(11) DEFAULT NULL,
  `exam_name` varchar(100) NOT NULL,
  `exam_date` date DEFAULT NULL,
  `duration_minutes` int(11) DEFAULT NULL,
  `created_by` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `exams`
--

INSERT INTO `exams` (`exam_id`, `course_id`, `exam_name`, `exam_date`, `duration_minutes`, `created_by`) VALUES
(4, 2, 'quiz', '2025-11-15', 60, 'INS-0005'),
(5, 2, 'LAB TEST', '2025-11-15', 15, 'INS-0005'),
(6, 2, 'small quiz', '2025-11-15', 15, 'INS-0005'),
(7, 2, 'cat', '2025-11-15', 15, 'INS-0005');

-- --------------------------------------------------------

--
-- Table structure for table `options`
--

CREATE TABLE `options` (
  `option_id` int(11) NOT NULL,
  `question_id` int(11) DEFAULT NULL,
  `option_text` varchar(255) DEFAULT NULL,
  `is_correct` char(1) DEFAULT NULL CHECK (`is_correct` in ('Y','N'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `question_id` int(11) NOT NULL,
  `exam_id` int(11) DEFAULT NULL,
  `question_text` text NOT NULL,
  `question_type` enum('MCQ','ESSAY','TRUE_FALSE') DEFAULT NULL,
  `option_a` varchar(255) DEFAULT NULL,
  `option_b` varchar(255) DEFAULT NULL,
  `option_c` varchar(255) DEFAULT NULL,
  `option_d` varchar(255) DEFAULT NULL,
  `correct_answer` varchar(50) DEFAULT NULL,
  `marks` decimal(5,2) DEFAULT NULL,
  `correct_text` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `questions`
--

INSERT INTO `questions` (`question_id`, `exam_id`, `question_text`, `question_type`, `option_a`, `option_b`, `option_c`, `option_d`, `correct_answer`, `marks`, `correct_text`) VALUES
(6, 3, 'are you rwandan?', 'MCQ', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(7, 3, 'you love me?', 'TRUE_FALSE', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(8, 3, 'give me one district in northern, rwanda', 'ESSAY', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(9, 4, 'who is president of rwanda', 'MCQ', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(10, 4, 'you love me?', 'TRUE_FALSE', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(11, 4, '1+2 =5', 'TRUE_FALSE', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(12, 5, 'Are you student of karongi?', 'TRUE_FALSE', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(13, 5, 'are you a girl?', 'TRUE_FALSE', NULL, NULL, NULL, NULL, NULL, 5.00, NULL),
(14, 7, 'are you JONAS?', 'TRUE_FALSE', 'True', 'False', '', '', 'True', 5.00, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `results`
--

CREATE TABLE `results` (
  `result_id` int(11) NOT NULL,
  `student_id` varchar(20) DEFAULT NULL,
  `exam_id` int(11) DEFAULT NULL,
  `score` decimal(5,2) DEFAULT NULL,
  `graded_by` varchar(20) DEFAULT NULL,
  `date_taken` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `results`
--

INSERT INTO `results` (`result_id`, `student_id`, `exam_id`, `score`, `graded_by`, `date_taken`) VALUES
(1, '24RP0-0003', 4, 0.00, 'INS-0005', '2025-11-15 12:42:52'),
(2, '24RP0-0003', 5, 0.00, 'INS-0005', '2025-11-15 16:13:12'),
(3, '24RP0-0003', 7, 5.00, 'INS-0005', '2025-11-15 16:41:07');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','TEACHER','STUDENT') DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `created_at`) VALUES
('24RP0-0003', 'adeline', '12345', 'STUDENT', '2025-11-12 07:36:08'),
('ADM-0002', 'admin', 'admin1234', 'ADMIN', '2025-11-12 07:32:27'),
('INS-0004', 'sabin', 'sabin', 'TEACHER', '2025-11-12 07:39:14'),
('INS-0005', 'shema', 'araphat', 'TEACHER', '2025-11-15 07:14:45');

--
-- Triggers `users`
--
DELIMITER $$
CREATE TRIGGER `trg_user_id` BEFORE INSERT ON `users` FOR EACH ROW BEGIN
    INSERT INTO user_seq VALUES (NULL);
    IF NEW.role = 'ADMIN' THEN
        SET NEW.user_id = CONCAT('ADM-', LPAD(LAST_INSERT_ID(), 4, '0'));
    ELSEIF NEW.role = 'TEACHER' THEN
        SET NEW.user_id = CONCAT('INS-', LPAD(LAST_INSERT_ID(), 4, '0'));
    ELSE
        SET NEW.user_id = CONCAT('24RP0-', LPAD(LAST_INSERT_ID(), 4, '0'));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `user_logs`
--

CREATE TABLE `user_logs` (
  `log_id` int(11) NOT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  `action` varchar(100) DEFAULT NULL,
  `log_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_logs`
--

INSERT INTO `user_logs` (`log_id`, `user_id`, `action`, `log_time`) VALUES
(1, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 12:37:40'),
(2, '24RP0-0003', 'logged in as STUDENT', '2025-11-15 12:40:36'),
(3, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 16:08:45'),
(4, 'INS-0005', 'logged in as TEACHER', '2025-11-15 16:09:54'),
(5, '24RP0-0003', 'logged in as STUDENT', '2025-11-15 16:12:24'),
(6, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 16:14:58'),
(7, 'INS-0005', 'logged in as TEACHER', '2025-11-15 16:33:11'),
(8, 'INS-0005', 'logged in as TEACHER', '2025-11-15 16:36:51'),
(9, 'INS-0005', 'logged in as TEACHER', '2025-11-15 16:37:49'),
(10, '24RP0-0003', 'logged in as STUDENT', '2025-11-15 16:40:55'),
(11, '24RP0-0003', 'logged in as STUDENT', '2025-11-15 16:46:28'),
(12, '24RP0-0003', 'logged in as STUDENT', '2025-11-15 16:50:18'),
(13, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 16:51:23'),
(14, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 16:53:27'),
(15, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 16:57:52'),
(16, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 17:12:30'),
(17, '24RP0-0003', 'logged in as STUDENT', '2025-11-15 17:19:23'),
(18, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 17:36:16'),
(19, 'INS-0005', 'logged in as TEACHER', '2025-11-15 17:37:00'),
(20, 'ADM-0002', 'logged in as ADMIN', '2025-11-15 17:44:14'),
(21, 'ADM-0002', 'created local backup file at C:\\Users\\user\\offline_exam_backups\\backup-20251115-194416.json', '2025-11-15 17:44:16'),
(22, 'ADM-0002', 'backup upload to backend failed', '2025-11-15 17:44:17');

-- --------------------------------------------------------

--
-- Table structure for table `user_seq`
--

CREATE TABLE `user_seq` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_seq`
--

INSERT INTO `user_seq` (`id`) VALUES
(1),
(2),
(3),
(4),
(5);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`course_id`),
  ADD KEY `created_by` (`created_by`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD PRIMARY KEY (`enrollment_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `approved_by` (`approved_by`),
  ADD KEY `course_id` (`course_id`);

--
-- Indexes for table `exams`
--
ALTER TABLE `exams`
  ADD PRIMARY KEY (`exam_id`),
  ADD KEY `course_id` (`course_id`),
  ADD KEY `created_by` (`created_by`);

--
-- Indexes for table `options`
--
ALTER TABLE `options`
  ADD PRIMARY KEY (`option_id`),
  ADD KEY `question_id` (`question_id`);

--
-- Indexes for table `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`question_id`),
  ADD KEY `exam_id` (`exam_id`);

--
-- Indexes for table `results`
--
ALTER TABLE `results`
  ADD PRIMARY KEY (`result_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `exam_id` (`exam_id`),
  ADD KEY `graded_by` (`graded_by`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `user_logs`
--
ALTER TABLE `user_logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `user_seq`
--
ALTER TABLE `user_seq`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
  MODIFY `course_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `enrollments`
--
ALTER TABLE `enrollments`
  MODIFY `enrollment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `exams`
--
ALTER TABLE `exams`
  MODIFY `exam_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `options`
--
ALTER TABLE `options`
  MODIFY `option_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `question_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `results`
--
ALTER TABLE `results`
  MODIFY `result_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_logs`
--
ALTER TABLE `user_logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `user_seq`
--
ALTER TABLE `user_seq`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`approved_by`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `enrollments_ibfk_3` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`);

--
-- Constraints for table `exams`
--
ALTER TABLE `exams`
  ADD CONSTRAINT `exams_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  ADD CONSTRAINT `exams_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `options`
--
ALTER TABLE `options`
  ADD CONSTRAINT `options_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`question_id`);

--
-- Constraints for table `questions`
--
ALTER TABLE `questions`
  ADD CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`exam_id`);

--
-- Constraints for table `results`
--
ALTER TABLE `results`
  ADD CONSTRAINT `results_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `results_ibfk_2` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`exam_id`),
  ADD CONSTRAINT `results_ibfk_3` FOREIGN KEY (`graded_by`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `user_logs`
--
ALTER TABLE `user_logs`
  ADD CONSTRAINT `user_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
