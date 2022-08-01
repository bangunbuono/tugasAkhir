<?php
require("connection.php");
$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST["id"];
    $bahan = $_POST["bahan"];
    $user = $_POST["user"];

    mysqli_begin_transaction($connect);

    // try {
    //     mysqli_query($connect, "DELETE from stocks where id = '$id';");
    //     mysqli_query($connect, "DELETE from komposisi_opsi where bahan = '$bahan' and user = '$user';");
    //     mysqli_query($connect, "DELETE from komposisi_opsi where bahan = '$bahan' and user = '$user';");

    //     mysqli_commit($connect);
    //     $response["code"] = 1;
    //     $response["pesan"] = "data berhasil dihapus";

    // } catch (mysqli_sql_exception $e) {
    
    //     mysqli_rollback($connect);
    //     $response["code"] = 0;
    //     $response["pesan"] = "data gagal dihapus";
    //     throw $e;
        
    // }    

    $query = "DELETE from stocks where id = '$id';";
    $execute = mysqli_query($connect, $query);
    $check = mysqli_affected_rows($connect);

    if($check > 0){
        $query2 = "DELETE from komposisi where bahan = '$bahan' and user = '$user';";
        $execute2 = mysqli_query($connect, $query2);
        $check2 = mysqli_affected_rows($connect);

        if($check2 > 0){
            $query3 = "DELETE from komposisi_opsi where bahan = '$bahan' and user = '$user';";
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