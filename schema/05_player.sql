create table player  (
 player_id int(11) NOT NULL AUTO_INCREMENT, 
 season_id int(11) NOT NULL,
 division_id int(11) NOT NULL,
 user_id int(11) NOT NULL,
 team_id int(11) NOT NULL,
 handicap varchar(16) NOT NULL,
 PRIMARY KEY (`player_id`),
CONSTRAINT FOREIGN KEY (`season_id`) REFERENCES season(season_id) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT FOREIGN KEY (`user_id`)   REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT FOREIGN KEY (`team_id`)   REFERENCES team(team_id) ON DELETE CASCADE ON UPDATE CASCADE
CONSTRAINT FOREIGN KEY (`division_id`)   REFERENCES division(division_id) ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=MyISAM AUTO_INCREMENT=90000;
