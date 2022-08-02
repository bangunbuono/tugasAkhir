<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $user = $_POST["user"];

    $query = "SELECT * from menu where user = '$user';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "available";
        $response["data"] = array();

        while($ambil = mysqli_fetch_object($execute)){
            $M["id"] = $ambil ->id;
            $M["menu"] = $ambil -> menu;
            $M["harga"] = $ambil -> harga;
            $M["qty"] = $ambil -> qty;
            $M["deskripsi"] = $ambil -> deskripsi;
            $M["user"] = $ambil -> user;

        array_push($response["data"], $M);
        }
    }
    else{
        $response["code"] = 0;
        $response["pesan"] = "tidak ada data";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "not available";
}

echo json_encode($response);
mysqli_close($connect);
