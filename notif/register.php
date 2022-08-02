<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $password = $_POST["password"];
    $user = $_POST["username"];
    

    $query = "SELECT * FROM users where username = '$user';";
    $execute = mysqli_query($connect, $query);
    $check_user = mysqli_affected_rows($connect);

    if($check_user != 0){
        $response['code'] = 2;
        $response['pesan'] = "akun sudah ada";
    }
    else{
        $query1 = "INSERT INTO users (username, password) 
                values ('$user', md5('$password'));";
        $query_input = mysqli_query($connect, $query1);

        $check_input = mysqli_affected_rows($connect);
        if($check_input == 1){
            $response['code'] = 1;
            $response['pesan'] = "berhasil input";
        }
        else{
            $response['code'] = 0;
            $response['pesan'] = "gagal input";
        }
    }
}
echo json_encode($response);
mysqli_close($connect);