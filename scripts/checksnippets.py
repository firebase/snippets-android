"""Script for checking that Java and Kotlin snippets are in line.

Example:
$ python scripts/checksnippets.py
Checking snippets in folder: storage/app/src/
ERROR: Missing kotlin file for java file FirebaseUIActivity.java
ERROR: The following snippets are missing from StorageActivity.kt: set(['storage_custom_app'])
ERROR: Missing kotlin file for java file UploadActivity.java
ERROR: Missing kotlin file for java file DownloadActivity.java
"""
import sys
import glob
import fnmatch
import os
import re

_RE_REGION_TAG_START = re.compile(r'\[START ([\w_\-]+)\]')

class MissingKotlinFile(Exception):

    def __init__(self, javaName):
        self.javaName = javaName
    
    def __str__(self):
        return 'ERROR: Missing kotlin file for java file {}'.format(self.javaName)

class RegionTagMismatch(Exception):

    def __init__(self, kotlinName, regionDiff):
        self.kotlinName = kotlinName
        self.regionDiff = regionDiff

    def __str__(self):
         return 'ERROR: The following snippets are missing from {}: {}'.format(
             self.kotlinName, self.regionDiff)

def checkSnippets(folder):
    print 'Checking snippets in folder: {}'.format(folder)
    javaFiles = findFileWithPattern(folder, '*.java')

    for f in javaFiles:
        checkJavaFile(folder, f)

    print 'Done'


def checkJavaFile(folder, javaFile):
    javaRegions = regionsInFile(javaFile)

    javaName = os.path.basename(javaFile)
    kotlinName = javaName.replace(".java", ".kt")

    # If the Java file has no snippet tags, we don't care about kotlin
    if len(javaRegions) == 0:
        return

    # Check to make sure a matching kotlin file exists
    kotlinFiles = findFileWithPattern(folder, kotlinName)
    if len(kotlinFiles) == 0:
        raise MissingKotlinFile(javaName)

    # Find all regions in the kotlin file, and check if they differ from the java file
    kotlinFile = kotlinFiles[0]
    kotlinRegions = regionsInFile(kotlinFile)

    regionDiff = javaRegions.difference(kotlinRegions)
    if len(regionDiff) > 0:
        raise RegionTagMismatch(kotlinName, regionDiff)

    print 'SUCCESS: {} <--> {}'.format(javaName, kotlinName)


def regionsInFile(path):
    regions = set()
    with open(path, 'r') as f:
        lines = f.read().split('\n')
        for line in lines:
            m = _RE_REGION_TAG_START.search(line)
            if m:
                regions.add(m.group(1))

    return regions
        

def findFileWithPattern(folder, pattern):
    matches = []
    for root, dirnames, filenames in os.walk(folder):
        for dirname in fnmatch.filter(dirnames, pattern):
            matches.append(os.path.join(root, dirname))

        for filename in fnmatch.filter(filenames, pattern):
            matches.append(os.path.join(root, filename))

    return matches

if __name__ == "__main__":
    sourceFolders = findFileWithPattern('.', 'src')
    for folder in sourceFolders:
        checkSnippets(folder)
