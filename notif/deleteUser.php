<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $username = $_POST["username"];
    
    $query = "DELETE FROM users where username = '$username';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    $check= mysqli_affected_rows($connect);
    if($check == 1){
        $response['code'] = 1;
        $response['pesan'] = "berhasil hapus akun";
    }
    else{
        $response['code'] = 0;
        $response['pesan'] = "gagal hapus akun";
    }
    
}
echo json_encode($response);
mysqli_close($connect);