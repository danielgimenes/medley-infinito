import argparse
import glob
import os

import insert_data_in_db
import split_mp3
from replace_filename_spaces import replace_spaces
from sonic_functions import analyze


def process(input_dir, output_dir, parts, length):
    replace_spaces(input_dir)
    split_mp3.split(input_dir, output_dir, parts, length)
    mp3files = glob.glob(output_dir + "/*.mp3")

    for filepath in mp3files:
        print "Analyzing {}".format(filepath)
        key, tempo, start, end  = analyze(filepath)
        insert_data_in_db.insert(filepath, key, tempo)
        # crop the edges
        os.system(
            "avconv -y -i {} -acodec copy -ss {} -t {} {}".format(
                filepath,
                split_mp3.convert_time(start),
                split_mp3.convert_time(start-end),
                filepath
            )
        )


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("input_dir", type=str)
    parser.add_argument("output_dir", type=str)
    parser.add_argument("-n", type=int, help="number of parts (max)")
    parser.add_argument("-l", type=int, help="length of each part (seconds)")

    args = parser.parse_args()

    process(args.input_dir, args.output_dir, args.n, args.l)
