<?
header('Content-Type: text/html; charset=utf-8');
$host = $_SERVER['HTTP_HOST'];
setlocale(LC_TIME, "es_ES.utf8");
date_default_timezone_set('Europe/Madrid');

?>
<?
if (isset($_REQUEST['id'])){
	$idv=$_REQUEST['id'];
}

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <h3>Test lectura base de datos</h3> 
</html>
<?

$user="u602876340_egc";
$pass="egc4db";
$server="localhost";
$db="u602876340_egc";
$con=mysql_connect($server,$user,$pass);
mysql_select_db($db, $con);
$result = mysql_query("SELECT * FROM keysvotes where idvotation ='".$idv."'", $con);
echo "Publickey: ".mysql_result($result, 0, "publicKey")."<br>";
echo "Privatekey: ".mysql_result($result, 0, "privateKey")."<br>"; 

mysql_close($con);
?>