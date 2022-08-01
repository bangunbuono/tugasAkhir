<?php
require("connection.php");
$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];
    $menu = $_POST["menu"];
    $harga = $_POST["harga"];
    $deskripsi = $_POST["deskripsi"];
    $menuBaru = $_POST["menuBaru"];
    $user = $_POST["user"];

    $query = "UPDATE menu 
    set menu = '$menuBaru', 
    harga = '$harga', 
    deskripsi = '$deskripsi' 
    where id = '$id';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "data berhasil diubah";

        $query2 = "UPDATE komposisi, komposisi_opsi 
                set komposisi.menu = '$menuBaru',
                komposisi_opsi.menu = '$menuBaru' 
                where komposisi.user ='$user' and 
                komposisi_opsi.user = '$user' and
                komposisi.menu = '$menu' and
                komposisi_opsi.menu = '$menu';";

        $execute2 = mysqli_query($connect, $query2);
        $check2 = mysqli_affected_rows($connect);

        if($check2 > 0){
            $response["code"] = 1;
            $response["pesan"] = "data berhasil diubah";
        }else{
            $response["code"] = 0;
            $response["pesan"] = "data gagal diubah";
        }   
    }

    else{
        $response["code"] = 0;
        $response["pesan"] = "data gagal diubah";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);
?>