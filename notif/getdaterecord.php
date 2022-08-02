<?php 
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    $user = $_POST["user"];
    $bulan = $_POST["bulan"];

    $query= "SELECT DISTINCT DATE_FORMAT(tanggal, '%d %M %y') as tanggal 
            FROM record_keluar_masuk 
            where user = '$user' and bulan = '$bulan';";

    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);
    
    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "berhasil";
        $response["date"] = array();

        while ($ambil = mysqli_fetch_object($execute)){
            // $M["id"] = $ambil ->id;     
            // $M["kode"] = $ambil -> kode;
            // $M["keterangan"] = $ambil -> keterangan;
            // $M["bulan"] = $ambil -> bulan;  
            $M["tanggal"] = $ambil -> tanggal;         

            array_push($response["date"], $M);
        }
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




?>