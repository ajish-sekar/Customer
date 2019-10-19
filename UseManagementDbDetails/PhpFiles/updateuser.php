    <?php
    $email = $_POST["email"];
    $newemail = $_POST["newemail"];
    $name = $_POST["name"];
    $mobile = $_POST["mobile"];
    include("dbConnect.php");
    $response=array();
    $response["success"]=false;
    $check=mysqli_query($conn,"SELECT * FROM `users` WHERE email='$email'");
    $affected=mysqli_affected_rows($conn);
    if($affected == 0){
      $response["status"]="USERNAME";
    }
    else{
      
      $result=mysqli_query($conn,"UPDATE `users` SET `email` = '$newemail', `name` = '$name', `phone` = '$mobile' where email= '$email'");      
    $response["success"]=$result;
      
  }
    echo json_encode($response);#encoding RESPONSE into a JSON and returning.
    mysqli_close($conn);
    exit();
    ?>