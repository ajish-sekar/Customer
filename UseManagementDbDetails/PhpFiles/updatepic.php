      <?php
    $email = $_POST["email"];
    $image = $_POST["image"];
    include("dbConnect.php");
    $response=array();
    $response["success"]=false;
    $check=mysqli_query($conn,"SELECT * FROM `users` WHERE `email`='$email'");
    $affected=mysqli_affected_rows($conn);
    if($affected<0){
      $response["status"]="USERNAME";
    }
    else{
      $fetchId=mysqli_query($conn,"SELECT `id` FROM `users` WHERE `email`='$email'");

      $id=0;
      while($rowid=mysqli_fetch_array($fetchId,MYSQLI_ASSOC)){
        $id=$rowid['id'];
      }

      if ($id>0) {
      	  $filePath = "dp/$id.png";
	      $url = "http://magic-print.000webhostapp.com/dp/$id.png";
	      $update=mysqli_query($conn,"UPDATE `users` SET `photo`='$url' WHERE `email`='$email'");
	      if($update){
	        file_put_contents($filePath,base64_decode($image));
	        $response["success"]=true;
	      }
	   }
    }
    echo json_encode($response);#encoding RESPONSE into a JSON and returning.
    mysqli_close($conn);
    exit();
    ?>