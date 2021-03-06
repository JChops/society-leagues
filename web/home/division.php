<?php

include '../include/mysql.php';
include '../include/xtemplate.class.php';

$view = new xtemplate('division.html');

$result = mysql_query(
"SELECT *, DATE_FORMAT(division.start_date,'%m/%d/%Y') jq_start, DATE_FORMAT(division.end_date,'%m/%d/%Y') jq_end 
FROM division
RIGHT JOIN league ON league.league_id=division.league_id
RIGHT JOIN season ON season.season_id=division.season_id
RIGHT JOIN days ON  d_id=division.division_day
RIGHT JOIN season_name ON season_name.sn_id=season.season_number
WHERE division.division_id='{$_GET['division_id']}'");

$league = mysql_fetch_assoc($result);

foreach ($league as $key => $val)
	$view->assign($key, $val);

$cache1 = mysql_query("SELECT GROUP_CONCAT(match_id) cache FROM match_schedule WHERE division_id='{$_GET['division_id']}'");
$schedule_cache = mysql_fetch_assoc($cache1);

if ($league['league_id'] == '1') {

$result = mysql_query("SELECT team.name, team.team_id,
SUM(result_team.is_win) team_wins, SUM(IF(result_team.is_win=1,0,1)) team_losses,
(SELECT SUM(is_win) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_match_wins,
(SELECT SUM(IF(is_win=1,0,1)) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_match_losses,
(SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_games_won,
(SELECT SUM(games_lost) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_games_lost,


TRIM(LEADING '0' FROM
FORMAT((SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) /
((SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) +
(SELECT SUM(games_lost) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']}))),3)) pct

FROM division_member 
JOIN team ON team.team_id=division_member.team_id
JOIN match_schedule ON match_schedule.division_id=division_member.division_id AND (match_schedule.home_team_id=division_member.team_id OR match_schedule.visit_team_id=division_member.team_id)
JOIN result_team ON result_team.team_id=division_member.team_id AND result_team.match_id=match_schedule.match_id
WHERE division_member.division_id='{$_GET['division_id']}'
GROUP BY division_member.team_id
ORDER BY team_wins DESC, player_match_wins DESC, player_match_losses ASC, pct DESC");
}
elseif ($league['league_id'] == '4'){

$result = mysql_query("SELECT team.name, team.team_id,
SUM(result_team.is_win) team_wins, SUM(IF(result_team.is_win=1,0,1)) team_losses,
(SELECT SUM(is_win) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_match_wins,
(SELECT SUM(IF(is_win=1,0,1)) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_match_losses,
(SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_games_won,
(SELECT SUM(games_lost) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_games_lost,

TRIM(LEADING '0' FROM
FORMAT((SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) /
((SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) +
(SELECT SUM(games_lost) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']}))),3)) pct

FROM division_member 
JOIN team ON team.team_id=division_member.team_id
JOIN match_schedule ON match_schedule.division_id=division_member.division_id AND (match_schedule.home_team_id=division_member.team_id OR match_schedule.visit_team_id=division_member.team_id)
JOIN result_team ON result_team.team_id=division_member.team_id AND result_team.match_id=match_schedule.match_id
WHERE division_member.division_id='{$_GET['division_id']}'
GROUP BY division_member.team_id
ORDER BY team_wins DESC, pct DESC, player_match_wins DESC, player_games_won DESC");


}
	
else {

$result = mysql_query("SELECT team.name, team.team_id,
SUM(result_team.is_win) team_wins, SUM(IF(result_team.is_win=1,0,1)) team_losses,
(SELECT SUM(is_win) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_match_wins,
(SELECT SUM(IF(is_win=1,0,1)) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_match_losses,
(SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_games_won,
(SELECT SUM(games_lost) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) player_games_lost,

TRIM(LEADING '0' FROM
FORMAT((SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) /
((SELECT SUM(games_won) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']})) +
(SELECT SUM(games_lost) FROM result_ind WHERE result_ind.team_id=division_member.team_id AND result_ind.match_id IN({$schedule_cache['cache']}))),3)) pct

FROM division_member 
JOIN team ON team.team_id=division_member.team_id
JOIN match_schedule ON match_schedule.division_id=division_member.division_id AND (match_schedule.home_team_id=division_member.team_id OR match_schedule.visit_team_id=division_member.team_id)
JOIN result_team ON result_team.team_id=division_member.team_id AND result_team.match_id=match_schedule.match_id
WHERE division_member.division_id='{$_GET['division_id']}'
GROUP BY division_member.team_id
ORDER BY team_wins DESC, pct DESC, player_match_wins DESC, player_games_won DESC");


}

if (mysql_num_rows($result) > 0)
{
	while ($row = mysql_fetch_assoc($result))
	{
		foreach($row as $key => $val)
			$view->assign($key, $val);
		
		if ($league['league_id'] == '1')
		{
			$view->parse("main.{$league['league_type']}.9_ball_3");	
		}
		
		$view->parse("main.{$league['league_type']}");
	}
	
	if ($league['league_id'] == '1')
	{
		$view->parse("main.9_ball_1");	
		$view->parse("main.9_ball_2");	
	}
}
else
{
	$view->parse("main.empty");
	$view->parse("main.delete_button");
}

$view->parse('main');
$view->out('main');

?>