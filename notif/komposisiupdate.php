<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];
    $bahan = $_POST["bahan"];
    $jumlah = $_POST["jumlah"];
    $satuan = $_POST["satuan"];

    $query = "UPDATE komposisi
                set bahan = '$bahan',
                jumlah = '$jumlah',
                satuan = '$satuan' where id = '$id';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "berhasil update data";
    }
            
    else{
        $response["code"] = 0;
        $response["pesan"] = "gagal:".mysqli_error($connect);
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);