<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

    $menu = $_POST["menu"];
    $user = $_POST["user"]; 
    $bahan = $_POST["bahan"];

    $query = "UPDATE stocks, komposisi_opsi set
         stocks.jumlah = stocks.jumlah - komposisi_opsi.jumlah
         where stocks.bahan_baku = komposisi_opsi.bahan 
         and komposisi_opsi.menu = '$menu' 
         and stocks.user = '$user'
         and komposisi_opsi.user = '$user'
         and komposisi_opsi.bahan = '$bahan';";    

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