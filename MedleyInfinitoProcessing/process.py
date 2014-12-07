import argparse
import glob
import os

import database
import split_mp3
from replace_filename_spaces import replace_spaces
import sonic_functions


def process(input_dir, output_dir, parts, length):
    replace_spaces(input_dir)
    original_mp3_files = glob.glob(input_dir + "/*.mp3")

    for input_file_path in original_mp3_files:
        mp3files = split_mp3.split_file(input_file_path, output_dir, parts, length)
        for filepath in mp3files:
            print "Processing {}".format(filepath)
            print "Check if already in database"
            analyze = database.doesnt_exist(filepath)
            if analyze:
                print "Analyze"
                try:
                    key, tempo, start, end  = sonic_functions.analyze(filepath)
                    if key > 11:
                        key = ((key - 9) % 12)
                    database.insert(filepath, key, tempo)
                    # crop the edges
                    print "Crop the edges"
                    os.system(
                        "avconv -y -i {} -acodec copy -ss {} -t {} {}".format(
                            filepath,
                            split_mp3.convert_time(start),
                            split_mp3.convert_time(end-start),
                            filepath
                        )
                    )
                except KeyError:
                    pass
            else:
                print "Skipping... Already in database"


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("input_dir", type=str)
    parser.add_argument("output_dir", type=str)
    parser.add_argument("-n", type=int, help="number of parts (max)")
    parser.add_argument("-l", type=int, help="length of each part (seconds)")

    args = parser.parse_args()

    process(args.input_dir, args.output_dir, args.n, args.l)
