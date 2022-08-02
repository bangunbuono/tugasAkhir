<?php 
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    $user = $_POST["user"];
    $tanggal = $_POST["tanggal"];
    $keterangan = $_POST["keterangan"];

    if($keterangan=="semua"){
        $query= "SELECT * FROM record_keluar_masuk 
                WHERE date(tanggal) = '$tanggal' and 
                user = '$user'";

        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);
        
        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "berhasil";
            $response["record"] = array();

            while ($ambil = mysqli_fetch_object($execute)){
                $M["id"] = $ambil ->id;     
                $M["kode"] = $ambil -> kode;
                $M["keterangan"] = $ambil -> keterangan;
                $M["bulan"] = $ambil -> bulan;         

                array_push($response["record"], $M);
            }
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "gagal";
        }
    }else{
        $query= "SELECT * FROM record_keluar_masuk 
                WHERE date(tanggal) = '$tanggal' and 
                user = '$user' and keterangan = '$keterangan';";

        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);
        
        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "berhasil";
            $response["record"] = array();

            while ($ambil = mysqli_fetch_object($execute)){
                $M["id"] = $ambil ->id;     
                $M["kode"] = $ambil -> kode;
                $M["keterangan"] = $ambil -> keterangan;
                $M["bulan"] = $ambil -> bulan;         

                array_push($response["record"], $M);
            }
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "gagal";
        }
    }


}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";   
}

echo json_encode($response);
mysqli_close($connect);


?>