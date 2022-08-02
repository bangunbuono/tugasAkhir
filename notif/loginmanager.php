<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $manager_name = $_POST["manager_name"];
    $password = $_POST["password"];     

    $query_manager = "SELECT manager_name FROM managers where manager_name = '$manager_name';";
    $execute = mysqli_query($connect, $query_manager);
    $check_manager = mysqli_affected_rows($connect);

    if($check_manager != 1){
        $response['status'] = "false";
        $response['pesan'] = "akun tidak ada";
    }
    else{
        $query_data = "SELECT id FROM managers 
                where manager_name = '$manager_name' AND
                password = md5('$password');";
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