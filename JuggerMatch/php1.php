<?php
   $obj = json_decode($_POST['value']);
   $con = mysql_connect("localhost","username","password");

     mysql_connect_db('your_database',$con);


$result = json_decode($obj);
foreach($result as $key => $value) {
    if($value) {
//works for array, not sure if for single object
//mysql table names,host,databasename or password yet unknown

            //how to use json array to insert data in Database
        mysql_query("INSERT INTO matches (team_1, team_2, team_score_1,team_score_2,start_time,end_time) VALUES ($value->team_1, $value->team_2,$value->team_score_1,$value->team_score_2,$value->start_time,$value->end_time)");
    }
    mysql_close($con);
}
   ?>
   
   
