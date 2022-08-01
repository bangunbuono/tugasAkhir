<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];
    $bahan_baku = $_POST["bahan_baku"];
    $jumlah = $_POST["jumlah"]; 
    $satuan = $_POST["satuan"];
    $min_pesan = $_POST["min_pesan"];
    $waktu = $_POST["waktu"];
    $user = $_POST["user"];
    $bahanBaru = $_POST["bahanBaru"];

    $query = "UPDATE stocks set
            bahan_baku = '$bahanBaru',
            jumlah = '$jumlah',
            satuan = '$satuan',
            min_pesan = '$min_pesan',
            waktu = '$waktu'
            where id = '$id';";

    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $query2 = "UPDATE komposisi, komposisi_opsi set
                komposisi.bahan = '$bahanBaru', 
                komposisi_opsi.bahan = '$bahanBaru' where
                komposisi.bahan = '$bahan_baku' and 
                komposisi.user = '$user' and 
                komposisi_opsi.bahan = '$bahan_baku' and 
                komposisi_opsi.user = '$user';";

        $execute2 = mysqli_query($connect, $query2);
        $check2 = mysqli_affected_rows($connect);

        if($check2 > 0){
            $response["code"] = 1;
            $response["pesan"] = "berhasil";
        }else{
            $response["code"] = 0;
            $response["pesan"] = "gagal";
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