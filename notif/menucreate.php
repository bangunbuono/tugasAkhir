<?php
require("connection.php");
$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $menu = $_POST["menu"];
    $harga = $_POST["harga"];
    $deskripsi = $_POST["deskripsi"];
    $user = $_POST["user"];

    $query_check = "SELECT * FROM menu
            where menu = '$menu' AND 
            user = '$user';";
    $execute_check = mysqli_query($connect, $query_check);
    $check_data = mysqli_affected_rows($connect);

    if($check_data != 0){
        $response["code"] = 2;
        $response["pesan"] = "menu sudah ada";
    }else{
        $query = "INSERT into menu (menu, harga, deskripsi,user) values
            ('$menu','$harga','$deskripsi','$user');";
            $execute = mysqli_query($connect, $query);
            $check = mysqli_affected_rows($connect);
        if($check == 1){
            $response["code"] = 1;
            $response["pesan"] = "menu berhasil ditambahkan";
        }
        else{
            $response["code"] = 0;
            $response["pesan"] = "menu gagal ditambhakan";
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