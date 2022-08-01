<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $bahan_baku = $_POST["bahan_baku"];
    $jumlah = $_POST["jumlah"]; 
    $satuan = $_POST["satuan"];
    $min_pesan = $_POST["min_pesan"];
    $waktu = $_POST["waktu"];
    $user = $_POST["user"];

    $query_check = "SELECT * FROM stocks
            where bahan_baku = '$bahan_baku' AND 
            user = '$user';";
    $execute_check = mysqli_query($connect, $query_check);
    $check_data = mysqli_affected_rows($connect);

    if($check_data != 0){
        $response["code"] = 2;
        $response["pesan"] = "data sudah ada";
    }else{
        $query = "INSERT INTO stocks 
        (bahan_baku,jumlah,satuan,min_pesan, waktu, user) 
        values
        ('$bahan_baku','$jumlah','$satuan','$min_pesan','$waktu', '$user');";
        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);
        
        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "berhasil";
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "gagal";
        }
    }    
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";   
}

echo json_encode($response);
mysqli_close($connect);