<?php 

$path = "../../boards/";
$filename = $path . "/curr.out";
if (isset($_GET['filename']))
	$filename = $path . $_GET['filename'];

read_boards($filename);

function read_boards($filename) {
	$file_handle = fopen($filename, "r");
	$list = array();
	while (!feof($file_handle)) {
		$arr = explode(",",str_replace("\n","",fgets($file_handle)));
		if (count($arr) != 3)
			continue;
		$h = $arr[0];
		$w = $arr[1];
		$name = $arr[2];
		$ret = array();
		for ($i=0;$i<$h;$i++) {
			$ret[] = explode(",", str_replace("\n","",fgets($file_handle)));
		}
		$list[] = array( "board"=>$ret, "w"=>$w,"h"=> $h, "name"=>$name );
	}
	fclose($file_handle);
	echo json_encode($list);
	
}

?>