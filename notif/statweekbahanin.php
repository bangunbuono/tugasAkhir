<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $user = $_POST["user"];
    $bahan = $_POST["bahan"];
    $week = $_POST["week"];
    $month = $_POST["month"];
    $year = $_POST["year"];
    $keterangan = $_POST["keterangan"];

    if($keterangan == "weekly"){
        $query = "SELECT bahan, sum(jumlah) as jumlah, satuan, DATE(tanggal_masuk) as tanggal_masuk
                FROM barang_masuk where bahan = '$bahan' AND 
                WEEK(tanggal_masuk,1) = $week AND user = '$user'
                GROUP BY DAY(tanggal_masuk);";
        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);

        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "available";
            $response["statBahan"] = array();

            while($ambil = mysqli_fetch_object($execute)){
                $M["bahan"] = $ambil -> bahan;
                $M["jumlah"] = $ambil -> jumlah;
                $M["satuan"] = $ambil -> satuan;
                $M["tanggal_masuk"] = $ambil -> tanggal_masuk;

            array_push($response["statBahan"], $M);
            }
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "tidak ada data";
        }

    }else if($keterangan == "monthly"){
        $query = "SELECT bahan, sum(jumlah) as jumlah, satuan, DATE(tanggal_masuk) as tanggal_masuk
                FROM barang_masuk where bahan = '$bahan' AND 
                MONTH(tanggal_masuk) = $month AND user = '$user'
                GROUP BY DAY(tanggal_masuk);";
        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);

        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "available";
            $response["statBahan"] = array();

            while($ambil = mysqli_fetch_object($execute)){
                $M["bahan"] = $ambil -> bahan;
                $M["jumlah"] = $ambil -> jumlah;
                $M["satuan"] = $ambil -> satuan;
                $M["tanggal_masuk"] = $ambil -> tanggal_masuk;


            array_push($response["statBahan"], $M);
            }
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "tidak ada data";
        }

    }elseif($keterangan == "yearly"){
        $query = "SELECT bahan, sum(jumlah) as jumlah, satuan, DATE(tanggal_masuk) as tanggal_masuk
                FROM barang_masuk where bahan = '$bahan' AND 
                YEAR(tanggal_masuk) = $year AND user = '$user'
                GROUP BY DAY(tanggal_masuk);";
        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);

        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "available";
            $response["statBahan"] = array();

            while($ambil = mysqli_fetch_object($execute)){
                $M["bahan"] = $ambil -> bahan;
                $M["jumlah"] = $ambil -> jumlah;
                $M["satuan"] = $ambil -> satuan;
                $M["tanggal_masuk"] = $ambil -> tanggal_masuk;

            array_push($response["statBahan"], $M);
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
