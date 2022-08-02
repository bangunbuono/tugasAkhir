<?php 
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    $user = $_POST["user"];
    $kode = $_POST["kode"];
    $keterangan = $_POST["keterangan"];
    $tanggal = $_POST["tanggal"];
    $bulan = $_POST["bulan"];

    $query= "INSERT INTO record_keluar_masuk (kode, keterangan, tanggal, user, bulan) values 
            ('$kode','$keterangan','$tanggal','$user','$bulan');";

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
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";   
}

echo json_encode($response);
mysqli_close($connect);
