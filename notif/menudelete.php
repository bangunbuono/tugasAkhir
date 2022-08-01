<?php
require("connection.php");
$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];
    $menu = $_POST["menu"];
    $user = $_POST["user"];

    $query = "DELETE from menu where id = '$id';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $query2 = "DELETE from komposisi where menu = '$menu' and user = '$user';";
        $execute2 = mysqli_query($connect, $query2);
        $check2 = mysqli_affected_rows($connect);

        if($check2 > 0){
            $query3 = "DELETE from komposisi_opsi where menu = '$menu' and user = '$user';";
            $execute3 = mysqli_query($connect, $query3);
            $check3 = mysqli_affected_rows($connect);
            
            if ($check3){
                $response["code"] = 1;
                $response["pesan"] = "data berhasil dihapus";
            }else{
                $response["code"] = 0;
                $response["pesan"] = "data gagal dihapus";
            }
            
        }else{
            $response["code"] = 0;
            $response["pesan"] = "data gagal dihapus";
        }
    }
    else{
        
    }
}
else{
    $response["code"] = 0;
    $response["pesan"] = "tidak ada data";
}

echo json_encode($response);
mysqli_close($connect);
?>