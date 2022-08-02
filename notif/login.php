<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $user = $_POST["username"];
    $password = $_POST["password"];     

    $query_user = "SELECT username FROM users where username = '$user';";
    $execute = mysqli_query($connect, $query_user);
    $check_user = mysqli_affected_rows($connect);

    if($check_user != 1){
        $response['status'] = false;
        $response['pesan'] = "akun tidak ada";
    }
    else{
        $query_data = "SELECT id FROM users where username = '$user' AND password = md5('$password');";
        $execute_data = mysqli_query($connect, $query_data);

        $check = mysqli_affected_rows($connect);
        if($check == 1){
            $response['status'] = "true";
            $response['pesan'] = "berhasil login";
        }
        else{
            $response['status'] = "false";
            $response['pesan'] = "password salah";
        }
    }
}
echo json_encode($response);
mysqli_close($connect); 