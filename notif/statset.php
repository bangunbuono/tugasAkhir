<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $user = $_POST["user"];

    $query = "SELECT distinct satuan from stocks where user = '$user';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "available";
        $response["statmodel"] = array();

        while($ambil = mysqli_fetch_object($execute)){
            // $M["id"] = $ambil ->id;
            // $M["bahan_baku"] = $ambil -> bahan_baku;
            // $M["jumlah"] = $ambil -> jumlah;
            $M["satuan"] = $ambil -> satuan;
            // $M["min_pesan"] = $ambil -> min_pesan;
            // $M["waktu"] = $ambil -> waktu;
            // $M["user"] = $ambil -> user;

        array_push($response["statmodel"], $M);
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
