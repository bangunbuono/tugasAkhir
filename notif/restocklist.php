<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $user = $_POST["user"];
    $query = "SELECT * from stocks where user = '$user';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        
        $response["stocks"] = array();

        while($ambil = mysqli_fetch_object($execute)){
            $M["id"] = $ambil -> id;
            $M["bahan_baku"] = $ambil ->bahan_baku;
            $M["satuan"] = $ambil -> satuan;

            array_push($response["stocks"], $M);
        }

    }
    else{
        echo "koneksi gagal".mysqli_error($connect);
    }
}


echo json_encode($response);
mysqli_close($connect);
