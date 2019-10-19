<?php
$email = $_POST["email"];
$password = $_POST["password"];
include("dbConnect.php");
$response = array();
$response["success"] = false;
$response["status"]="INVALID";
$result = mysqli_query($conn, "SELECT * FROM `users` WHERE `email` = '$email' AND `password` = '$password'");
$affected = mysqli_affected_rows($conn);
if ($affected > 0) {
  #USER DETAILS MATCH
    $response["success"] = true;
    while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
        $response["name"] = $row['name'];
        $response["email"] = $row['email'];
        $response["mobile"] = $row['phone'];
    $response["url"] = $row["photo"];
    }
}
else{
  $userCheck = mysqli_query($conn, "SELECT * FROM `users` WHERE `email` = '$email'");
  $userAffected = mysqli_affected_rows($conn);
  if($userAffected>0){
    $response["status"]="PASSWORD";
  }
}
echo json_encode($response);
mysqli_close($conn);
exit();
?>