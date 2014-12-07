import argparse
import glob
import os


def main(input_dir, output_dir, parts, length):
    # ignore first 15 seconds of the music file
    start_border = 15

    mp3files = glob.glob(input_dir + "/*.mp3")

    def convert_time(sec):
        hh = sec/(60 * 60)
        mm = sec/(60)
        ss = sec % 60
        return "{}:{}:{}".format(hh, mm, ss)

    for inputpath in mp3files:
        filename = inputpath.split("/")[-1]
        print "Processing {}".format(filename)
        for part in range(parts):
            print "-- Part {} of {}".format(part, parts)
            start = start_border + part * length
            os.system(
                "avconv -i {} -acodec copy -ss {} -t {} {}".format(
                    inputpath,
                    convert_time(start),
                    convert_time(length),
                    output_dir+filename[:-4]+"_"+str(part)+".mp3"
                )
            )


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("input_dir", type=str)
    parser.add_argument("output_dir", type=str)
    parser.add_argument("-n", type=int, help="number of parts (max)")
    parser.add_argument("-l", type=int, help="length of each part (seconds)")

    args = parser.parse_args()

    main(args.input_dir, args.output_dir, args.n, args.l)
