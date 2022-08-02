<?php 
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    $user = $_POST["user"];

    $query= "SELECT DISTINCT bulan from record_keluar_masuk WHERE user = '$user' ORDER BY bulan;";

    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);
    
    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "berhasil";
        $response["bulan"] = array();

        while ($ambil = mysqli_fetch_object($execute)){
            // $M["id"] = $ambil ->id;     
            // $M["kode"] = $ambil -> kode;
            // $M["keterangan"] = $ambil -> keterangan;
            $M["bulan"] = $ambil -> bulan;            

            array_push($response["bulan"], $M);
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




?>