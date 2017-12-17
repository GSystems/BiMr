GRANT ALL PRIVILEGES ON BIMR.* TO bimruser@localhost;

DROP TABLE `tweets`;

CREATE TABLE tweets (
	id bigint(100) NOT NULL AUTO_INCREMENT,
	tweetId bigint(100),
	tweetMessage varchar(200),
	latitude varchar(50),
	longitude varchar(50),
	observationDate date,
	PRIMARY KEY(id)
);

CREATE TABLE twitter_user (
	id varchar(50),
	username varchar(50),
	screenName varchar(50),
	email varchar(50),
	location varchar(50),
	url varchar(50)
);

CREATE TABLE ebird_data (
	id int,
	user varchar(50),
	common_name varchar(50),
	latitude varchar(50),
	longitute varchar(50),
	observation_date date
);
