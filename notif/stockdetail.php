<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];

    $query = "SELECT * from stocks where id = '$id';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "available";
        $response["stocksModels"] = array();

        while($ambil = mysqli_fetch_object($execute)){
            $M["id"] = $ambil ->id;
            $M["bahan_baku"] = $ambil -> bahan_baku;
            $M["jumlah"] = $ambil -> jumlah;
            $M["satuan"] = $ambil -> satuan;
            $M["min_pesan"] = $ambil -> min_pesan;
            $M["waktu"] = $ambil -> waktu;

            array_push($response["stocksModels"], $M);
        }

    }
    else{
        $response["code"] = 0;
        $response["pesan"] = "not available";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}


echo json_encode($response);
mysqli_close($connect);
