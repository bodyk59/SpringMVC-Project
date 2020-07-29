SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jom`
--

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(30);

-- --------------------------------------------------------

--
-- Table structure for table `marathon`
--

CREATE TABLE `marathon` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `marathon_user`
--

CREATE TABLE `marathon_user` (
  `marathon_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `progress`
--

CREATE TABLE `progress` (
  `id` int(11) NOT NULL,
  `started` date NOT NULL,
  `status` varchar(255) NOT NULL,
  `updated` date NOT NULL,
  `task_id` int(11) NOT NULL,
  `trainee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sprint`
--

CREATE TABLE `sprint` (
  `id` int(11) NOT NULL,
  `finish` date NOT NULL,
  `start_date` date NOT NULL,
  `title` varchar(255) NOT NULL,
  `marathon_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `id` int(11) NOT NULL,
  `created` date NOT NULL,
  `title` varchar(255) NOT NULL,
  `updated` date NOT NULL,
  `sprint_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `marathon`
--
ALTER TABLE `marathon`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `marathon_user`
--
ALTER TABLE `marathon_user`
  ADD KEY `FKkt2gn0a9ay0e9t8w83rai59it` (`marathon_id`),
  ADD KEY `FKnhaad5lcolq6rdii9rmy7wj10` (`user_id`);

--
-- Indexes for table `progress`
--
ALTER TABLE `progress`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKni6ct4x7y17pr3w1wdfrkogho` (`task_id`),
  ADD KEY `FK4d8n3oweua6vjkbgx9xnkmkts` (`trainee_id`);

--
-- Indexes for table `sprint`
--
ALTER TABLE `sprint`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlr9nt5lj3wqo9907lh0uh67mo` (`marathon_id`);

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKao8fqiovc2g9en1pq7jaengh6` (`sprint_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `marathon`
--
ALTER TABLE `marathon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT for table `progress`
--
ALTER TABLE `progress`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `sprint`
--
ALTER TABLE `sprint`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;
--
-- AUTO_INCREMENT for table `task`
--
ALTER TABLE `task`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `marathon_user`
--
ALTER TABLE `marathon_user`
  ADD CONSTRAINT `FKkt2gn0a9ay0e9t8w83rai59it` FOREIGN KEY (`marathon_id`) REFERENCES `marathon` (`id`),
  ADD CONSTRAINT `FKnhaad5lcolq6rdii9rmy7wj10` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `progress`
--
ALTER TABLE `progress`
  ADD CONSTRAINT `FK4d8n3oweua6vjkbgx9xnkmkts` FOREIGN KEY (`trainee_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKni6ct4x7y17pr3w1wdfrkogho` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`);

--
-- Constraints for table `sprint`
--
ALTER TABLE `sprint`
  ADD CONSTRAINT `FKlr9nt5lj3wqo9907lh0uh67mo` FOREIGN KEY (`marathon_id`) REFERENCES `marathon` (`id`);

--
-- Constraints for table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `FKao8fqiovc2g9en1pq7jaengh6` FOREIGN KEY (`sprint_id`) REFERENCES `sprint` (`id`);
