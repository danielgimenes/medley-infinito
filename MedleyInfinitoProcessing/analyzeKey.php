<?php
$accessId = 'bc59492a-d127-487b-acb4-c2e4226b34cb';
$taskUrl = 'analyze/key';
$parameters = array();
$parameters['access_id'] = $accessId;
$parameters['format'] = 'json';

$parameters['input_file'] = 'http://www.sonicAPI.com/music/brown_eyes_by_ueberschall.mp3';

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

