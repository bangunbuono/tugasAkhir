<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $kode = $_POST["kode"];
    $bahan = $_POST["bahan"];
    $jumlah = $_POST["jumlah"];
    $satuan = $_POST["satuan"];
    $user = $_POST["user"];
    $tanggal_masuk = $_POST["tanggal_masuk"];

    $query= "INSERT INTO barang_masuk (kode, bahan, jumlah, satuan, user, tanggal_masuk)
                VALUES ('$kode','$bahan','$jumlah','$satuan','$user', '$tanggal_masuk');";

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