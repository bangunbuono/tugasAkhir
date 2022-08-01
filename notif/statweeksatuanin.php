<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $user = $_POST["user"];
    $satuan = $_POST["satuan"];
    $week = $_POST["week"];
    $month = $_POST["month"];
    $year = $_POST["year"];
    $keterangan = $_POST["keterangan"];

    if($keterangan == "weekly"){
        $query = "SELECT bahan, sum(jumlah) as jumlah, satuan from barang_masuk 
        WHERE satuan = '$satuan' and 
        week(tanggal_masuk, 1) = '$week' AND 
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

    }else if($keterangan == "monthly"){
        $query = "SELECT bahan, sum(jumlah) as jumlah, satuan from barang_masuk
        WHERE satuan = '$satuan' and 
        MONTH(tanggal_masuk) = '$month' and
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

    }elseif($keterangan == "yearly"){
        $query = "SELECT bahan, sum(jumlah) as jumlah, satuan from barang_masuk
        WHERE satuan = '$satuan' and 
        YEAR(tanggal_masuk) = '$year' and
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
}
else{
    $response["code"] = 0;
    $response["pesan"] = "gagal";
}

// header('Content-Type: application/json; charset=utf-8');
echo json_encode($response);
mysqli_close($connect);
