<?php


$accessId = '03806329-d448-46fb-b21b-c2ef41b3e81d';
$taskUrl = 'analyze/tempo';
$parameters = array();
$parameters['access_id'] = $accessId;
$parameters['format'] = 'json';

$file_id = $argv[1];
$parameters['input_file'] = $file_id;

// important: the calls require the CURL extension for PHP
$ch = curl_init('https://api.sonicAPI.com/' . $taskUrl);
curl_setopt($ch, CURLOPT_HEADER, FALSE);
curl_setopt($ch, CURLOPT_POST, TRUE);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
curl_setopt($ch, CURLOPT_POSTFIELDS, $parameters);
// you can remove the following line when using http instead of https, or
// you point curl to the CA certificate
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

$httpResponse = curl_exec($ch);
$infos = curl_getinfo($ch);
curl_close($ch);

$response = json_decode($httpResponse);
print json_encode($response);

//if ($infos['http_code'] == 200) {
//    echo "Task succeeded, analysis result:<br />" . json_encode($response);
//    } else {
//        $errorMessages = array_map(function($error) { return $error->message; }, $response->errors);
//            
//                echo 'Task failed, reason: ' . implode('; ', $errorMessages);
//                }
