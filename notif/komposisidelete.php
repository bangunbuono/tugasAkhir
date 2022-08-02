<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];
    $user = $_POST["user"];

    $query = "DELETE FROM komposisi where id = '$id' and user = '$user' ;";
    if(mysqli_query($connect,$query)){
        $response["code"] = 1;
        $response["pesan"] = "komposisi berhasil dihapus";
    }
    else{
        $response["code"] = 0;
        $response["pesan"] = "komposisi gagal dihapus";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);