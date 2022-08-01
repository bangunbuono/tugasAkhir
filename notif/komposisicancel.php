<?php
require("connection.php");
$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $menu = $_POST["menu"];
    $user = $_POST["user"];

    $query = "DELETE from komposisi where menu = '$menu' and user = '$user';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $response["code"] = 1;
        $response["pesan"] = "data berhasil dihapus";
    }else{
        $response["code"] = 0;
        $response["pesan"] = "data gagal dihapus";
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);
?>