<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];
    $jumlah = $_POST["jumlah"]; 

    $query = "UPDATE stocks set
            jumlah = jumlah - '$jumlah'
            where id = '$id';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "berhasil menambah stock";
    }
    else{
        $response["code"] = 0;
        $response["pesan"] = "gagal";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";   
}

echo json_encode($response);
mysqli_close($connect);
