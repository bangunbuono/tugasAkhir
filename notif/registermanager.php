<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $password = $_POST["password"];
    $manager_name = $_POST["manager_name"];
    
    $query = "SELECT * FROM managers where manager_name = '$manager_name';";
    $execute = mysqli_query($connect, $query);
    $check_manager = mysqli_affected_rows($connect);

    if($check_manager != 0){
        $response["code"] = 2;
        $response["pesan"] = "akun manager sudah ada";
    }
    else{
        $query1 = "INSERT INTO managers (manager_name, password) 
                values ('$manager_name', md5('$password'));";
        $query_input = mysqli_query($connect, $query1);

        $check_input = mysqli_affected_rows($connect);
        if($check_input == 1){
            $response["code"] = 1;
            $response["pesan"] = "berhasil ragistrasi akun manager";
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "gagal ragistrasi akun manager";
        }
    }
}
echo json_encode($response);
mysqli_close($connect);