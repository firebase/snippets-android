"""Script for checking that Java and Kotlin snippets are in line.

Example:
$ python scripts/checksnippets.py storage/app/src/
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

def checkSnippets(folder):
    print 'Checking snippets in folder: {}'.format(folder)
    javaFiles = findWithPattern(folder, '*.java')

    for f in javaFiles:
        checkJavaFile(folder, f)


def checkJavaFile(folder, javaFile):
    javaRegions = regionsInFile(javaFile)

    javaName = os.path.basename(javaFile)
    kotlinName = javaName.replace(".java", ".kt")

    # If the Java file has no snippet tags, we don't care about kotlin
    if len(javaRegions) == 0:
        return

    # Check to make sure a matching kotlin file exists
    kotlinFiles = findWithPattern(folder, kotlinName)
    if len(kotlinFiles) == 0:
        print 'ERROR: Missing kotlin file for java file {}'.format(javaName)
        return

    # Find all regions in the kotlin file, and check if they differ from the java file
    kotlinFile = kotlinFiles[0]
    kotlinRegions = regionsInFile(kotlinFile)

    regionDiff = javaRegions.difference(kotlinRegions)
    if len(regionDiff) > 0:
        print 'ERROR: The following snippets are missing from {}: {}'.format(kotlinName, regionDiff)


def regionsInFile(path):
    regions = set()
    with open(path, 'r') as f:
        lines = f.read().split('\n')
        for line in lines:
            m = _RE_REGION_TAG_START.search(line)
            if m:
                regions.add(m.group(1))

    return regions
        

def findWithPattern(folder, pattern):
    matches = []
    for root, _, filenames in os.walk(folder):
        for filename in fnmatch.filter(filenames, pattern):
            matches.append(os.path.join(root, filename))
    return matches

if __name__ == "__main__":
    folder = sys.argv[1]
    checkSnippets(folder)
