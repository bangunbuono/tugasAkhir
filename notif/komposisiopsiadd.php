<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $bahan = $_POST["bahan"];
    $jumlah = $_POST["jumlah"];
    $satuan = $_POST["satuan"];
    $user = $_POST["user"];
    $menu = $_POST["menu"];

    $query_check = "SELECT * FROM komposisi_opsi
    where bahan = '$bahan' AND 
    user = '$user' AND 
    menu = '$menu';";
    $execute_check = mysqli_query($connect, $query_check);
    $check_data = mysqli_affected_rows($connect);

    if($check_data != 0){
        $response["code"] = 2;
        $response["pesan"] = "data sudah ada";
    }else{
        $query = "INSERT INTO komposisi_opsi 
            (bahan, jumlah, satuan, menu, user) values
            ('$bahan','$jumlah','$satuan', '$menu', '$user');";
            $execute = mysqli_query($connect, $query);
            $check = mysqli_affected_rows($connect);

        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "Berhasil menambah komposisi";
        }
                
        else{
            $response["code"] = 0;
            $response["pesan"] = "Gagal:".mysqli_error($connect);
        }
    }  
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);