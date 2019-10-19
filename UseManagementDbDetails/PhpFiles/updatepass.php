  <?php
    $email = $_POST["email"];
    $password = $_POST["password"];
    include("dbConnect.php");
    $response=array();
    $response["success"]=false;
    $check=mysqli_query($conn,"SELECT * FROM `users` WHERE `email`='$email'");
    $affected=mysqli_affected_rows($conn);
    if($affected>0){

      $response["status"]="USERNAME";

    }
    else{
      
      $result=mysqli_query($conn,"UPDATE `users` SET `password` = '$password '");      
	  $response["success"]=true;
	    
	}
    echo json_encode($response);#encoding RESPONSE into a JSON and returning.
    mysqli_close($conn);
    exit();
    ?>