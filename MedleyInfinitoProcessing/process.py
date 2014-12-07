import argparse
import glob
import os

import insert_data_in_db
import split_mp3


def process(input_dir, output_dir, parts, length):
    split_mp3.split(input_dir, output_dir, parts, length)
    mp3files = glob.glob(output_dir + "/*.mp3")

    for filepath in mp3files:
        key, tempo = (5, 120)
        insert_data_in_db.insert(filepath, key, tempo)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("input_dir", type=str)
    parser.add_argument("output_dir", type=str)
    parser.add_argument("-n", type=int, help="number of parts (max)")
    parser.add_argument("-l", type=int, help="length of each part (seconds)")

    args = parser.parse_args()

    process(args.input_dir, args.output_dir, args.n, args.l)
