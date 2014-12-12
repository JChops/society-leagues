<?php
if (getenv('DB_HOST') && getenv('DB_HOST') !=  'localhost') {
   $conn = mysql_connect(getenv('DB_HOST'), 'daleegdbuser','pr3dat0r');
   mysql_select_db('league', $conn);
} else {
   $conn = mysql_connect('localhost', 'league');
   mysql_select_db('league', $conn);
}

?>
