<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $manager_name = $_POST["manager_name"];
    
    $query = "DELETE FROM managers where manager_name = '$manager_name';";
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