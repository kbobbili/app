CREATE TABLE `department` (
                              `id` bigint(20) NOT NULL,
                              `created_at` datetime(6) DEFAULT NULL,
                              `updated_at` datetime(6) DEFAULT NULL,
                              `start_date` date DEFAULT NULL,
                              `type` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `address` (
                           `id` bigint(20) NOT NULL,
                           `created_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `apt_num` varchar(255) DEFAULT NULL,
                           `city` varchar(255) DEFAULT NULL,
                           `state` varchar(255) DEFAULT NULL,
                           `street` varchar(255) DEFAULT NULL,
                           `zip_code` varchar(255) DEFAULT NULL,
                           `department_id` bigint(20) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK8yd929rpucstfmp9ke60eaxnb` (`department_id`),
                           CONSTRAINT `FK8yd929rpucstfmp9ke60eaxnb` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employee` (
                            `id` bigint(20) NOT NULL,
                            `created_at` datetime(6) DEFAULT NULL,
                            `updated_at` datetime(6) DEFAULT NULL,
                            `commission` double DEFAULT NULL,
                            `first_name` varchar(255) DEFAULT NULL,
                            `job_type` varchar(255) DEFAULT NULL,
                            `joining_date` date DEFAULT NULL,
                            `last_name` varchar(255) DEFAULT NULL,
                            `salary` double DEFAULT NULL,
                            `department_id` bigint(20) DEFAULT NULL,
                            `department_headed_id` bigint(20) DEFAULT NULL,
                            `manager_id` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKbejtwvg9bxus2mffsm3swj3u9` (`department_id`),
                            KEY `FK8nxlgtt5weiyc3cw6mn7polht` (`department_headed_id`),
                            KEY `FKou6wbxug1d0qf9mabut3xqblo` (`manager_id`),
                            CONSTRAINT `FK8nxlgtt5weiyc3cw6mn7polht` FOREIGN KEY (`department_headed_id`) REFERENCES `department` (`id`),
                            CONSTRAINT `FKbejtwvg9bxus2mffsm3swj3u9` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
                            CONSTRAINT `FKou6wbxug1d0qf9mabut3xqblo` FOREIGN KEY (`manager_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `project` (
                           `id` bigint(20) NOT NULL,
                           `created_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `end_date` datetime(6) DEFAULT NULL,
                           `estimated_end_date` datetime(6) DEFAULT NULL,
                           `is_completed` bit(1) DEFAULT NULL,
                           `name` varchar(255) DEFAULT NULL,
                           `start_date` datetime(6) DEFAULT NULL,
                           `status` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `project_assignment` (
                                      `employee_id` bigint(20) NOT NULL,
                                      `project_id` bigint(20) NOT NULL,
                                      `assignment_date` datetime(6) DEFAULT NULL,
                                      `id` bigint(20) NOT NULL,
                                      PRIMARY KEY (`employee_id`,`project_id`),
                                      KEY `FKh69ib8gnnrkkvaptrxki4kmd6` (`id`),
                                      CONSTRAINT `FKh69ib8gnnrkkvaptrxki4kmd6` FOREIGN KEY (`id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employee_project` (
                                    `employee_id` bigint(20) NOT NULL,
                                    `project_id` bigint(20) NOT NULL,
                                    PRIMARY KEY (`employee_id`,`project_id`),
                                    KEY `FK4yddvnm7283a40plkcti66wv9` (`project_id`),
                                    CONSTRAINT `FK4yddvnm7283a40plkcti66wv9` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
                                    CONSTRAINT `FKb25s5hgggo6k4au4sye7teb3a` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hibernate_sequence` (
    `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
