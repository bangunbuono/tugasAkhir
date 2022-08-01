<?php
require("connection.php");
$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];

    $query = "select * from menu where id = '$id'";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "data tersedia";
        $response["data"] = array();
        
        while($ambil = mysqli_fetch_object($execute)){
            $M["id"] = $ambil ->id;
            $M["menu"] = $ambil -> menu;
            $M["harga"] = $ambil -> harga;
            $M["qty"] = $ambil -> qty;
            $M["deskripsi"] = $ambil -> deskripsi;
    
            array_push($response["data"], $M);
        }
    }
    else{
        $response["code"] = 0;
        $response["pesan "] = "data tidak tersedia";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);
?>