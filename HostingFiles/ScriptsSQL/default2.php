<?
header('Content-Type: text/html; charset=utf-8');
$host = $_SERVER['HTTP_HOST'];
setlocale(LC_TIME, "es_ES.utf8");
date_default_timezone_set('Europe/Madrid');

/*
Directory Listing Script - Version 2
====================================
Script Author: Ash Young <ash@evoluted.net>. www.evoluted.net
Layout: Manny <manny@tenka.co.uk>. www.tenka.co.uk
*/


?>
<?
if (isset($_REQUEST['id'])){
	$idv=$_REQUEST['id'];
}
if (isset($_REQUEST['pub'])){
	$pub=$_REQUEST['pub'];
}
if (isset($_REQUEST['priv'])){
	$priv=$_REQUEST['priv'];
}

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <h3>Test escritura base de datos</h3> 
</html>
<?

$user="u602876340_egc";
$pass="egc4db";
$server="localhost";
$db="u602876340_egc";
$con=mysqli_connect($server,$user,$pass,$db);

$sql = "INSERT INTO keysvotes (idvotation, publicKey, privateKey) VALUES ('".$idv."', '".$pub."', '".$priv."')";

if (mysqli_query($con, $sql)) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . mysqli_error($con);
}

mysqli_close($con);

?>