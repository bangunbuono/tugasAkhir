<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $user = $_POST["user"];
    $satuan = $_POST["satuan"];
    $week = $_POST["week"];

    $query = "SELECT bahan, sum(jumlah) as jumlah, satuan from barang_keluar 
        WHERE satuan = '$satuan' and 
        week(tanggal_keluar, 1) = '$week' AND 
        user = '$user'
        GROUP BY bahan";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "available";
        $response["statSatuan"] = array();

        while($ambil = mysqli_fetch_object($execute)){
            $M["bahan"] = $ambil -> bahan;
            $M["jumlah"] = $ambil -> jumlah;
            $M["satuan"] = $ambil -> satuan;

        array_push($response["statSatuan"], $M);
        }
    }
    else{
        $response["code"] = 0;
        $response["pesan"] = "tidak ada data";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "gagal";
}

echo json_encode($response);
mysqli_close($connect);
