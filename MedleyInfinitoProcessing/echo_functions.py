import os

def retrieve_inf(file_path):

  from pyechonest import artist, track, config, song
  config.ECHO_NEST_API_KEY = "WUODI5WC8Y1QIHJTJ"

  filename = file_path
  _ = os.popen("avconv -y -i " + filename + " -f ffmetadata metadata.txt").read()

  with open("metadata.txt", 'r') as outfile:
      line=outfile.read().split('\n')

      Title = [l for l in line if "title" in l][0].split('=')[-1]
      Artist = [l for l in line if "artist" in l][0].split('=')[-1]

  _ = os.system("rm metadata.txt")

  Image = artist.search(name=Artist)[0].get_images()[0]['url']

  return Title, Image, Artist
