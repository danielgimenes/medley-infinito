import os, sys
import subprocess
import xml.etree.ElementTree as ET
import json
import commands

####GET FILE_ID VIA CURL

def init(file_path):

  accessId ='03806329-d448-46fb-b21b-c2ef41b3e81d '

  arg = "https://api.sonicapi.com/file/upload?access_id="+ accessId + "-Ffile=@" + file_path

  xml_response = os.popen("curl " + arg).read()

  tree = ET.fromstring(xml_response)
  file_id = tree.findall('file').pop().get('file_id')
  return file_id

####Call PHP function
# shell execute PHP

def analyze(file_path):

  file_id = init(file_path)

  response = commands.getoutput("php -f MedleyInfinitoProcessing/analyzeTempo.php " + file_id)

  json_dict = json.loads(response)

  first_click = json_dict.get('auftakt_result').get('click_marks')[0]['time']

  last_click = json_dict.get('auftakt_result').get('click_marks')[-1]['time']

  overall_tempo = json_dict.get('auftakt_result').get('overall_tempo')

  response = commands.getoutput("php -f MedleyInfinitoProcessing/analyzeKey.php " + file_id)

  json_dict = json.loads(response)

  key_index = json_dict.get('tonart_result').get('key_index')

  return key_index, overall_tempo, first_click, last_click
