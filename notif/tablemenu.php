<?php
require("connection.php");
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $table = $_POST["table"];
    // $komposisi = $_POST["komposisi"];
    // $jumlah = $_POST["jumlah"];
    // $satuan = $_POST["satuan"];

    $query = "CREATE TABLE $table (
        id int NOT NULL AUTO_INCREMENT,
        komposisi varchar(100),
        jumlah int,
        satuan varchar(25),
        tanggal_dibuat timestamp not null default current_timestamp,
        terakhir_diubah timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
        PRIMARY KEY (id),
        UNIQUE KEY (komposisi));";

        if(mysqli_query($connect, $query)){
            $response["code"] = 1;
            $response["pesan"] = "tabel berhasil dibuat";

            // $query2 = "INSERT INTO $table
            // (komposisi, jumlah, satuan) values 
            // ('$komposisi','$jumlah','$satuan')";

            // $execute = mysqli_query($connect, $query2);
            // $check2 = mysqli_affected_rows($connect);

            // if($check2 > 0){
            //     $response["code"] = 1;
            //     $response["pesan"] = "data berhasil ditambahkan";
            // }
            // else{
            //     $response["code"] = 0;
            //     $response["pesan"] = "data gagal ditambhakan";
            // }
            
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "tabel gagal dibuat: ".mysqli_error($connect);
        }

       

}

echo json_encode($response);
mysqli_close($connect);
