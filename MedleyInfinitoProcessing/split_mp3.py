import argparse
import glob
import os


def convert_time(seconds):
    seconds = int(seconds)
    hh = seconds/(60 * 60)
    mm = seconds/(60)
    ss = seconds % 60
    return "{}:{}:{}".format(hh, mm, ss)


def split(input_dir, output_dir, parts, length):
    # ignore first 15 seconds of the music file
    start_border = 15

    mp3files = glob.glob(input_dir + "/*.mp3")

    for inputpath in mp3files:
        filename = inputpath.split("/")[-1]
        print "Processing {}".format(filename)
        for part in range(parts):
            print "-- Part {} of {}".format(part, parts)
            start = start_border + part * length
    	    avconv_str = "avconv -y -i {} -acodec copy -ss {} -t {} {}".format(
    	         inputpath,
                 convert_time(start),
    	         convert_time(length),
    	         output_dir+filename[:-4]+"_"+str(part)+".mp3"
    	    )
    	    print avconv_str
            os.system(avconv_str)

def split_file(input_file_path, output_dir, parts, length):
    # ignore first 15 seconds of the music file
    start_border = 15

    filename = input_file_path.split("/")[-1]
    print "Processing {}".format(filename)
    part_names = []
    for part in range(parts):
        print "-- Part {} of {}".format(part, parts)
        start = start_border + part * length
        part_name = output_dir+filename[:-4]+"_"+str(part)+".mp3"
        avconv_str = "avconv -y -i {} -acodec copy -ss {} -t {} {}".format(
             input_file_path,
             convert_time(start),
             convert_time(length),
             part_name
        )
        part_names.append(part_name)
        print avconv_str
        os.system(avconv_str)
    return part_names


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("input_dir", type=str)
    parser.add_argument("output_dir", type=str)
    parser.add_argument("-n", type=int, help="number of parts (max)")
    parser.add_argument("-l", type=int, help="length of each part (seconds)")

    args = parser.parse_args()

    split(args.input_dir, args.output_dir, args.n, args.l)
