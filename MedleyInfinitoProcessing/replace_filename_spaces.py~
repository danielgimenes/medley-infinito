#!/usr/bin/env python
import os
import glob

# converts spaces to underscores in filenames only 
#    ignores directories
#    tests for existing file with same name and throws error

# get current working directory && get cwd contents, returns list
cwd = os.getcwd()
dir_contents = os.listdir(cwd)

filelist = []
files_with_spaces = []

def obtain_filenames(dir_contents):
    '''build list of files from directory contents'''
    for item in dir_contents:
        if os.path.isfile(item):
            filelist.append(item)
        else:
            continue

def filename_has_space(filelist):
    '''build list of filenames containing spaces'''
    for filename in filelist:
        if filename.find(" ") > -1:
            files_with_spaces.append(filename)
        else:
            continue

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


if __name__ == '__main__':
    obtain_filenames(dir_contents)
    filename_has_space(filelist)
    replace_space(files_with_spaces)
