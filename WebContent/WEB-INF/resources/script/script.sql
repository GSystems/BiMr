GRANT ALL PRIVILEGES ON BIMR.* TO bimruser@localhost;

CREATE TABLE tweets (
	id varchar(100),
	tweetMessage varchar(200),
	latitude varchar(50),
	longitude varchar(50),
	observationDate date,
	user_id varchar(50)
);

CREATE TABLE twitter_user (
	id varchar(50),
	username varchar(50),
	screenName varchar(50),
	email varchar(50),
	location varchar(50),
	url varchar(50)
);
