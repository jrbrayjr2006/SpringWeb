SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `physician_survey`;
CREATE SCHEMA IF NOT EXISTS `physician_survey` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `physician_survey` ;

-- -----------------------------------------------------
-- Table `physician_survey`.`survey`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `physician_survey`.`survey` ;

CREATE  TABLE IF NOT EXISTS `physician_survey`.`survey` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `rating` INT NULL COMMENT 'Level of satisfaction the physician has.  This will range from 1 on the low end to 10 on the high end of satisfaction.' ,
  `why_feeling` VARCHAR(45) NULL COMMENT 'Why does the physician feel this way.' ,
  `work_dissatisfaction` VARCHAR(45) NULL ,
  `answer_matrix` VARCHAR(45) NULL COMMENT 'Answers that are comma colon delimited to account for different numbers of questions in each group' ,
  `comments` LONGTEXT NULL COMMENT 'Detailed free text comments from physicians' ,
  `organization_key` LONGTEXT NULL COMMENT 'Identifying key for institution' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci, 
COMMENT = 'Table to collect physician survey response data' ;

-- -----------------------------------------------------
-- Table `physician_survey`.`institutions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `physician_survey`.`institutions` ;

CREATE  TABLE IF NOT EXISTS `physician_survey`.`institutions` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `organization_key` VARCHAR(45) NULL COMMENT 'Identifying key for institution.' ,
  `organization_name` VARCHAR(45) NULL COMMENT 'Name of the medical institutions.', 
  `demo` VARCHAR(5) NOT NULL COMMENT 'Is this a demo institution that is not used in production',
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci, 
COMMENT = 'Table of medical institutions' ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO institutions (organization_key, organization_name, demo) VALUES ("MCNI001","Mercy North Iowa","DEMO");


-- ---------------------------
-- Export data to CSV
-- ---------------------------
SELECT id, rating, why_feeling, work_dissatisfaction, answer_matrix, comments 
FROM survey
-- WHERE organization_key = 'DEMO'
-- INTO OUTFILE '/usr/ec2-user/survey.csv'
INTO OUTFILE '/Users/james_r_bray/survey.csv'
FIELDS ENCLOSED BY '"' TERMINATED BY ';' ESCAPED BY '"'
LINES TERMINATED BY '\r\n';