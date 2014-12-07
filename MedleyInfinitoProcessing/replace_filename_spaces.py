#!/usr/bin/env python
import glob
import os


def replace_spaces(filespath):
    dir_contents = glob.glob(filespath + "/*.mp3")

    filelist = obtain_filenames(dir_contents)
    remove_special_characters(filelist)
    obtain_filenames(dir_contents)
    files_with_spaces = filename_has_space(filelist)
    replace_space(files_with_spaces)


def obtain_filenames(dir_contents):
    '''build list of files from directory contents'''
    filelist = []
    for item in dir_contents:
        if os.path.isfile(item):
            filelist.append(item)
        else:
            continue
    return filelist


def remove_special_characters(filelist):
    for filename in filelist:
        newfilename = filename
        for sc in "!@#$%^&*()[]{}';:,<>?|`~-=+":
            newfilename = newfilename.replace(sc, "_")
        print filename, newfilename
        # see if newfilename already exists
        if os.path.isfile(newfilename):
            pass
        else:
            os.rename(filename, newfilename)


def filename_has_space(filelist):
    '''build list of filenames containing spaces'''
    files_with_spaces = []
    for filename in filelist:
        if filename.find(" ") > -1:
            files_with_spaces.append(filename)
        else:
            continue
    return files_with_spaces

def replace_space(files_with_spaces):
    '''rename files replacing spaces with underscores, ignores directories'''
    #test if files_with_spaces has files
    if not files_with_spaces:
        print "No filenames containing spaces found."
        return
    else:
        print "Replacing spaces with underscores in filenames:"
    for filename in files_with_spaces:
        newfilename = filename.replace(' ', '_')
        # see if newfilename already exists
        if os.path.isfile(newfilename):
            print "Name collision: cannot rename '%s' --- '%s' already exists" % (filename, newfilename)
            pass
        else:
            os.rename(filename, newfilename)
            print "Success: ", filename, " >> ", newfilename
