<?php

use function PHPSTORM_META\elementType;

require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $menu = $_POST["menu"];
    $user = $_POST["user"];
    
    $query = "SELECT * FROM komposisi where menu = '$menu' AND user = '$user'";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);
    
    if($check > 0){
    $response["code"] = 1;
    $response["pesan"] = "tersedia";
    $response["komposisiModelList"] = array();

    while($ambil = mysqli_fetch_object($execute)){
        $M["id"] = $ambil ->id;     
        $M["bahan"] = $ambil -> bahan;
        $M["jumlah"] = $ambil -> jumlah;
        $M["satuan"] = $ambil -> satuan;

        array_push($response["komposisiModelList"], $M);
    }
}
    else {
        $response["code"] = 0;
        $response["pesan"] = "gagal memuat";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);
