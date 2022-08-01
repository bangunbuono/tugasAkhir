<?php 
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    $user = $_POST["user"];
    $kode = $_POST["kode"];
    $keterangan = $_POST["keterangan"];

    if($keterangan=="barang keluar"){
        $query= "SELECT bahan, sum(jumlah) as jumlah, satuan, tanggal_keluar as tanggal FROM 
                barang_keluar WHERE kode = '$kode' and 
                user = '$user' GROUP BY bahan;";

        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);
        
        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "berhasil";
            $response["recordDetail"] = array();

            while ($ambil = mysqli_fetch_object($execute)){
                $M["bahan"] = $ambil ->bahan;     
                $M["jumlah"] = $ambil -> jumlah;
                $M["satuan"] = $ambil -> satuan;
                $M["tanggal"] = $ambil -> tanggal;         

                array_push($response["recordDetail"], $M);
            }
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "gagal";
        }
    }
    elseif($keterangan=="barang masuk"){
        $query= "SELECT bahan, sum(jumlah) as jumlah, satuan, tanggal_masuk as tanggal FROM 
                barang_masuk WHERE kode = '$kode' and 
                user = '$user' GROUP BY bahan;";

        $execute = mysqli_query($connect, $query);
        $check = mysqli_affected_rows($connect);
        
        if($check > 0){
            $response["code"] = 1;
            $response["pesan"] = "berhasil";
            $response["recordDetail"] = array();

            while ($ambil = mysqli_fetch_object($execute)){
                $M["bahan"] = $ambil ->bahan;     
                $M["jumlah"] = $ambil -> jumlah;
                $M["satuan"] = $ambil -> satuan;
                $M["tanggal"] = $ambil -> tanggal;         

                array_push($response["recordDetail"], $M);
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